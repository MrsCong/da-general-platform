package com.dgp.excel.handler;


import com.dgp.excel.annotation.ExportExcel;

import javax.servlet.http.HttpServletResponse;

public interface SheetWriteHandler {

    /**
     * 是否支持
     */
    boolean support(Object obj);

    /**
     * 校验
     *
     * @param exportExcel 注解
     */
    void check(ExportExcel exportExcel);

    /**
     * 返回的对象
     *
     * @param o             obj
     * @param response      输出对象
     * @param exportExcel 注解
     */
    void export(Object o, HttpServletResponse response, ExportExcel exportExcel);

    /**
     * 写成对象
     *
     * @param o             obj
     * @param response      输出对象
     * @param exportExcel 注解
     */
    void write(Object o, HttpServletResponse response, ExportExcel exportExcel);

}
