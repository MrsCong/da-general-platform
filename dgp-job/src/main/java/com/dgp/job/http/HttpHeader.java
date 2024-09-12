package com.dgp.job.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpHeader implements Serializable {

    /**
     * 请求头名称
     */
    private String headerName;

    /**
     * 请求头值
     */
    private String headerValue;

}
