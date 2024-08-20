package com.dgp.common.security.rsa;

import com.dgp.common.code.BaseStatusCode;
import com.dgp.common.exception.RsaException;
import com.dgp.common.security.EncryptionConstant;
import com.dgp.common.utils.Base64Util;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liushengping
 * @author Liaoqianlin
 * @author Huijiale
 * @since 1.0.0
 */
@Slf4j
public class RsaEncryptionUtils {

	// ------------------------------------------公钥加密、私钥解密

	/**
	 * 公钥加密
	 *
	 * @param content   待加密内容
	 * @param publicKey 公钥字符串
	 * @return 加密后字符串
	 */
	public static String encrypt(String content, String publicKey) {
		try {
			// base64编码的公钥
			byte[] decoded = Base64Util.decodeFast(publicKey);
			RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					.generatePublic(new X509EncodedKeySpec(decoded));
			// RSA加密
			Cipher cipher = Cipher.getInstance(EncryptionConstant.ENCRYP_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			String outStr = Base64Util.encode(cipher.doFinal(content.getBytes("UTF-8")));
			return outStr;
		} catch (Exception e) {
			log.error("RSA加密失败", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR, e);
		}
	}

	/**
	 * 公钥加密
	 *
	 * @param content        待加密内容
	 * @param publicKey      公钥字符串
	 * @param transformation 加密方式
	 * @return 加密后字符串
	 */
	public static String encrypt(String content, String publicKey, String transformation) {
		try {
			// base64编码的公钥
			byte[] decoded = Base64Util.decodeFast(publicKey);
			RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					.generatePublic(new X509EncodedKeySpec(decoded));
			// RSA加密
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			String outStr = Base64Util.encode(cipher.doFinal(content.getBytes("UTF-8")));
			return outStr;
		} catch (Exception e) {
			log.error("RSA加密失败{}", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR,e);
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param encryptText 待解密内容
	 * @param privateKey  私钥字符串
	 * @return 解密后字符串
	 */
	public static String decrypt(String encryptText, String privateKey) {

		try {
			// 64位解码加密后的字符串
			byte[] inputByte = Base64Util.decodeFast(encryptText);
			// base64编码的私钥
			byte[] decoded = Base64Util.decodeFast(privateKey);
			RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					.generatePrivate(new PKCS8EncodedKeySpec(decoded));
			// RSA解密
			Cipher cipher = Cipher.getInstance(EncryptionConstant.ENCRYP_RSA);
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			String outStr = new String(cipher.doFinal(inputByte));
			return outStr;
		} catch (Exception e) {
			log.error("RSA解密失败{}", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR,e);
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param encryptText    待解密内容
	 * @param privateKey     私钥字符串
	 * @param transformation 解密方式
	 * @return 解密后字符串
	 */
	public static String decrypt(String encryptText, String privateKey, String transformation) {

		try {
			// 64位解码加密后的字符串
			byte[] inputByte = Base64Util.decodeFast(encryptText);
			// base64编码的私钥
			byte[] decoded = Base64Util.decodeFast(privateKey);
			RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					.generatePrivate(new PKCS8EncodedKeySpec(decoded));
			// RSA解密
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			String outStr = new String(cipher.doFinal(inputByte));
			return outStr;
		} catch (Exception e) {
			log.error("RSA解密失败", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR,e);
		}
	}

	// ------------------------------------------私钥加密、公钥解密

	/**
	 * 私钥加密
	 *
	 * @param content    待加密内容
	 * @param privateKey 私钥字符串
	 * @return 加密后字符串
	 */
	public static String encryptByPri(String content, String privateKey) {
		try {
			// base64编码的私钥
			byte[] decoded = Base64Util.decodeFast(privateKey);
			RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					// 生成私钥
					.generatePrivate(new PKCS8EncodedKeySpec(decoded));
			// RSA私钥加密
			Cipher cipher = Cipher.getInstance(EncryptionConstant.ENCRYP_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			String outStr = Base64Util.encode(cipher.doFinal(content.getBytes("UTF-8")));
			return outStr;
		} catch (Exception e) {
			log.error("RSA私钥加密失败", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR, e);
		}
	}

	/**
	 * 私钥加密
	 *
	 * @param content        待加密内容
	 * @param privateKey      私钥字符串
	 * @param transformation 加密方式
	 * @return 加密后字符串
	 */
	public static String encryptByPri(String content, String privateKey, String transformation) {
		try {
			// base64编码的私钥
			byte[] decoded = Base64Util.decodeFast(privateKey);
			RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					// 生成私钥
					.generatePrivate(new PKCS8EncodedKeySpec(decoded));
			// RSA加密
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			String outStr = Base64Util.encode(cipher.doFinal(content.getBytes("UTF-8")));
			return outStr;
		} catch (Exception e) {
			log.error("RSA私钥加密失败", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR, e);
		}
	}

	/**
	 * 公钥解密
	 *
	 * @param encryptText 待解密内容
	 * @param publicKey   公钥字符串
	 * @return 解密后字符串
	 */
	public static String decryptByPub(String encryptText, String publicKey) {
		try {
			// 64位解码加密后的字符串
			byte[] inputByte = Base64Util.decodeFast(encryptText);
			// base64编码的公钥
			byte[] decoded = Base64Util.decodeFast(publicKey);
			RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					// 生成公钥
					.generatePublic(new X509EncodedKeySpec(decoded));
			// RSA公钥解密
			Cipher cipher = Cipher.getInstance(EncryptionConstant.ENCRYP_RSA);
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			String outStr = new String(cipher.doFinal(inputByte),"UTF-8");
			return outStr;
		} catch (Exception e) {
			log.error("RSA公钥解密失败", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR, e);
		}
	}

	/**
	 * 公钥解密
	 *
	 * @param encryptText    待解密内容
	 * @param publicKey     公钥字符串
	 * @param transformation 解密方式
	 * @return 解密后字符串
	 */
	public static String decryptByPub(String encryptText, String publicKey, String transformation) {
		try {
			// 64位解码加密后的字符串
			byte[] inputByte = Base64Util.decodeFast(encryptText);
			// base64编码的公钥
			byte[] decoded = Base64Util.decodeFast(publicKey);
			RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(EncryptionConstant.ENCRYP_RSA)
					// 生成公钥
					.generatePublic(new X509EncodedKeySpec(decoded));
			// RSA解密
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			String outStr = new String(cipher.doFinal(inputByte),"UTF-8");
			return outStr;
		} catch (Exception e) {
			log.error("RSA公钥解密失败", e);
			throw new RsaException(BaseStatusCode.RSA_ERROR, e);
		}
	}

	/**
	 * RSA：随机生成密钥对
	 *
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(EncryptionConstant.ENCRYP_RSA);

		// 初始化密钥对生成器，密钥大小为96-2048位
		keyPairGen.initialize(2048, new SecureRandom());

		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥

		// 得到公钥字符串
		String publicKeyStr = new String(Base64Util.encode(publicKey.getEncoded()));
		// 得到私钥字符串
		String privateKeyStr = new String(Base64Util.encode((privateKey.getEncoded())));

		System.out.println("随机生成的公钥为:" + publicKeyStr);
		System.out.println("随机生成的私钥为:" + privateKeyStr);
		HashMap<String, String> key = new HashMap();
		key.put("publicKeyStr", publicKeyStr);
		key.put("privateKeyStr", privateKeyStr);

		return key;
	}

}
