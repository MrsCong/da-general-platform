package com.dgp.elasticsearch.result;

import com.dgp.elasticsearch.sql.SqlDesc;

public interface EsResultHandler {

    public Object handler(String result, SqlDesc sqlDesc);

    public EsResult handlerToEsResult(String result);

}
