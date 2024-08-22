package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.common.EsOperateConstant;
import com.dgp.elasticsearch.result.EsOperateByIdResult;
import com.dgp.elasticsearch.sql.SqlDesc;

public class EsUpdateByIdResultHandler extends BaseResultHandler {

    @Override
    public Object handler(String result, SqlDesc sqlDesc) {

        try {

            EsOperateByIdResult queryResult = GsonUtil.str2Object(result, EsOperateByIdResult.class);

            if (EsOperateConstant.UPDATE.equals(queryResult.getResult())) {
                return queryResult.get_shards().getSuccessful();
            }
            return EsOperateConstant.ZERO;

        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }

    }

}
