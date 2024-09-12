package com.dgp.job.http.response;

import com.dgp.job.http.response.item.DataItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JobGroupPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 过滤的记录数
     */
    private int recordsFiltered;

    /**
     * 任务组数据
     */
    private List<DataItem> data;

    /**
     * 总记录数
     */
    private int recordsTotal;

}
