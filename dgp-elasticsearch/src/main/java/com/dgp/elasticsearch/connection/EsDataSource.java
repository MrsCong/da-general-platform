package com.dgp.elasticsearch.connection;

import com.dgp.elasticsearch.support.DSLConfiguration;

public interface EsDataSource {

    public EsConnection getEsConnection();

    public DSLConfiguration getDslConfiguration();

}
