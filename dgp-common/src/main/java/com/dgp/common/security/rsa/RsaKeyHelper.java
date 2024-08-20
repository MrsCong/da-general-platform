package com.dgp.common.security.rsa;


import com.dgp.common.security.EncryptionConstant;
import com.dgp.common.utils.Base64Util;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LQL
 * @author Liiushengping
 * @date 2019年10月5日
 * @description RSA公、私钥生成工具
 */
public class RsaKeyHelper {

	public static final String KEY_ALGORITHM = "RSA";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";
	public static final int KEY_SIZE = 2048;

	/**
	 * 获取公钥
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public PublicKey getPublicKey(String filename) throws Exception {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
		DataInputStream dis = new DataInputStream(resourceAsStream);
		byte[] keyBytes = new byte[resourceAsStream.available()];
		dis.readFully(keyBytes);
		dis.close();
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	/**
	 * 获取密钥
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public PrivateKey getPrivateKey(String filename) throws Exception {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
		DataInputStream dis = new DataInputStream(resourceAsStream);
		byte[] keyBytes = new byte[resourceAsStream.available()];
		dis.readFully(keyBytes);
		dis.close();
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	/**
	 * 获取公钥
	 *
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public PublicKey getPublicKey(byte[] publicKey) throws Exception {
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	/**
	 * 获取密钥
	 *
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public PrivateKey getPrivateKey(byte[] privateKey) throws Exception {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	/**
	 * 生存rsa公钥和密钥
	 *
	 * @param publicKeyFilename
	 * @param privateKeyFilename
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public void generateKey(String publicKeyFilename, String privateKeyFilename, String password)
			throws IOException, NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		FileOutputStream fos = new FileOutputStream(publicKeyFilename);
		fos.write(publicKeyBytes);
		fos.close();
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		fos = new FileOutputStream(privateKeyFilename);
		fos.write(privateKeyBytes);
		fos.close();
	}

	/**
	 * 生存rsa公钥
	 *
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] generatePublicKey(String password) throws IOException, NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		return keyPair.getPublic().getEncoded();
	}

	
	public static RSAPublicKey generateRSAPublicKey(String password) throws IOException, NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		return (RSAPublicKey) keyPair.getPublic();
	}

	public static RSAPrivateKey generateRSAPrivateKey(String password) throws IOException, NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		return (RSAPrivateKey) keyPair.getPrivate();
	}

	/**
	 * 
	 * 
	 * @author:LQL
	 * @date:2019年10月31日
	 * @return: String
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @description:返回公钥字符串
	 */
	public static String generatePublicKeyStr(String password) throws IOException, NoSuchAlgorithmException {

		return encode(generatePublicKey(password));

	}

	/**
	 * 生存rsa密钥
	 * 
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] generatePrivateKey(String password) throws IOException, NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		return keyPair.getPrivate().getEncoded();
	}

	/**
	 * 
	 * @author:LQL
	 * @date:2019年10月31日
	 * @return: String
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @description:返回私钥字符串
	 */
	public static String generatePrivateKeyStr(String password) throws IOException, NoSuchAlgorithmException {

		return encode(generatePrivateKey(password));

	}

	/**
	 * 
	 * @author:LQL
	 * @date:2019年10月31日
	 * @return: Map<String,byte[]>
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @description:返回byte rsa密钥对
	 */
	public static Map<String, byte[]> generateKey(String password) throws IOException, NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		map.put(EncryptionConstant.PUBLIC_KEY, publicKeyBytes);
		map.put(EncryptionConstant.PRIVATE_KEY, privateKeyBytes);
		return map;
	}

	/**
	 * 
	 * @author:LQL
	 * @date:2019年10月31日
	 * @return: Map<String,String>
	 * @param password
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @description:返回字符串密钥对
	 */
	public static Map<String, String> generateKeyStr(String password) throws IOException, NoSuchAlgorithmException {

		Map<String, String> result = new HashMap<String, String>();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom secureRandom = new SecureRandom(password.getBytes());
		keyPairGenerator.initialize(KEY_SIZE, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		result.put(EncryptionConstant.PUBLIC_KEY, encode(publicKeyBytes));
		result.put(EncryptionConstant.PRIVATE_KEY, encode(privateKeyBytes));
		return result;
	}

	public static String encode(byte[] b) {

		return Base64Util.encode(b);

	}

	public final byte[] toBytes(String s) throws IOException {
		return Base64Util.decodeToByte(s);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		RsaKeyHelper rsaHelper = new RsaKeyHelper();
		try {
			System.out.println("private key=" + rsaHelper.generatePrivateKey("123"));
			System.out.println("public key=" + rsaHelper.generatePublicKey("123"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
