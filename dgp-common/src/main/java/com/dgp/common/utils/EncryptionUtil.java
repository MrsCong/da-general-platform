package com.dgp.common.utils;

import com.dgp.common.security.EncryptionConstant;
import com.dgp.common.security.aes.AesEncryptUtils;
import com.dgp.common.security.des.DesEncryptUtils;
import com.dgp.common.security.md5.Md5EncryptionUtils;
import com.dgp.common.security.rsa.RsaEncryptionUtils;
import com.dgp.common.security.sha1.SHA1EncryptUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class EncryptionUtil {

    public static EncryptionUtil me;

    private EncryptionUtil(){
        //单例
    }
    //双重锁
    public static EncryptionUtil getInstance(){
        if (me==null) {
            synchronized (EncryptionUtil.class) {
                if(me == null){
                    me = new EncryptionUtil();
                }
            }
        }
        return me;
    }


    /**
     * 加密方法（摘要）
     * @param algorithm 算法：MD5/SHA1
     * @param res 需要加密的内容
     * @return
     */
    public static String encrypt(String algorithm,String res){

        String encode = "";
        switch (algorithm){
            case EncryptionConstant.ENCRYP_MD5:
                encode = Md5EncryptionUtils.getMd5(res);
                break;
            case EncryptionConstant.ENCRYP_SHA1:
                encode = SHA1EncryptUtils.encrypt(res);
                break;
            default:
                break;
        }
        return encode;
    }

    /**
     * 加密方法（需要密钥）
     * @param algorithm 算法：DES/AES/RSA
     * @param res 需要加密的内容
     * @param key 密钥
     * @return
     */
    public static String encrypt(String algorithm,String res,String key) {
        String encode = "";
        switch (algorithm){
            case EncryptionConstant.ENCRYP_DES:
                encode = DesEncryptUtils.encrypt(res, key);
                break;
            case EncryptionConstant.ENCRYP_AES:
                encode = AesEncryptUtils.encode(key, res);
                break;
            case EncryptionConstant.ENCRYP_RSA:
                encode = RsaEncryptionUtils.encrypt(res, key);
                break;
            default:
                break;
        }
        return encode;
    }

    /**
     * 加密方法（指定cipher转化模式和iv）
     * @param algorithm 算法名称
     * @param res 需要加密的内容
     * @param key 密钥
     * @param transformation cipher转化模式
     * @param iv 初始化向量
     *           当模式为CBC/CFB/OFB时可用
     *           AES 为16bytes. DES 为8bytes
     * @return
     */
    public static String encrypt(String algorithm,String res,String key,String transformation,String iv){
        String encode = "";
        switch (algorithm){
            case EncryptionConstant.ENCRYP_DES:
                encode = DesEncryptUtils.encrypt(res, key, transformation, iv);
                break;
            case EncryptionConstant.ENCRYP_AES:
                encode = AesEncryptUtils.encode(key, res, transformation, iv);
                break;
            case EncryptionConstant.ENCRYP_RSA:
                encode = RsaEncryptionUtils.encrypt(res, key,transformation);
                break;
            default:
                break;
        }
        return encode;
    }


    /**
     * 解密方法 （需要密钥）
     * @param algorithm 算法名称
     * @param res 需要解密的内容
     * @param key 密钥
     * @return
     */
    public static String decrypt(String algorithm,String res,String key) {
        String encode = "";
        switch (algorithm){
            case EncryptionConstant.ENCRYP_DES:
                encode = DesEncryptUtils.decrypt(res, key);
                break;
            case EncryptionConstant.ENCRYP_AES:
                encode = AesEncryptUtils.decode(key, res);
                break;
            case EncryptionConstant.ENCRYP_RSA:
                encode = RsaEncryptionUtils.decrypt(res, key);
                break;
            default:
                break;
        }
        return encode;
    }

    /**
     * 解密方法（指定cipher转化模式和iv）
     * @param algorithm 算法名称
     * @param res 需要解密的内容
     * @param key 密钥
     * @param transformation cipher转化模式
     * @param iv 初始化向量
     *           当模式为CBC/CFB/OFB时可用
     *           AES 为16bytes. DES 为8bytes
     * @return
     */
    public static String decrypt(String algorithm,String res,String key,String transformation,String iv) {
        String encode = "";
        switch (algorithm){
            case EncryptionConstant.ENCRYP_DES:
                encode = DesEncryptUtils.decrypt(res, key,transformation,iv);
                break;
            case EncryptionConstant.ENCRYP_AES:
                encode = AesEncryptUtils.decode(key, res,transformation,iv);
                break;
            case EncryptionConstant.ENCRYP_RSA:
                encode = RsaEncryptionUtils.decrypt(res, key,transformation);
                break;
            default:
                break;
        }
        return encode;
    }




    /**
     * md5加密算法进行加密（不可逆）
     * @param res 需要加密的原文
     * @return
     */
    public String MD5(String res) {
        return Md5EncryptionUtils.getMd5(res);
    }



    /**
     * 使用SHA1加密算法进行加密（不可逆）
     * @param res 需要加密的原文
     * @return
     */
    public String SHA1(String res) {
        return SHA1EncryptUtils.encrypt(res);
    }



    /**
     * 使用DES加密算法进行加密（可逆）
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return
     */
    public String DESencode(String res, String key) {
        return DesEncryptUtils.encrypt(res, key);
    }

    /**
     * 使用DES加密算法进行加密（可逆）(指定工作模式，填充模式，初始化向量)
     * @param res 需要加密的原文
     * @param key 秘钥
     * @param transformation cipher转化模式
     * @return
     */
    public String DESencode(String res, String key, String transformation, String iv) {

        return DesEncryptUtils.encrypt(res, key, transformation, iv);
    }



    /**
     * 对使用DES加密算法的密文进行解密（可逆）
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public String DESdecode(String res, String key) {
        return DesEncryptUtils.decrypt(res, key);
    }

    /**
     * 对使用DES加密算法的密文进行解密（可逆）(指定工作模式，填充模式，初始化向量)
     * @param res 需要解密的密文
     * @param key 秘钥
     * @param transformation cipher转化模式
     * @return
     */
    public String DESdecode(String res, String key, String transformation, String iv) {

        return DesEncryptUtils.decrypt(res, key,transformation, iv);
    }

    /**
     * 使用AES加密算法经行加密（可逆）
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return
     */
    public String AESencode(String res, String key) {
        return AesEncryptUtils.encode(key,res);
    }

    /**
     * 使用AES加密算法经行加密（可逆）(指定工作模式，填充模式，初始化向量)
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return
     */
    public String AESencode(String res, String key, String transformation, String iv) {

        return AesEncryptUtils.encode(key,res,transformation, iv);
    }



    /**
     * 对使用AES加密算法的密文进行解密
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public String AESdecode(String res, String key) {
        return AesEncryptUtils.encode(key,res);
    }

    /**
     * 对使用AES加密算法的密文进行解密(指定工作模式，填充模式，初始化向量)
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public String AESdecode(String res, String key, String transformation, String iv) {


        return AesEncryptUtils.decode(key,res,transformation, iv);
    }

    /**
     * 使用异或进行加密
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return
     */
    public String XORencode(String res, String key) {
        byte[] bs = res.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return parseByte2HexStr(bs);
    }

    /**
     * 使用异或进行解密
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public String XORdecode(String res, String key) {
        byte[] bs = parseHexStr2Byte(res);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return new String(bs);
    }

    /**
     * 直接使用异或（第一调用加密，第二次调用解密）
     * @param res 密文
     * @param key 秘钥
     * @return
     */
    public int XOR(int res, String key) {
        return res ^ key.hashCode();
    }

    /**
     * 使用Base64进行加密
     * @param res 密文
     * @return
     */
    public String Base64Encode(String res) {
        return Base64.encode(res.getBytes());
    }

    /**
     * 使用Base64进行解密
     * @param res
     * @return
     */
    public String Base64Decode(String res) {
        return new String(Base64.decode(res));
    }



    /**将二进制转换成16进制 */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    /**将16进制转换为二进制*/
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /* public static void main(String[] args) {

        String res = "我是需要加密的明文";
        String mode = EncryptionConstant.ENCRYP_DES;
        Map<String, String> keyPair = null;
        try {
            //keyPair = RsaEncryptionUtils.genKeyPair();
        }catch (Exception e){

        }

        //String publicKey = keyPair.get("publicKeyStr");
        //String privateKey = keyPair.get("privateKeyStr");
        String key = "123456852";
        String trans = EncryptionConstant.DES_ECB_PKCS5PADDING;
        String iv = "12345678";
        String encryptStr = encrypt(mode, res,key,trans,iv);

        System.out.println("加密前："+res);
        System.out.println("加密方式:"+mode);
        System.out.println("加密后："+encryptStr);

        //解密
        String decryptStr =decrypt(mode,encryptStr,key,trans,iv);
        System.out.println("解密后："+decryptStr);
    }*/

}
