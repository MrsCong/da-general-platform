package com.dgp.elasticsearch.connection.impl;

import com.dgp.elasticsearch.connection.EsConnection;
import com.dgp.elasticsearch.connection.EsHttpClient;
import com.dgp.elasticsearch.sql.SqlDesc;
import com.dgp.elasticsearch.support.DSLConfiguration;

public abstract class EsBaseConnection implements EsConnection {

    protected EsHttpClient httpClient;

    protected DSLConfiguration dslConfiguration;

    public EsBaseConnection(EsHttpClient httpClient, DSLConfiguration dslConfiguration) {

        this.httpClient = httpClient;
        this.dslConfiguration = dslConfiguration;

    }

    @Override
    public Object execute(SqlDesc sqlDesc) {

        return doExecute(sqlDesc);

    }


    public abstract Object doExecute(SqlDesc sqlDesc);


}
