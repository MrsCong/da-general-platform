package com.dgp.elasticsearch.common;

import com.dgp.elasticsearch.enums.EsActionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface EsMapperMethod {

	String index() default "";

	EsActionEnum action() default EsActionEnum.SEARCH;

	String param() default "";

	String key() default "id";

}
