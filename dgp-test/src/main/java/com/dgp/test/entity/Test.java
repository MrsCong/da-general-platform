package com.dgp.test.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_test")
public class Test extends Model<Test> {

    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

}
