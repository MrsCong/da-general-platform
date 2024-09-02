package com.dgp.kafka.dto;

import lombok.Data;

@Data
public class BaseEventDto {

    private String userToken;

    private String clientToken;

    private String data;

}
