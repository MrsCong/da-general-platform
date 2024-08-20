package com.dgp.common.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 身份证信息
 * </p>
 *
 * @author Yangquanmin
 * @since 2020-10-09 11:44
 */
@Data
public class IdCardVO {

    /**
     * 初始年月
     */
    private LocalDate birthday;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 性别格式：F-女，M-男
     */
    private String sexCode;
}
