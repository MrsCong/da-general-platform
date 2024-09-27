package com.dgp.file.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 9527L;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty(value = "文件key")
    private String fileKey;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件存储名称")
    private String saveName;

    @ApiModelProperty(value = "文件存储类型")
    private Integer storageType;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "文件类型")
    private Integer fileType;

    @ApiModelProperty(value = "文件url", notes = "存储在OSS上的url")
    private String fileUrl;

    @ApiModelProperty(value = "缩略图url")
    private String thumbUrl;

    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;


}
