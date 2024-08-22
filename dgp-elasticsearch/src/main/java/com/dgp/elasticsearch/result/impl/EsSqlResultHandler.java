package com.dgp.elasticsearch.result.impl;

import com.dgp.common.exception.ESQueryException;
import com.dgp.common.utils.GsonUtil;
import com.dgp.elasticsearch.sql.ColumnInfo;
import com.dgp.elasticsearch.sql.EsSqlResult;
import com.dgp.elasticsearch.sql.SqlDesc;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsSqlResultHandler extends BaseResultHandler {

	@Override
	public Object handler(String result, SqlDesc sqlDesc) {

		try {
			EsSqlResult queryResult = GsonUtil.str2Object(result, EsSqlResult.class);
			if (queryResult.getStatus() > 0) {
				throw new ESQueryException(result);
			}
			List<Object> resultList = new ArrayList<Object>();
			List<Object[]> data = queryResult.getRows();
			List<ColumnInfo> columList = queryResult.getColumns();
			if (data.size() == 0) {
				return resultList;
			}
			int columSize = columList.size();
			for (Object[] obj : data) {

				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columSize; i++) {
					ColumnInfo info = columList.get(i);
					map.put(info.getName(), obj[i]);
				}
				Object objResult = GsonUtil.str2Object(GsonUtil.obj2String(map), sqlDesc.getResultType());
				resultList.add(objResult);
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
