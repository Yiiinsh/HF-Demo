#!/bin/bash

CHANNEL_NAME="$1"
: ${CHANNEL_NAME:="mychannel"}
: ${TIMEOUT:="60"}
COUNTER=0
MAX_RETRY=5
ORDERER_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/cacerts/example.com-cert.pem

echo "Channel name : "$CHANNEL_NAME


verifyResult () {
	if [ $1 -ne 0 ] ; then
		echo "!!!!!!!!!!!!!!! "$2" !!!!!!!!!!!!!!!!"
                echo "================== ERROR !!! FAILED to execute End-2-End Scenario =================="
		echo
   		exit 1
	fi
}

setGlobals () {

	if [ $1 -eq 0 -o $1 -eq 1 ] ; then
		CORE_PEER_LOCALMSPID="Org0MSP"
		if [ $1 -eq 0 ]; then
			CORE_PEER_ADDRESS=peer0.org1.example.com:7051
			CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/cacerts/org1.example.com-cert.pem
			CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com
		else
			CORE_PEER_ADDRESS=peer1.org1.example.com:7051
			CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer1.org1.example.com/cacerts/org1.example.com-cert.pem
			CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer1.org1.example.com
		fi
	else
		CORE_PEER_LOCALMSPID="Org1MSP"
		if [ $1 -eq 2 ]; then
			CORE_PEER_ADDRESS=peer0.org2.example.com:7051
			CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/cacerts/org2.example.com-cert.pem
			CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com
		else
			CORE_PEER_ADDRESS=peer1.org2.example.com:7051
			CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer1.org2.example.com/cacerts/org2.example.com-cert.pem
			CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer1.org2.example.com
		fi

	fi
	env |grep CORE
}

createChannel() {
	CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com
	CORE_PEER_LOCALMSPID="OrdererMSP"
	env |grep CORE

        if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer channel create -o orderer.example.com:7050 -c $CHANNEL_NAME -f channel.tx >&log.txt
	else
		peer channel create -o orderer.example.com:7050 -c $CHANNEL_NAME -f channel.tx --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Channel creation failed"
	echo "===================== Channel \"$CHANNEL_NAME\" is created successfully ===================== "
	echo
}

## Sometimes Join takes time hence RETRY atleast for 5 times
joinWithRetry () {
	peer channel join -b $CHANNEL_NAME.block  >&log.txt
	res=$?
	cat log.txt
	if [ $res -ne 0 -a $COUNTER -lt $MAX_RETRY ]; then
		COUNTER=` expr $COUNTER + 1`
		echo "PEER$1 failed to join the channel, Retry after 2 seconds"
		sleep 2
		joinWithRetry $1
	else
		COUNTER=0
	fi
        verifyResult $res "After $MAX_RETRY attempts, PEER$ch has failed to Join the Channel"
}

joinChannel () {
	for ch in 0 1 2 3; do
		setGlobals $ch
		joinWithRetry $ch
		echo "===================== PEER$ch joined on the channel \"$CHANNEL_NAME\" ===================== "
		sleep 2
		echo
	done
}

installChaincode () {
	PEER=$1
	setGlobals $PEER
	peer chaincode install -n mycc -v 1.0 -p github.com/hyperledger/fabric/examples/chaincode/go/hfdemo >&log.txt
	res=$?
	cat log.txt
        verifyResult $res "Chaincode installation on remote peer PEER$PEER has Failed"
	echo "===================== Chaincode is installed on remote peer PEER$PEER ===================== "
	echo
}

instantiateChaincode () {
	PEER=$1
	setGlobals $PEER
        if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode instantiate -o orderer.example.com:7050 -C $CHANNEL_NAME -n mycc -v 1.0 -c '{"Args":["init"]}' -P "OR	('Org0MSP.member','Org1MSP.member')" >&log.txt
	else
		peer chaincode instantiate -o orderer.example.com:7050 --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n mycc -v 1.0 -c '{"Args":["init"]}' -P "OR	('Org0MSP.member','Org1MSP.member')" >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Chaincode instantiation on PEER$PEER on channel '$CHANNEL_NAME' failed"
	echo "===================== Chaincode Instantiation on PEER$PEER on channel '$CHANNEL_NAME' is successful ===================== "
	echo
}

chaincodeQuery () {
  PEER=$1
  echo "===================== Querying on PEER$PEER on channel '$CHANNEL_NAME'... ===================== "
  setGlobals $PEER
  local rc=1
  local starttime=$(date +%s)

  while test "$(($(date +%s)-starttime))" -lt "$TIMEOUT" -a $rc -ne 0
  do
     sleep 3
     echo "Attempting to Query PEER$PEER ...$(($(date +%s)-starttime)) secs"
     peer chaincode query -C $CHANNEL_NAME -n mycc -c '{"Args":["query","123456"]}' >&log.txt
     test $? -eq 0 && VALUE=$(cat log.txt | awk '/Query Result/ {print $NF}')
     let rc=0
  done
  echo
  cat log.txt
}

chaincodeInvoke () {
        PEER=$1
        setGlobals $PEER
        if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.example.com:7050 -C $CHANNEL_NAME -n mycc -c '{"Args":["insert", "{\"id\":\"123456\",\"name\":\"aaa\",\"university\":\"tju\",\"degree\":\"bachelor\",\"start\":\"2015-01-01\",\"end\":\"2017-01-01\",\"educationQualifications\":[\"school\",\"junior high school\",\"high school\"],\"internInfos\":[{\"studentId\":\"123456\",\"name\":\"aaa\",\"workingId\":\"S99999\",\"company\":\"companyA\",\"department\":\"test\",\"position\":\"developer\",\"start\":\"2016-01-01\",\"end\":\"2016-03-10\"},{\"studentId\":\"123456\",\"name\":\"aaa\",\"workingId\":\"B00000\",\"company\":\"companyB\",\"department\":\"development\",\"position\":\"developer\",\"start\":\"2016-03-20\",\"end\":\"2016-06-10\"}]}"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.example.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n mycc -c '{"Args":["invoke","a","b","10"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke execution on PEER$PEER failed "
	echo "===================== Invoke transaction on PEER$PEER on channel '$CHANNEL_NAME' is successful ===================== "
	echo
}

chaincodeInvokeUpdate() {
    PEER=$1
    setGlobals $PEER
	peer chaincode invoke -o orderer.example.com:7050 -C $CHANNEL_NAME -n mycc -c '{"Args":["update", "{\"id\":\"123456\",\"name\":\"aaa\",\"university\":\"tongjiuniversity\",\"degree\":\"bachelor\",\"start\":\"2015-01-01\",\"end\":\"2017-01-01\",\"educationQualifications\":[\"school\",\"junior high school\",\"high school\"],\"internInfos\":[{\"studentId\":\"123456\",\"name\":\"aaa\",\"workingId\":\"S99999\",\"company\":\"companyA\",\"department\":\"test\",\"position\":\"developer\",\"start\":\"2016-01-01\",\"end\":\"2016-03-10\"},{\"studentId\":\"123456\",\"name\":\"aaa\",\"workingId\":\"B00000\",\"company\":\"companyB\",\"department\":\"development\",\"position\":\"developer\",\"start\":\"2016-03-20\",\"end\":\"2016-06-10\"}]}"]}' >&log.txt
	res=$?
	cat log.txt
	verifyResult $res "Invoke execution on PEER$PEER failed "
	echo "===================== Invoke transaction on PEER$PEER on channel '$CHANNEL_NAME' is successful ===================== "
	echo
}

## Create channel
createChannel

## Join all the peers to the channel
joinChannel

## Install chaincode on Peer0/Org0 and Peer2/Org1
installChaincode 0
installChaincode 2

#Instantiate chaincode on Peer2/Org1
echo "Instantiating chaincode on Peer2/Org0 ..."
instantiateChaincode 2

#Query on chaincode on Peer0/Org0
chaincodeQuery 0 100

#Invoke on chaincode on Peer0/Org0
echo "send Invoke transaction on Peer0/Org0 ..."
chaincodeInvoke 0

### Install chaincode on Peer3/Org1
installChaincode 3

##Query on chaincode on Peer3/Org1, check if the result is 90
chaincodeQuery 3 90

chaincodeInvokeUpdate 0

chaincodeQuery 3

echo
echo "===================== All GOOD, End-2-End execution completed ===================== "
echo
#exit 0
