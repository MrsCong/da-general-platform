package com.dgp.job.http.response;

import com.dgp.job.http.response.item.JobInfoPageItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JobInfoPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 过滤的记录数
     */
    private int recordsFiltered;

    /**
     * 任务数据
     */
    private List<JobInfoPageItem> data;

    /**
     * 总记录数
     */
    private int recordsTotal;

}
