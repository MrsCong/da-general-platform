package com.dgp.common.security.aes;

import com.dgp.common.utils.Base64Util;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

@Slf4j
public class AesEncryptUtils {


    /**
     * @param aesKey
     * @param content
     * @author:LQL
     * @date:2019年10月30日
     * @return: String
     * @return: String
     * @description:aes 128位加密字符串
     */
    public static String encode(String aesKey, String content) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(aesKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(Base64Util.encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (Exception e) {
            log.error("aes加密失敗:{}", e);
        }

        return null;
    }

    /**
     * @param aesKey
     * @param encrypText
     * @author:LQL
     * @date:2019年10月30日
     * @return: String
     * @return: String
     * @description:aes128位解密密文
     */
    public static String decode(String aesKey, String encrypText) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(aesKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = Base64Util.decodeFast(encrypText);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (Exception e) {
            log.error("aes解密失敗:{}", e);
        }

        return null;
    }


    /**
     * @param aesKey         密钥
     * @param content        需要加密的内容
     * @param transformation cipher转化模式
     * @param iv             初始向量AES 为16bytes. DES 为8bytes
     * @author:LQL
     * @date:2019年10月30日
     * @return: String
     * @return: String
     * @description:aes128位解密密文
     */
    public static String encode(String aesKey, String content, String transformation, String iv) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(aesKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(transformation);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            if ("CBC/CFB/OFB".contains(transformation.substring(4, 7))) {//当模式为CBC/CFB/OFB时可用iv
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(Base64Util.encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (Exception e) {
            log.error("aes加密失敗:{}", e);
        }

        return null;
    }


    /**
     * @param aesKey         密钥
     * @param encrypText     需要解密的内容
     * @param transformation 密码转化模式
     * @param iv             base64编码的初始向量
     * @author:LQL
     * @date:2019年10月30日
     * @return: String
     * @return: String
     * @description:aes128位解密密文
     */

    public static String decode(String aesKey, String encrypText, String transformation,
            String iv) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(aesKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器--AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance(transformation);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY，第三个参数为初始向量
            if ("CBC/CFB/OFB".contains(transformation.substring(4, 7))) {//当模式为CBC/CFB/OFB时可用iv
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            //8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = Base64Util.decodeFast(encrypText);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (Exception e) {
            log.error("aes解密失敗:{}", e);
        }

        return null;
    }

    public static String decodeForWechat(String encrypText, String aesKey, String transformation,
            String iv) {
        try {
            log.info("使用 AES/CBC/PKCS5Padding 解密");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] ivByte = decoder.decode(iv);
            byte[] keyByte = decoder.decode(aesKey);
            byte[] encryptedByte = decoder.decode(encrypText);
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            //1.根据指定算法AES自成密码器--AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance(transformation);
            //2.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY，第三个参数为初始向量
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(ivByte));
            //3.将加密并编码后的内容解码成字节数组
            byte[] byte_decode = cipher.doFinal(encryptedByte);
            String AES_decode = new String(byte_decode, StandardCharsets.UTF_8);
            return AES_decode;
        } catch (Exception ex) {
            log.error("AES解密失败，", ex);
        }

        return null;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("使用AES对称加密，请输入加密的规则");
        String encodeRules = scanner.next();
        System.out.println("请输入要加密的内容:");
        String content = scanner.next();
        System.out.println(
                "根据输入的规则" + encodeRules + "加密后的密文是:" + AesEncryptUtils.encode(encodeRules,
                        content));

        System.out.println("使用AES对称解密，请输入加密的规则：(须与加密相同)");
        encodeRules = scanner.next();
        System.out.println("请输入要解密的内容（密文）:");
        content = scanner.next();
        System.out.println(
                "根据输入的规则" + encodeRules + "解密后的明文是:" + AesEncryptUtils.decode(encodeRules,
                        content));
    }


}
