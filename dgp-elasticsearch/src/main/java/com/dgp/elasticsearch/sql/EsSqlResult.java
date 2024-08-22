package com.dgp.elasticsearch.sql;

import com.dgp.elasticsearch.result.EsQueryBaseResult;

import java.util.ArrayList;
import java.util.List;

public class EsSqlResult extends EsQueryBaseResult {

    private List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
    private List<Object[]> rows = new ArrayList<Object[]>();


    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public List<Object[]> getRows() {
        return rows;
    }

    public void setRows(List<Object[]> rows) {
        this.rows = rows;
    }


}
