package com.dgp.test.mapper;

import com.dgp.core.web.mapper.BizMapper;
import com.dgp.test.entity.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BizMapper<Test> {
}
