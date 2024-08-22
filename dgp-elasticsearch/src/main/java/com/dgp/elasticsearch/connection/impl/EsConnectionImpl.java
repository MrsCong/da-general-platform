package com.dgp.elasticsearch.connection.impl;


import com.dgp.elasticsearch.connection.EsHttpClient;
import com.dgp.elasticsearch.result.EsResultHandler;
import com.dgp.elasticsearch.result.impl.ResultHandlerFactory;
import com.dgp.elasticsearch.sql.SqlDesc;
import com.dgp.elasticsearch.support.DSLConfiguration;

public class EsConnectionImpl extends EsBaseConnection {

    public EsConnectionImpl(EsHttpClient httpClient, DSLConfiguration dslConfiguration) {

        super(httpClient, dslConfiguration);

    }

    @Override
    public Object doExecute(SqlDesc sqlDesc) {
        String resultStr = null;
        switch (sqlDesc.getActionEnum()) {
            case INSERT_BY_ID:
            case UPDATE_BY_ID:
                resultStr = executePost(sqlDesc.getDsl(), sqlDesc.getRestFulPath());
                break;
            case DELETE_BY_ID:
                resultStr = executeDelete(sqlDesc.getRestFulPath());
                break;
            case SEARCH_BY_ID:
                resultStr = executeGet(sqlDesc.getRestFulPath());
                break;
            case SEARCH_BY_SQL:
                resultStr = executePost(sqlDesc.getDsl(), sqlDesc.getRestSqlPath());
                break;
            default:
                resultStr = executePost(sqlDesc.getDsl(), sqlDesc.getRequestPath());
                break;
        }

        EsResultHandler resultHandler = ResultHandlerFactory.createEsResultHandler(sqlDesc.getActionEnum());
        return resultHandler.handler(resultStr, sqlDesc);
    }

    @Override
    public String executePost(String dsl, String path) {

        return httpClient.executePostJson(dsl, path);
    }

    @Override
    public String executeGet(String path) {
        return httpClient.executeGet(path);
    }

    @Override
    public String executeDelete(String path) {
        return httpClient.executeDelete(path);
    }

}
