package com.dgp.elasticsearch.enums;

import com.dgp.common.code.BaseStatusCode;
import com.dgp.common.exception.NormalException;

public enum EsActionEnum {

    SEARCH("SEARCH", "查询", "_search"),

    SEARCH_PAGE("SEARCH_PAGE", "分页查询", "_search"),

    UPDATE_BY_QUERY("UPDATE_BY_QUERY", "查询更新", "_update_by_query"),

    DELETE_BY_QUERY("DELETE_BY_QUERY", "查询删除", "_delete_by_query"),

    BULK_INSERT("BULK_INSERT", "批量插入", "_bulk"),

    BULK_UPDATE("BULK_UPDATE", "批量更新", "_bulk"),

    INSERT_BY_ID("INSERT_BY_ID", "添加ById", "_doc"),

    UPDATE_BY_ID("UPDATE_BY_ID", "更新ById", "_update"),

    SEARCH_BY_ID("SEARCH_BY_ID", "查询ById", "_doc"),

    SEARCH_BY_SQL("SEARCH_BY_SQL", "查询BySql", "_sql"),

    DELETE_BY_ID("DELETE_BY_ID", "删除ById", "_doc"),

    ANALYZE_TEXT("ANALYZE_TEXT", "文本分词", "_analyze");

    private String code;
    private String text;
    private String value;

    EsActionEnum(String code, String text, String value) {
        this.code = code;
        this.text = text;
        this.value = value;
    }

    private static final EsActionEnum[] enums = EsActionEnum.values();

    public static EsActionEnum getEsActionEnumByCode(String code) {

        for (EsActionEnum enm : enums) {
            if (enm.getCode().equals(code)) {
                return enm;
            }
        }

        throw new NormalException(BaseStatusCode.ES_BAD_ACTION);

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
