package org.tju.HFDemo.core.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Created by shaohan.yin on 04/05/2017.
 */
public class SecretUtil {
    public static String secretHash(String secret) {
        return Hashing.sha256().hashString(secret, StandardCharsets.UTF_8).toString();
    }
}
