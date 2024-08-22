package com.dgp.elasticsearch.result;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;


@Data
public class EsAnalyzerResult implements EsResult {

    List<JsonObject> tokens;


}
