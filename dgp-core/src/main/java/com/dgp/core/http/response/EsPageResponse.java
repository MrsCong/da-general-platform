package com.dgp.core.http.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Es分页VO")
public class EsPageResponse<T> {

	@ApiModelProperty(value = "响应时间", required = true)
	private String timestamp;
	/**
	 * 记录总数
	 */
	@ApiModelProperty(value = "记录总数", dataType = "int")
	private Integer total;

	@ApiModelProperty(value = "总页数", required = true)
	private Integer totalPage;

	@ApiModelProperty(value = "分页大小")
	private Integer pageSize;

	@ApiModelProperty(value = "当前页数")
	private Integer current;

	/**
	 * 元素列表
	 */
	@ApiModelProperty(value = "当前页元素", dataType = "int")
	private List<T> records;

	public Integer getTotalPage() {
		totalPage = 1;
		if (pageSize != null) {
			if (total % pageSize == 0) {
				totalPage = total / pageSize;
			} else {
				totalPage = total / pageSize + 1;
			}
		}
		return totalPage;

	}

}
