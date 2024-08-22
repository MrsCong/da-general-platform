package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.result.EsOperateByQueryResult;
import com.dgp.elasticsearch.sql.SqlDesc;

public class EsOperateByQueryResultHandler extends BaseResultHandler {

    @Override
    public Object handler(String result, SqlDesc sqlDesc) {
        try {
            EsOperateByQueryResult queryResult = GsonUtil.str2Object(result, EsOperateByQueryResult.class);
            if (queryResult.getStatus() > 0) {
                throw new ESQueryException(result);
            }
            return queryResult.getDeleted() > queryResult.getUpdated() ? queryResult.getDeleted()
                    : queryResult.getUpdated();
        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }

    }

}
