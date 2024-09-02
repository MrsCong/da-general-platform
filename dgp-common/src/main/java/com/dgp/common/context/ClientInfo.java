package com.dgp.common.context;

import lombok.Data;

@Data
public class ClientInfo {
    private Long id;
    private String name;
    private String clientId = "gk-default";
    private String clientKey = "gk-default";
    private long expireTime = 43200;
}
