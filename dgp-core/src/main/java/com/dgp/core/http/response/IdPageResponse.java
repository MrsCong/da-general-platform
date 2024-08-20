package com.dgp.core.http.response;

import com.dgp.common.code.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@ApiModel("ID分页响应对象")
public class IdPageResponse<T> extends BaseResponse {

    @ApiModelProperty(value = "响应数据", required = true)
    TableData<T> data;

    public IdPageResponse() {
    }

    public IdPageResponse(List<T> records, int total) {
        this.data = new TableData<T>(total, records);
    }

    public IdPageResponse(Integer total, String pageId, Integer limit, List<T> records) {
        this.data = new TableData<T>(total, pageId, limit, records);
    }

    @Override
    public IdPageResponse<T> code(StatusCode code) {
        this.setCode(code.getCode());
        this.setMessage(code.getMessage());
        return this;
    }

    @Override
    public IdPageResponse<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public IdPageResponse<T> total(int total) {
        data.setTotal(total);
        return this;
    }

    public IdPageResponse<T> pageId(String pageId) {
        data.setPageId(pageId);
        return this;
    }

    public IdPageResponse<T> limit(int limit) {
        data.setLimit(limit);
        return this;
    }

    public IdPageResponse<T> totalPage(int totalPage) {
        data.setTotalPage(totalPage);
        return this;
    }

    public IdPageResponse<T> records(List<T> records) {
        data.setRecords(records);
        return this;
    }

    @Getter
    @Setter
    public class TableData<T> {

        @ApiModelProperty(value = "数据集", required = true)
        private List<T> records = new ArrayList<>();
        @ApiModelProperty(value = "分页ID", required = true)
        private String pageId;
        @ApiModelProperty(value = "每页条数", required = true)
        private Integer limit;
        @ApiModelProperty(value = "总数", required = true)
        private Integer total;
        @ApiModelProperty(value = "总页数", required = true)
        private Integer totalPage;

        public TableData(int total, List<T> records) {
            this.total = total;
            this.records = records;
        }

        public TableData(Integer total, String pageId, Integer limit, List<T> records) {
            this.records = records;
            this.pageId = pageId;
            this.limit = limit;
            this.total = total;
        }

        public Integer getTotalPage(Integer total, Integer limit) {
            totalPage = 1;
            if (Objects.nonNull(total) && Objects.nonNull(limit)) {
                if (total % limit == 0) {
                    totalPage = total / limit;
                } else {
                    totalPage = total / limit + 1;
                }
            }
            return totalPage;

        }

    }

}
