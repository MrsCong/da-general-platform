package com.dgp.common.security.des;

import com.dgp.common.utils.Base64Util;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;

@Slf4j
public class DesEncryptUtils {
	/**
	 * 加密/解密算法-工作模式-填充模式
	 */
	private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
	private static final String IV_PARAM = "abcdefgh";//初始化向量参数，AES 为16bytes. DES 为8bytes.


	private static byte[] desEncrypt(byte[] plainText,String desKey,String cipherParam,byte[] ivByte) throws Exception {
		byte rawKeyData[] = desKey.getBytes();
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(cipherParam);
		if("CBC/CFB/OFB".contains(cipherParam.substring(4,7))){//当模式为CBC/CFB/OFB时可用iv
			cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(ivByte));
		}else{
			cipher.init(Cipher.ENCRYPT_MODE, key);
		}
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	private static byte[] desDecrypt(byte[] encryptText,String desKey,String cipherParam,byte[] ivByte) throws Exception {
		byte rawKeyData[] = desKey.getBytes();
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(cipherParam);
		if("CBC/CFB/OFB".contains(cipherParam.substring(4,7))){//当模式为CBC/CFB/OFB时可用iv
			cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(ivByte));
		}else{
			cipher.init(Cipher.DECRYPT_MODE, key);
		}
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	public static String encrypt(String input,String desKey, String transformation,String iv) {
		try {
			String encode = base64Encode(desEncrypt(input.getBytes(), desKey, transformation, iv.getBytes()));
			return encode;
		}catch(Exception exception){
			log.error("des加密失敗:{}",exception);
		}
		return null;
	}

	public static String encrypt(String input,String desKey) {

		try {
			String encode = base64Encode(desEncrypt(input.getBytes(), desKey, CIPHER_ALGORITHM, IV_PARAM.getBytes()));
			return encode;
		}catch(Exception exception){
			log.error("des加密失敗:{}",exception);
		}
		return null;
	}

	public static String decrypt(String input,String desKey) {
		try {
			byte[] result = base64Decode(input);
			String encode = new String(desDecrypt(result, desKey, CIPHER_ALGORITHM, IV_PARAM.getBytes()), "utf-8");
			return encode;
		}catch(Exception exception){
			log.error("des解密失敗:{}",exception);
		}
		return null;
	}

	public static String decrypt(String input,String desKey, String transformation,String iv) {


		try {
			byte[] result = base64Decode(input);
			String encode = new String(desDecrypt(result, desKey, transformation,iv.getBytes()), "utf-8");
			return encode;
		}catch(Exception exception){
			log.error("des解密失敗:{}",exception);
		}
		return null;
	}

	private static String base64Encode(byte[] s) {
		if (s == null) {
			return null;
		}
		return Base64Util.encode(s);
	}

	private static byte[] base64Decode(String s) throws IOException {
		if (s == null) {
			return null;
		}
		byte[] b = Base64Util.decodeFast(s);
		return b;
	}



}
