package com.dgp.core.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class BaseRequest {

    @ApiModelProperty(value = "分页大小")
    Integer pageSize = 20;

    @ApiModelProperty(value = "当前页数")
    Integer current = 1;

}
