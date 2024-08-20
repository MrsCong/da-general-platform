package com.dgp.test.service;

import com.dgp.core.web.service.BaseBiz;
import com.dgp.test.entity.Test;
import com.dgp.test.mapper.TestMapper;
import org.springframework.stereotype.Service;

@Service
public class TestService extends BaseBiz<TestMapper, Test> {

    public boolean add(Test test) {
        return this.save(test);
    }
}
