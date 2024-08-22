package com.dgp.elasticsearch.result;

public class EsQueryBaseResult implements EsResult {

    private int took;
    private int _seq_no;
    private int _primary_term;
    private int status;
    private boolean timed_out;

    private EsShards _shards;

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public boolean isTimed_out() {
        return timed_out;
    }

    public void setTimed_out(boolean timed_out) {
        this.timed_out = timed_out;
    }

    public EsShards get_shards() {
        return _shards;
    }

    public void set_shards(EsShards _shards) {
        this._shards = _shards;
    }

    public int get_seq_no() {
        return _seq_no;
    }

    public void set_seq_no(int _seq_no) {
        this._seq_no = _seq_no;
    }

    public int get_primary_term() {
        return _primary_term;
    }

    public void set_primary_term(int _primary_term) {
        this._primary_term = _primary_term;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
