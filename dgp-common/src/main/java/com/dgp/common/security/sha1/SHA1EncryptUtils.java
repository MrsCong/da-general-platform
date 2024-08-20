package com.dgp.common.security.sha1;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * @author: create by huijiale
 * @date:2021/9/6
 */
@Slf4j
public class SHA1EncryptUtils {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String encrypt(String str) {
        try {
            if (str == null) {
                return null;
            }
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            log.error("sha1加密失败");
        }
        return null;
    }

}
