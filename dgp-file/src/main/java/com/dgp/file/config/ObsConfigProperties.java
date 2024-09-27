package com.dgp.file.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "obs")
public class ObsConfigProperties {

    @ApiModelProperty(value = "obs配置编码")
    private String obsCode;

    @ApiModelProperty(value = "obs配置名称")
    private String obsName;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "secretKey")
    private String secretKey;

    @ApiModelProperty(value = "endpoint")
    private String endpoint;

    @ApiModelProperty(value = "obs配置")
    private List<FileObsConfig> configs;

}
