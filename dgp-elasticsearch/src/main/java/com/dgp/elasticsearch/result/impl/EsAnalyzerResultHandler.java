package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.result.EsAnalyzerResult;
import com.dgp.elasticsearch.sql.SqlDesc;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EsAnalyzerResultHandler extends BaseResultHandler {

	@Override
	public Object handler(String result, SqlDesc sqlDesc) {

		try {
			EsAnalyzerResult queryResult = GsonUtil.str2Object(result, EsAnalyzerResult.class);
			List<Object> resultList = new ArrayList<>();
			for (JsonObject json : queryResult.getTokens()) {
				Object obj = GsonUtil.str2Object(GsonUtil.obj2String(json), sqlDesc.getResultType());
				resultList.add(obj);
			}
			return resultList;
		} catch (Exception e) {
			log.error("es analyzer error result {},exception {}", result, e);
			throw new ESQueryException(result, e);
		}

	}

}
