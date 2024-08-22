package com.dgp.elasticsearch.result;

import lombok.Data;

@Data
public class EsShards {

    private Integer total;

    private Integer successful;

    private Integer skipped;

    private Integer failed;

}
