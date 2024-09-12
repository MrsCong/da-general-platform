package com.dgp.job.http.response.item;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务组ID
     */
    private int id;

    /**
     * 执行器AppName
     */
    private String appName;

    /**
     * 任务组名称
     */
    private String title;

    /**
     * 地址类型：0=自动注册、1=手动录入
     */
    private int addressType;

    /**
     * 执行器地址列表，多地址逗号分隔
     */
    private String addressList;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 执行器注册节点列表
     */
    private List<String> registryList;

}
