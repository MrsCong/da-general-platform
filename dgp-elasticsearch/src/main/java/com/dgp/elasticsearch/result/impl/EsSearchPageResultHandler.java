package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.DateUtil;
import com.dgp.common.utils.GsonUtil;
import com.dgp.core.http.response.EsPageResponse;
import com.dgp.elasticsearch.common.EsOperateConstant;
import com.dgp.elasticsearch.result.EsHits;
import com.dgp.elasticsearch.result.EsHitsObject;
import com.dgp.elasticsearch.result.EsQueryResult;
import com.dgp.elasticsearch.sql.SqlDesc;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class EsSearchPageResultHandler extends BaseResultHandler {

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
            return EsPageResponse.builder()
                    .records(resultList)
                    .total(hits.getTotal().getValue()).timestamp(DateUtil.getDate_ymdhms())
                    .build();
        } catch (Exception e) {
            throw new ESQueryException(result, e);
        }

    }


}
