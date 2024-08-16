package com.dgp.core.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EsPageRequest extends BaseRequest {

    @ApiModelProperty(value = "分页大小")
    Integer size = 20;

    @ApiModelProperty(value = "当前页数")
    Integer from = 0;

    public static Integer getEsCurrent(Integer current, Integer pageSize) {
        if (current == 1) {
            return 0;
        }
        current = current - 1;
        return (current * pageSize);
    }

}

