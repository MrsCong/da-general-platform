package com.dgp.file.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class QiNiuCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String code = context.getEnvironment().getProperty("obs.obsCode");
        return StringUtils.equals("QINIU", code);
    }
}
