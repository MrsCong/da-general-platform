package com.dgp.elasticsearch.result;

import com.google.gson.JsonObject;

public class EsSearchByIdResult extends EsQueryBaseResult {

    private String _index;
    private String _type;
    private String _id;
    private String _version;
    private Boolean found;
    private JsonObject _source;

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_version() {
        return _version;
    }

    public void set_version(String _version) {
        this._version = _version;
    }


    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public JsonObject get_source() {
        return _source;
    }

    public void set_source(JsonObject _source) {
        this._source = _source;
    }

}
