package com.dgp.common.cache;

public interface Cache {

    public void put(String key, String field, String offset);

    public String get(String key, String field);

}
