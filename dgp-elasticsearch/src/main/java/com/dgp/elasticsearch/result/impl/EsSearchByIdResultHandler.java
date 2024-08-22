package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.result.EsSearchByIdResult;
import com.dgp.elasticsearch.sql.SqlDesc;

public class EsSearchByIdResultHandler extends BaseResultHandler {

    @Override
    public Object handler(String result, SqlDesc sqlDesc) {

        try {

            EsSearchByIdResult queryResult = GsonUtil.str2Object(result, EsSearchByIdResult.class);

            if (queryResult.isFound()) {
                Object obj = GsonUtil.obj2Object(queryResult.get_source(),
                        sqlDesc.getResultType());
                return obj;
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }

    }

}
