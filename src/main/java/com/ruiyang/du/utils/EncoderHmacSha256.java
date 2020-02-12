package com.ruiyang.du.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncoderHmacSha256 {

    public static String encoderHmacSha256(String data, String key) {
        try {
            byte[] dataByte = data.getBytes();
            byte[] keyByte = key.getBytes();

            SecretKeySpec signingKey = new SecretKeySpec(keyByte, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return StringArrayUtils.byte2hex(mac.doFinal(dataByte));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
//             LOGGER.error("Md5Utils.encoderHmacSha256 NoSuchAlgorithmException,ex={}", ex.getMessage());
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
//             LOGGER.error("Md5Utils.encoderHmacSha256 InvalidKeyException,ex={}", ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
//             LOGGER.error("Md5Utils.encoderHmacSha256 Exception,ex={}", ex.getMessage());
        }
        return StringUtils.EMPTY;
    }
}
