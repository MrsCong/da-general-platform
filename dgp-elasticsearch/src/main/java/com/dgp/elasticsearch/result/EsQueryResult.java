package com.dgp.elasticsearch.result;

public class EsQueryResult extends EsQueryBaseResult {

    private EsHits hits;

    public EsHits getHits() {
        return hits;
    }

    public void setHits(EsHits hits) {
        this.hits = hits;
    }

}
