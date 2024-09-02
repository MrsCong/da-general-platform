package com.dgp.common.context;

import lombok.Data;

import java.util.Map;

@Data
public class TokenInfo {

    private Long userId;

    private String nickName;

    private String name;

    private Long companyId;

    private Long staffId;

    private String imUserId;

    private Integer userType;

    private Integer accountType;

    private Integer grantType;

    private long expireTime;

    private String channel;

    private String appCode;

    private Map<String, String> otherInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TokenInfo jwtInfo = (TokenInfo) o;

        if (userId != null ? !userId.equals(jwtInfo.userId) : jwtInfo.userId != null) {
            return false;
        }
        return userId != null ? userId.equals(jwtInfo.userId) : jwtInfo.userId == null;

    }

    @Override
    public int hashCode() {
        int result = nickName != null ? nickName.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
