package org.tju.HFDemo.core.manager.hf;

import com.google.common.reflect.TypeToken;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.tju.HFDemo.core.constant.HFOperations;
import org.tju.HFDemo.core.dto.StudentInfo;
import org.tju.HFDemo.core.exception.OperationFailException;
import org.tju.HFDemo.core.manager.AbstractManager;
import org.tju.HFDemo.core.role.DefaultUser;
import org.tju.HFDemo.core.role.User;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shaohan.yin on 02/05/2017.
 */
public class DefaultHFManager extends AbstractManager implements HFManager {
    private static final String STUDENT_INFO_CHAIN = "mychannel";

    private HFClient client = HFClient.createNewInstance();
    private Map<String, Chain> chains = new HashMap<>();
    private Map<String, ChainCodeID> chainCodeIDs = new HashMap<>();
    private User admin;

    DefaultHFManager() {
        loadChain();
        loadAdmin();
    }

    private void loadChain() {
        config.getChainNames().stream().forEach((chainName) -> {
            try {
                logger.info("[DefaultHFManager][load]Chain:{}", chainName);
                Chain chain = client.newChain(chainName);
                chain.addOrderer(client.newOrderer(config.getOrdererName(), config.getOrdererLocation()));
                config.getPeerNames().stream().forEach((peerName) -> {
                    try {
                        chain.addPeer(client.newPeer(peerName, config.getPeerLocation(peerName)));
                        // TODO: add eventhub
                        //chain.addEventHub(client.newEventHub(peerName, config.getPeerEventHub(peerName)));
                    } catch (InvalidArgumentException e) {
                        logger.error("[DefaultHFManager][add peer][fail]Chain:{}, Peer:{}", chainName, peerName, e);
                    }
                });
                chain.initialize();

                ChainCodeID chainCodeID = ChainCodeID.newBuilder()
                        .setName(config.getChainCodeName(chainName))
                        .setVersion(config.getChainCodeVersion(chainName))
                        .setPath(config.getChainCodePath(chainName))
                        .build();

                chains.put(chainName, chain);
                chainCodeIDs.put(chainName, chainCodeID);
            } catch (InvalidArgumentException e) {
                logger.error("[DefaultHFManager][load][fail]Chain:{}", chainName, e);
            } catch (TransactionException e) {
                logger.error("[DefaultHFManager][load][chain initialize fail]Chain:{}", chainName, e);
            }
        });
    }

    private void loadAdmin() {
        try {
            HFCAClient hfcaClient = new HFCAClient(config.getCALocation(), null);
            hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            admin = new DefaultUser();
            admin.setName(config.getAdminName());
            admin.setMSPID(config.getUserMSPID());
            admin.setEnrollment(hfcaClient.enroll(config.getAdminName(), config.getAdminSecret()));

            client.setUserContext(admin);
        } catch (Exception e){
            logger.error("[DefaultHFManager][load admin][fail]", e);
        }
    }

    @Override
    public Chain getChain(String name) {
        return chains.get(name);
    }

    @Override
    public StudentInfo getStudentInfo(User user, String id) {
        try {
            logger.info("[DefaultHFManager][GetStudentInfo]From:{}, Id:{}", user.getName(), id);
            client.setUserContext(user);
            QueryByChaincodeRequest request = client.newQueryProposalRequest();
            request.setChaincodeID(chainCodeIDs.get(STUDENT_INFO_CHAIN));
            request.setFcn("query");
            request.setArgs(new String[]{HFOperations.QUERY.val(), id});


            Collection<ProposalResponse> queryProposalResponses = getChain(STUDENT_INFO_CHAIN).queryByChaincode(request);
            List<ProposalResponse> successResponse = queryProposalResponses
                    .stream()
                    .filter((response) -> response.getStatus().equals(ProposalResponse.Status.SUCCESS))
                    .collect(Collectors.toList());
            if(!successResponse.isEmpty()) {
                String result = successResponse.get(0).getProposalResponse().getResponse().getPayload().toStringUtf8();
                return gson.fromJson(result, StudentInfo.class);
            } else {
                logger.error("[DefaultHFManager][GetStudentInfo][fail]User:{}, Id:{}", user.getName(), id);
                throw new OperationFailException(String.format("Fail to query.User:%s, Id:%s", user.getName(), id));
            }

        } catch (Exception e) {
            logger.error("[DefaultHFManager][GetStudentInfo][fail]",e);
        }
        return null;
    }

    @Override
    public List<StudentInfo> getStudentInfoBatch(User user, String startId, String endId) {
        try {
            logger.info("[DefaultHFManager][GetStudentInfo]From:{}, Id:{}-{}", user.getName(), startId, endId);
            client.setUserContext(user);
            QueryByChaincodeRequest request = client.newQueryProposalRequest();
            request.setChaincodeID(chainCodeIDs.get(STUDENT_INFO_CHAIN));
            request.setFcn("query");
            request.setArgs(new String[]{HFOperations.QUERY_BATCH.val(), startId, endId});

            Collection<ProposalResponse> queryProposalResponses = getChain(STUDENT_INFO_CHAIN).queryByChaincode(request);
            List<ProposalResponse> successResponse = queryProposalResponses
                    .stream()
                    .filter((response) -> response.getStatus().equals(ProposalResponse.Status.SUCCESS))
                    .collect(Collectors.toList());
            if(!successResponse.isEmpty()) {
                String result = successResponse.get(0).getProposalResponse().getResponse().getPayload().toStringUtf8();

                Type listType = new TypeToken<List<StudentInfo>>(){}.getType();
                return gson.fromJson(result, listType);
            } else {
                logger.error("[DefaultHFManager][GetStudentInfo][fail]User:{}, Id:{}-{}", user.getName(), startId, endId);
                throw new OperationFailException(String.format("Fail to query.User:%s, Id:%s-%s", user.getName(), startId, endId));
            }

        } catch (Exception e) {
            logger.error("[DefaultHFManager][GetStudentInfo][fail]",e);
        }
        return null;
    }

    @Override
    public void updateStudentInfo(User user, StudentInfo studentInfo) {
        try {
            logger.info("[DefaultHFManger][UpdateStudentInfo]From:{}, NewInfo:{}", user.getName(), studentInfo);
            client.setUserContext(user);
            TransactionProposalRequest request = client.newTransactionProposalRequest();
            request.setChaincodeID(chainCodeIDs.get(STUDENT_INFO_CHAIN));
            request.setFcn("invoke");
            request.setArgs(new String[]{HFOperations.UPDATE.val(), gson.toJson(studentInfo)});

            Collection<ProposalResponse> responses = getChain(STUDENT_INFO_CHAIN).sendTransactionProposal(request);
            List<ProposalResponse> successResponse = responses
                    .stream()
                    .filter((response) -> response.getStatus().equals(ProposalResponse.Status.SUCCESS))
                    .collect(Collectors.toList());
            if(!successResponse.isEmpty()) {
                // TODO: handle async response
                getChain(STUDENT_INFO_CHAIN).sendTransaction(successResponse);
            } else {
                logger.error("[DefaultHFManager][UpdateStudentInfo][fail]User:{}, Info:{}", user.getName(), studentInfo);
                throw new OperationFailException(String.format("Fail to update.User:%s, Info:%s", user.getName(), gson.toJson(studentInfo)));
            }
        } catch (Exception e) {
            logger.error("[DefaultHFManager][UpdateStudentInfo][fail]",e);
        }
    }

    @Override
    public void removeStudentInfo(User user, String id) {
        try {
            logger.info("[DefaultHFManger][RemoveStudentInfo]From:{}, Id:{}", user.getName(), id);
            client.setUserContext(user);
            TransactionProposalRequest request = client.newTransactionProposalRequest();
            request.setChaincodeID(chainCodeIDs.get(STUDENT_INFO_CHAIN));
            request.setFcn("invoke");
            request.setArgs(new String[]{HFOperations.REMOVE.val(), id});

            Collection<ProposalResponse> responses = getChain(STUDENT_INFO_CHAIN).sendTransactionProposal(request);
            List<ProposalResponse> successResponse = responses
                    .stream()
                    .filter((response) -> response.getStatus().equals(ProposalResponse.Status.SUCCESS))
                    .collect(Collectors.toList());
            if(!successResponse.isEmpty()) {
                // TODO: handle async response
                getChain(STUDENT_INFO_CHAIN).sendTransaction(successResponse);
            } else {
                logger.error("[DefaultHFManager][UpdateStudentInfo][fail]User:{}, Id:{}", user.getName(), id);
                throw new OperationFailException(String.format("Fail to update.User:%s, Id:%s", user.getName(), id));
            }
        } catch (Exception e) {
            logger.error("[DefaultHFManager][UpdateStudentInfo][fail]",e);
        }
    }

}
