package com.dgp.elasticsearch.connection;

import com.dgp.elasticsearch.sql.SqlDesc;

public interface EsConnection {

	public Object execute(SqlDesc sqlDesc);

	public String executePost(String dsl, String path);

	public String executeGet(String path);

	public String executeDelete(String path);

}
