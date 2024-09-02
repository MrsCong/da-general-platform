package com.dgp.common.context;

public interface TokenUtil {


    public TokenInfo getUserTokenInfo(String token);

    public ClientInfo getClientInfo(String token);

}
