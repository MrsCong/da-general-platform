package com.dgp.test.application;

import com.dgp.core.web.application.BaseAppService;
import com.dgp.test.entity.Test;
import com.dgp.test.esmapper.EsTestMapper;
import com.dgp.test.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestAppService extends BaseAppService<TestService, Test> {

    @Resource
    private EsTestMapper esTestMapper;

    public int add(Test test) {
        bizService.save(test);
        return esTestMapper.insertById(test);
    }

    public Test selectById(Long id) {
        return esTestMapper.selectById(id);
    }

    public int deleteById(Long id) {
        bizService.removeById(id);
        return esTestMapper.deleteById(id);
    }
}
