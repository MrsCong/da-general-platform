package com.dgp.excel.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.dgp.common.exception.BaseException;
import com.dgp.excel.annotation.ExportExcel;
import com.dgp.excel.autoconfig.ExcelConfigProperties;
import com.dgp.excel.enhancer.WriterBuilderEnhancer;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

    public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
                                   ObjectProvider<List<Converter<?>>> converterProvider,
                                   WriterBuilderEnhancer excelWriterBuilderEnhance) {
        super(configProperties, converterProvider, excelWriterBuilderEnhance);
    }

    /**
     * obj 是List 且list不为空同时list中的元素不是是List 才返回true
     *
     * @param obj 返回对象
     * @return boolean
     */
    @Override
    public boolean support(Object obj) {
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            return !objList.isEmpty() && !(objList.get(0) instanceof List);
        } else {
            throw new BaseException("@ResponseExcel 返回值必须为List类型");
        }
    }

    @Override
    public void write(Object obj, HttpServletResponse response, ExportExcel exportExcel) {
        List<?> list = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, exportExcel);

        // 有模板则不指定sheet名
        Class<?> dataClass = list.get(0).getClass();
        WriteSheet sheet = this
                .sheet(exportExcel.sheets()[0], dataClass, exportExcel.template(),
                        exportExcel.headGenerator());
        excelWriter.write(list, sheet);
        excelWriter.finish();
    }

}
