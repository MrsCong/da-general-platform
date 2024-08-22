package com.dgp.elasticsearch.result;

public class EsOperateByQueryResult extends EsQueryBaseResult {

    private int total;
    private int deleted;
    private int updated;
    private int batches;
    private int version_conflicts;
    private int noops;
    private Object[] failures;
    private int throttled_millis;
    private float requests_per_second;
    private int throttled_until_millis;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getBatches() {
        return batches;
    }

    public void setBatches(int batches) {
        this.batches = batches;
    }

    public int getVersion_conflicts() {
        return version_conflicts;
    }

    public void setVersion_conflicts(int version_conflicts) {
        this.version_conflicts = version_conflicts;
    }

    public int getNoops() {
        return noops;
    }

    public void setNoops(int noops) {
        this.noops = noops;
    }

    public Object[] getFailures() {
        return failures;
    }

    public void setFailures(Object[] failures) {
        this.failures = failures;
    }

    public int getThrottled_millis() {
        return throttled_millis;
    }

    public void setThrottled_millis(int throttled_millis) {
        this.throttled_millis = throttled_millis;
    }

    public float getRequests_per_second() {
        return requests_per_second;
    }

    public void setRequests_per_second(float requests_per_second) {
        this.requests_per_second = requests_per_second;
    }

    public int getThrottled_until_millis() {
        return throttled_until_millis;
    }

    public void setThrottled_until_millis(int throttled_until_millis) {
        this.throttled_until_millis = throttled_until_millis;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }


}
