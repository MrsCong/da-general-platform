package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.result.EsBulkResult;
import com.dgp.elasticsearch.sql.SqlDesc;

public class EsBulkResultHandler extends BaseResultHandler {

    @Override
    public Object handler(String result, SqlDesc sqlDesc) {
        try {

            EsBulkResult queryResult = GsonUtil.str2Object(result, EsBulkResult.class);

            return queryResult.isErrors();

        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }
    }

}
