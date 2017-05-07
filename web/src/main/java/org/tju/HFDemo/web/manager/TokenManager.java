package org.tju.HFDemo.web.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.tuple.Pair;
import org.tju.HFDemo.core.role.User;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by shaohan.yin on 06/05/2017.
 */
public class TokenManager {
    // UserId, <Token, UserInfo>
    private LoadingCache<String, Pair<String, User>> tokenCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.MAX_VALUE)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, Pair<String, User>>() {
                        @Override
                        public Pair<String, User> load(String key) throws Exception {
                            return null;
                        }
                    }
            );

    public void putToken(String userId,String token, User user) {
//        if(checkToken(userId, token)) {
//            throw new RuntimeException(String.format("Token of user %s already exists", userId));
//        } else {
//            tokenCache.put(userId, Pair.of(token, user));
//        }
        tokenCache.put(userId, Pair.of(token, user));
    }

    public User getHFUserFromToken(String userId, String token) throws ExecutionException {
        if(checkToken(userId, token)) {
            return tokenCache.get(userId).getRight();
        }
        return null;
    }

    public boolean checkUser(String userId) {
        return getTokenMap().containsKey(userId);
    }

    public boolean checkToken(String userId, String token) {
        Map<String, Pair<String, User>> tokenMap = getTokenMap();
        if(tokenMap.containsKey(userId)) {
            Pair<String, User> pair = tokenMap.get(userId);
            if(pair.getLeft().equals(token)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void deleteToken(String userId, String token) {
        if(checkToken(userId, token)) {
            tokenCache.invalidate(userId);
        }
    }

    private Map<String, Pair<String, User>> getTokenMap() {
        return tokenCache.asMap();
    }

}
