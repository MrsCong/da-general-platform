package com.dgp.mybatis.typehandler;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({JSONObject.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonObjToStringTypeHandler implements TypeHandler<JSONObject> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, JSONObject jsonObject, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, jsonObject.toJSONString());
    }

    @Override
    public JSONObject getResult(ResultSet resultSet, String s) throws SQLException {
        if (resultSet.getString(s) != null) {
            return JSONObject.parseObject(resultSet.getString(s));
        }
        return null;
    }

    @Override
    public JSONObject getResult(ResultSet resultSet, int i) throws SQLException {
        if (resultSet.getString(i) != null) {
            return JSONObject.parseObject(resultSet.getString(i));
        }
        return null;
    }

    @Override
    public JSONObject getResult(CallableStatement callableStatement, int i) throws SQLException {
        if (callableStatement.getString(i) != null) {
            return JSONObject.parseObject(callableStatement.getString(i));
        }
        return null;
    }
}
