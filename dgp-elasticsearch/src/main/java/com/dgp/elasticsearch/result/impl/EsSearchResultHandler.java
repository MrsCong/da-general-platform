package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.common.EsOperateConstant;
import com.dgp.elasticsearch.result.EsHits;
import com.dgp.elasticsearch.result.EsHitsObject;
import com.dgp.elasticsearch.result.EsQueryResult;
import com.dgp.elasticsearch.sql.SqlDesc;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.ArrayList;
import java.util.List;

public class EsSearchResultHandler extends BaseResultHandler {

    @Override
    public Object handler(String result, SqlDesc sqlDesc) {

        try {

            EsQueryResult queryResult = handlerToEsResult(result);
            EsHits hits = queryResult.getHits();
            List<Object> resultList = new ArrayList<Object>();

            if (CollectionUtils.isNotEmpty(hits.getHits())) {
                for (EsHitsObject hit : hits.getHits()) {
                    hit.get_source().add(EsOperateConstant.HIGHLIGHT_FIELD, hit.getHighlight());
                    Object obj = GsonUtil.str2Object(GsonUtil.obj2String(hit.get_source()), sqlDesc.getResultType());
                    resultList.add(obj);
                }
            }
            if (sqlDesc.isReturnsMany() || sqlDesc.isReturnsVoid()) {
                return resultList;
            } else {
                if (resultList.size() == 1) {
                    return resultList.get(0);
                } else if (resultList.size() > 1) {
                    throw new TooManyResultsException(
                            "Expected one result (or null) to be returned by selectOne(), but found: "
                                    + resultList.size());
                } else {
                    return null;
                }
            }

        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }

    }

}
