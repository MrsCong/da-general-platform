package com.dgp.elasticsearch.result.impl;

import com.dgp.common.code.BaseStatusCode;
import com.dgp.common.exception.NormalException;
import com.dgp.elasticsearch.enums.EsActionEnum;
import com.dgp.elasticsearch.result.EsResultHandler;

public class ResultHandlerFactory {

    public static EsResultHandler createEsResultHandler(EsActionEnum commandType) {
        EsResultHandler esReultHandler = null;
        switch (commandType) {
            case SEARCH:
                esReultHandler = new EsSearchResultHandler();
                break;
            case SEARCH_PAGE:
                esReultHandler = new EsSearchPageResultHandler();
                break;
            case UPDATE_BY_QUERY:
            case DELETE_BY_QUERY:
                esReultHandler = new EsOperateByQueryResultHandler();
                break;
            case BULK_INSERT:
            case BULK_UPDATE:
                esReultHandler = new EsBulkResultHandler();
                break;
            case INSERT_BY_ID:
                esReultHandler = new EsInsertByIdResultHandler();
                break;
            case UPDATE_BY_ID:
                esReultHandler = new EsUpdateByIdResultHandler();
                break;
            case SEARCH_BY_ID:
                esReultHandler = new EsSearchByIdResultHandler();
                break;
            case DELETE_BY_ID:
                esReultHandler = new EsDeleteByIdResultHandler();
                break;
            case SEARCH_BY_SQL:
                esReultHandler = new EsSqlResultHandler();
                break;
            case ANALYZE_TEXT:
            	esReultHandler = new EsAnalyzerResultHandler();
            	break;

            default:
                throw new NormalException(BaseStatusCode.NOT_FOUND);
        }

        return esReultHandler;
    }

}
