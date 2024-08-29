package com.dgp.web.utils;

import com.dgp.common.utils.SnowflakeId;
import com.dgp.common.utils.UUIDUtil;
import lombok.Data;

@Data
public class SnowflakeUtil {

    private SnowflakeId snowflake;
    private Integer snowflakeConfigId = 0;

    public void initSnowflake(int snowflakeConfigId, int machineId, int dataId) {
        this.setSnowflakeConfigId(snowflakeConfigId);
        snowflake = new SnowflakeId(machineId, dataId);
    }

    public Long longId() {
        return snowflake.nextId();
    }

    public String longStringId() {
        return String.valueOf(snowflake.nextId());
    }

    public String stringId() {
        return UUIDUtil.generateShortUuid();
    }

}
