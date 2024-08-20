package com.dgp.common.security;

public class EncryptionConstant {
	/**
	 * 公钥
	 */
	public static final String PUBLIC_KEY="publicKey";
	/**
	 * 私钥
	 */
	public static final String PRIVATE_KEY="privateKey";

	/**
	 * 算法名称
	 */
	public static final String ENCRYP_RSA="RSA";
	public static final String ENCRYP_DES="DES";
	public static final String ENCRYP_AES="AES";
	public static final String ENCRYP_MD5="MD5";
	public static final String ENCRYP_SHA1 = "SHA1";
;

	/**
	 * transformations
	 * --作为cipher.getInstance参数
	 * 例如：cipher.getInstance(AES/CBC/NoPadding)
	 * 算法/模式/填充模式
	 * AES/CBC/NoPadding (128)
	 * AES/CBC/PKCS5Padding (128)
	 * AES/ECB/NoPadding (128)
	 * AES/ECB/PKCS5Padding (128)
	 * DES/CBC/NoPadding (56)
	 * DES/CBC/PKCS5Padding (56)
	 * DES/ECB/NoPadding (56)
	 * DES/ECB/PKCS5Padding (56)
	 * DESede/CBC/NoPadding (168)
	 * DESede/CBC/PKCS5Padding (168)
	 * DESede/ECB/NoPadding (168)
	 * DESede/ECB/PKCS5Padding (168)
	 * RSA/ECB/PKCS1Padding (1024, 2048)
	 * RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
	 * RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
	 *
	 * 注意：
	 * NoPadding: 加密内容不足8位用0补足8位, Cipher类不提供补位功能，需自己实现代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
	 * PKCS5Padding: 加密内容不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
	 */
	public static final String AES_CBC_NOPADDING = "AES/CBC/NoPadding";//不建议使用
	public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
	public static final String AES_ECB_NOPADDING = "AES/ECB/NoPadding";//不建议使用
	public static final String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
	public static final String DES_CBC_NOPADDING = "DES/CBC/NoPadding";//不建议使用
	public static final String DES_CBC_PKCS5PADDING = "DES/CBC/PKCS5Padding";
	public static final String DES_ECB_NOPADDING = "DES/ECB/NoPadding";//不建议使用
	public static final String DES_ECB_PKCS5PADDING = "DES/ECB/PKCS5Padding";
	public static final String DESede_CBC_NOPADDING = "DESede/CBC/NoPadding";
	public static final String DESede_CBC_PKCS5PADDING = "DESede/CBC/PKCS5Padding";
	public static final String DESede_ECB_NOPADDING = "DESede/ECB/NoPadding";
	public static final String DESede_ECB_PKCS5PADDING = "DESede/ECB/PKCS5Padding";
	public static final String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";
	public static final String RSA_ECB_OAEPWithSHA1ANDMGF1PADDING = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
	public static final String RSA_ECB_OAEPWithSHA256ANDMGF1PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";



	/**
	 * 模式
	 */
	public static final String MODE_ECB="ECB";//电子密码本
	public static final String MODE_CBC="CBC";//密码分组链接--需要初始化向量
	public static final String MODE_CFB="CFB";//密文反馈--需要初始化向量
	public static final String MODE_OFB="OFB";//输出反馈--需要初始化向量
	public static final String MODE_CTR="CTR";//计数器模式
	public static final String MODE_PCBC="PCBC";//填充密码分组链接

	/**
	 * 填充模式
	 */
	public static final String MODE_NOPADDING="NoPadding";
	public static final String MODE_PKCS5PADDING="PKCS5Padding";
	public static final String MODE_PKCS7PADDING="PKCS7Padding";
	public static final String MODE_PKCS1PADDING="PKCS1Padding";
	public static final String MODE_ISO10126PADDING="ISO10126Padding";
	public static final String MODE_ISO7816PADDING="ISO7816-4Padding";
	public static final String MODE_ZEROBYTEPADDING="ZeroBytePadding";
	public static final String MODE_X923PADDING="X923Padding";
	public static final String MODE_TBCPPADDING="TBCPadding";


	

}
