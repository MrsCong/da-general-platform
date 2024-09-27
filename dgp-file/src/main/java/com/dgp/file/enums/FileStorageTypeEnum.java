package com.dgp.file.enums;

/**
 * 文件对象存储客户端类型
 */
public enum FileStorageTypeEnum {

    /**
     * 华为obs
     */
    HUAWEI(0, "huaWeiStorageServiceImpl"),

    /**
     * 七牛云
     */
    QINIU(1, "qiniuStorageServiceImpl"),

    ;

    FileStorageTypeEnum(int type, String storageService) {
        this.type = type;
        this.storageService = storageService;
    }

    /**
     * 存储类型
     */
    private final Integer type;

    /**
     * 对应实现类
     */
    private final String storageService;


    public Integer getType() {
        return type;
    }

    public String getStorageService() {
        return storageService;
    }


}
