package com.dgp.common.security.sm;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;

public class SM2Utils {

    //生成随机秘钥对
    public static String prik = "";

    // 国密规范正式公钥
    public static String pubk = "";

    @SuppressWarnings("deprecation")
    public static void generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();

        prik = ByteUtils.byteToHex(privateKey.toByteArray());
        pubk = ByteUtils.byteToHex(publicKey.getEncoded());
        System.out.println("公钥: " + pubk);
        System.out.println("私钥: " + prik);
    }

    /**
     * SM2数据加密
     *
     * @param publicKey
     * @param data
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static String encrypt(String publicKey, String data) throws IOException {
        if (StringUtils.isEmpty(publicKey) || StringUtils.isEmpty(data)) {
            return null;
        }

        byte[] pubKey = ByteUtils.hexToByte(publicKey);
        if (pubKey == null || pubKey.length == 0) {
            return null;
        }

        byte[] result = data.getBytes();
        if (result == null || result.length == 0) {
            return null;
        }

        byte[] source = new byte[result.length];
        System.arraycopy(result, 0, source, 0, result.length);
        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(pubKey);
        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);

        //C1 C2 C3拼装成加密字串
        return ByteUtils.byteToHex(c1.getEncoded()) + ByteUtils.byteToHex(source) + ByteUtils.byteToHex(c3);
    }

    /**
     * SM2数据解密
     *
     * @param privateKey
     * @param encryptedData
     * @return
     * @throws IOException
     */
    public static String decrypt(String privateKey, String encryptedData) throws IOException {
        if (StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(encryptedData)) {
            return null;
        }

        byte[] priKey = ByteUtils.hexToByte(privateKey);
        if (priKey == null || priKey.length == 0) {
            return null;
        }

        byte[] result = ByteUtils.hexToByte(encryptedData);
        if (result == null || result.length == 0) {
            return null;
        }

        // 加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = ByteUtils.byteToHex(result);
        /**
         * 分解加密字串 （C1 = C1标志位2位 + C1实体部分128位 = 130） （C3 = C3实体部分64位 = 64） （C2 =
         * encryptedData.length * 2 - C1长度 - C2长度）
         */
        byte[] c1Bytes = ByteUtils.hexToByte(data.substring(0, 130));
        int c2Len = result.length - 97;
        byte[] c2 = ByteUtils.hexToByte(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = ByteUtils.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, priKey);

        // 通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);
        // 返回解密结果
        return ByteUtils.byteToString(c2);
    }

}

