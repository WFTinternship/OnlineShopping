package com.workfront.internship.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Workfront on 7/28/2016.
 */
public class HashManager {
    public static String getHash(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] result = messageDigest.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                stringBuffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while tring to calculate hash", e);
        }
    }
}
