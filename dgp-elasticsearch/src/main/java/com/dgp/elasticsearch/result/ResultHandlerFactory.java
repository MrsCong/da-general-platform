package com.dgp.elasticsearch.result;

import com.dgp.elasticsearch.enums.CommandType;

public interface ResultHandlerFactory {

    EsResultHandler createEsResultHandler(CommandType commandType);

}
