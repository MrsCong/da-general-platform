package com.dgp.elasticsearch.result;

public class EsBulkResult extends EsQueryBaseResult {

    private boolean errors;


    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

}
