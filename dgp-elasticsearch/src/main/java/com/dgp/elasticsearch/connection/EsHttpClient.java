package com.dgp.elasticsearch.connection;

public interface EsHttpClient {


    public String executePostJson(String dsl, String path);

    public String executeGet(String path);

    public String customUrl(String path);

    public String executeDelete(String path);

}
