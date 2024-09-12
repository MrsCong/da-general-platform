package com.dgp.job.http.request;

import com.dgp.core.http.request.BaseRequest;
import com.dgp.job.enums.TriggerStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JobQueryRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private int jobId;

    /**
     * 任务执行器id
     */
    private int jobGroup;

    /**
     * 任务状态
     */
    private TriggerStatusEnum triggerStatus = TriggerStatusEnum.ALL;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 任务执行器
     */
    private String executorHandler;

    /**
     * 任务创建人
     */
    private String author;

}
