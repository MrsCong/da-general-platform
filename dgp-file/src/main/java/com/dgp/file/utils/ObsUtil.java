package com.dgp.file.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.dgp.file.config.FileObsConfig;
import com.dgp.file.config.ObsConfigProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * Obs工具类
 */
public class ObsUtil {

    /**
     * 根据code查询FileObsConfig
     * @param configCode
     * @return
     */
    public static FileObsConfig getObsConfig(String configCode) {
        ObsConfigProperties properties = SpringUtil.getBean(ObsConfigProperties.class);
        return properties.getConfigs().stream().filter(v -> StringUtils.equals(configCode, v.getConfigCode())).findFirst().orElse(null);
    }

}
