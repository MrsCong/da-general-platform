package com.dgp.common.context;

import com.dgp.common.security.EncryptionConstant;
import com.dgp.common.security.rsa.RsaEncryptionUtils;
import com.dgp.common.utils.GsonUtil;

public abstract class BaseTokenUtil implements TokenUtil {

    /**
     * token加密
     *
     * @param obj    待加密对象
     * @param priKey 私钥
     * @return token
     */
    public String getToken(Object obj, String priKey) {
        String token = RsaEncryptionUtils.encryptByPri(GsonUtil.obj2String(obj), priKey, EncryptionConstant.RSA_ECB_PKCS1PADDING);
        return token;
    }

    /**
     * 公钥解出token
     *
     * @param token dto
     * @return token
     */
    public String decrypt(String token, String publicKey) {
        return RsaEncryptionUtils.decryptByPub(token, publicKey, EncryptionConstant.RSA_ECB_PKCS1PADDING);
    }

    /**
     * 从token中获取用户对象
     *
     * @param token token
     * @return tokenInfo
     */
    public TokenInfo getUserTokenInfo(String token, String publicKey) {
        // 公钥解密
        String decrypt = decrypt(token, publicKey);
        return GsonUtil.str2Object(decrypt, TokenInfo.class);
    }

    /**
     * 从token中获取服务对象
     *
     * @param token token
     * @return ClientInfo
     */
    public ClientInfo getClientTokenInfo(String token, String publicKey) {
        // 公钥解密
        String decrypt = decrypt(token, publicKey);
        return GsonUtil.str2Object(decrypt, ClientInfo.class);
    }

}
