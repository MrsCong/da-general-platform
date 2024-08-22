package com.dgp.elasticsearch.result.impl;


import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.result.EsQueryResult;
import com.dgp.elasticsearch.result.EsResultHandler;

public abstract class BaseResultHandler implements EsResultHandler {


    @Override
    public EsQueryResult handlerToEsResult(String result) {

        try {

            EsQueryResult queryResult = GsonUtil.str2Object(result, EsQueryResult.class);
            return queryResult;
        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }
    }

}
