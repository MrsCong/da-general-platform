package com.dgp.common.context;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

public class SessionContextHolder extends BaseThreadLocal {

    public static void setTokenInfo(TokenInfo tokenParam, String token) {
        set(SessionContextConstant.USER_TOKEN_INFO, tokenParam);
        set(SessionContextConstant.USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(SessionContextConstant.USER_TOKEN);
    }

    public static String getClientToken() {
        return getString(SessionContextConstant.CLIENT_TOKEN);
    }

    public static void setClientToken(String token) {
        set(SessionContextConstant.CLIENT_TOKEN, token);
    }

    public static TokenInfo getTokenInfo() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo;

    }

    public static String getChannel() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        if (ObjectUtil.isNull(tokenInfo)) {
            return null;
        }
        String channel = tokenInfo.getChannel();
        // 兼容之前的token没有渠道这个参数
        return StrUtil.isBlank(channel) ? SessionContextConstant.WECHAT_CHANNEL : channel;
    }

    public static Long getUserId() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getUserId();
    }

    public static String getAppCode() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getAppCode();
    }

    public static String getNickName() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getNickName();
    }

    public static String getName() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getName();
    }

    public static Long getCompanyId() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getCompanyId();
    }

    public static Long getStaffId() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getStaffId();
    }

    public static String getImUserId() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getImUserId();
    }

    public static Integer getUserType() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getUserType();
    }

    public static Integer getAccountType() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getAccountType();
    }

    public static Integer getGrantType() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getGrantType();
    }

    public static Long getExpireTime() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        return ObjectUtil.isNull(tokenInfo) ? null : tokenInfo.getExpireTime();
    }

    public static void remove() {
        TokenInfo tokenInfo = (TokenInfo) get(SessionContextConstant.USER_TOKEN_INFO);
        tokenInfo = null;
        threadLocal.remove();
    }

}
