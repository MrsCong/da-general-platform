package com.dgp.test.typehandler;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListStringToStringTypeHandler implements TypeHandler<List<String>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<String> list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, StringUtils.join(list, ","));
    }

    @Override
    public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        return Arrays.asList(StringUtils.split(result, ","));
    }

    @Override
    public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        return Arrays.asList(StringUtils.split(result, ","));
    }

    @Override
    public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        return Arrays.asList(StringUtils.split(result, ","));
    }
}
