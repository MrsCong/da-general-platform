package com.dgp.redis.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueueConsumer {

    /**
     * 可消费的topic
     */
    String topic() default "";

    /**
     * 可消费的topic数组
     */
    String[] topics() default {};
}
