/**   
* @Title: HitsObject.java 
* @Package com.guokang.ms.elasticsearch.result 
* @Description: TODO(用一句话描述该文件做什么) 
* @author LQL  
* @date 2021年10月22日     
*/
package com.dgp.elasticsearch.result;

import com.google.gson.JsonObject;

public class EsHitsObject {

	private String _index;
	private String _type;
	private String _id;
	private Float _score;
	private JsonObject _source;
	private JsonObject highlight;

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

	public Float get_score() {
		return _score;
	}

	public void set_score(Float _score) {
		this._score = _score;
	}

	public JsonObject get_source() {
		return _source;
	}

	public void set_source(JsonObject _source) {
		this._source = _source;
	}

	public JsonObject getHighlight() {
		return highlight;
	}

	public void setHighlight(JsonObject highlight) {
		this.highlight = highlight;
	}
	
	

}
