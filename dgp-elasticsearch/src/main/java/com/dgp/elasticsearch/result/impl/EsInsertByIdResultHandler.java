package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.exception.NormalException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.common.EsOperateConstant;
import com.dgp.elasticsearch.result.EsOperateByIdResult;
import com.dgp.elasticsearch.sql.SqlDesc;

public class EsInsertByIdResultHandler extends BaseResultHandler {

    @Override
    public Object handler(String result, SqlDesc sqlDesc) {

        try {

            EsOperateByIdResult queryResult = GsonUtil.str2Object(result, EsOperateByIdResult.class);

            if (EsOperateConstant.CREATE.equals(queryResult.getResult())
                    || EsOperateConstant.UPDATE.equals(queryResult.getResult())) {
                return queryResult.get_shards().getSuccessful();
            }
            throw new NormalException(result);

        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }

    }

}
