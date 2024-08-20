package com.dgp.test.application;

import com.dgp.core.web.application.BaseAppService;
import com.dgp.test.entity.Test;
import com.dgp.test.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestAppService extends BaseAppService<TestService, Test> {
    public boolean add(Test test) {
        return bizService.add(test);
    }
}
