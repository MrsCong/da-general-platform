package com.dgp.file.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileObsConfig {

    /**
     * 配置编码
     */
    @ApiModelProperty(value = "配置编码")
    private String configCode;

    /**
     * 配置目录名-obs目录名
     */
    @ApiModelProperty(value = "配置目录名-obs目录名")
    private String configDir;

    /**
     * 配置路径
     */
    @ApiModelProperty(value = "配置路径")
    private String configPath;

    /**
     * 配置地址-obs桶地址
     */
    @ApiModelProperty(value = "配置地址-obs桶地址")
    private String configUrl;
}
