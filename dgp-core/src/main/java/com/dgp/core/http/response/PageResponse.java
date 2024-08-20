package com.dgp.core.http.response;

import com.dgp.common.code.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("分页请求响应对象")
public class PageResponse<T> extends BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应数据", required = true)
    TableData<T> data;

    public PageResponse() {
        this.data = new TableData<T>(0L, new ArrayList<>());
    }

    public PageResponse(List<T> records, long total) {
        this.data = new TableData<T>(total, records);
    }

    public PageResponse(long total, long page, long limit, List<T> records) {
        this.data = new TableData<T>(total, page, limit, records);
    }

    @Override
    public PageResponse<T> code(StatusCode code) {
        this.setCode(code.getCode());
        this.setMessage(code.getMessage());
        return this;
    }

    @Override
    public PageResponse<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public PageResponse<T> total(int total) {
        data.setTotal(total);
        return this;
    }

    public PageResponse<T> current(int page) {
        data.setCurrent(page);
        return this;
    }

    public PageResponse<T> size(int limit) {
        data.setSize(limit);
        return this;
    }

    public PageResponse<T> totalPage(int totalPage) {
        data.setTotalPage(totalPage);
        return this;
    }

    public PageResponse<T> records(List<T> records) {
        data.setRecords(records);
        return this;
    }

    @Data
    public static class TableData<T> {

        public TableData() {
        }

        @ApiModelProperty(value = "数据集", required = true)
        private List<T> records = new ArrayList<>();
        @ApiModelProperty(value = "当前页数", required = true)
        private long current;
        @ApiModelProperty(value = "每页条数", required = true)
        private long size = 20;
        @ApiModelProperty(value = "总数", required = true)
        private long total;
        @ApiModelProperty(value = "总页数", required = true)
        private long totalPage;

        public TableData(Long total, List<T> records) {
            this.total = total;
            this.records = records;
        }

        public TableData(long total, long current, long size, List<T> records) {
            this.records = records;
            this.current = current;
            this.size = size;
            this.total = total;
        }

        public long getTotalPage() {
            totalPage = 0;
            if (size > 0) {
                if (total % size == 0) {
                    totalPage = total / size;
                } else {
                    totalPage = total / size + 1;
                }
            }
            return totalPage;

        }

    }

}
