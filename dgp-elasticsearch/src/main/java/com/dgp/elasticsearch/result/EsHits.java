package com.dgp.elasticsearch.result;

import java.util.List;

public class EsHits {

	private float max_score;
	private EsTotal total;
	private List<EsHitsObject> hits;

	public float getMax_score() {
		return max_score;
	}

	public void setMax_score(float max_score) {
		this.max_score = max_score;
	}

	public EsTotal getTotal() {
		return total;
	}

	public void setTotal(EsTotal total) {
		this.total = total;
	}

	public List<EsHitsObject> getHits() {
		return hits;
	}

	public void setHits(List<EsHitsObject> hits) {
		this.hits = hits;
	}

}
