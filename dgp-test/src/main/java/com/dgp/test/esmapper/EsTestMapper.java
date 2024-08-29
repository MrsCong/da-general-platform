package com.dgp.test.esmapper;

import com.dgp.elasticsearch.common.EsMapper;
import com.dgp.elasticsearch.common.EsMapperMethod;
import com.dgp.elasticsearch.enums.EsActionEnum;
import com.dgp.test.entity.Test;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@EsMapper(index = "test")
@Repository
public interface EsTestMapper {

    @EsMapperMethod(action = EsActionEnum.INSERT_BY_ID)
    int insertById(Test test);

    @EsMapperMethod(action = EsActionEnum.SEARCH_BY_ID)
    @Select("#{id}")
    Test selectById(@Param("id") Long id);

    @EsMapperMethod(action = EsActionEnum.DELETE_BY_ID)
    @Delete("#{id}}")
    int deleteById(@Param("id") Long id);
}
