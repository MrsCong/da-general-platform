package com.dgp.test.rest;

import com.dgp.core.http.response.ObjectResponse;
import com.dgp.core.web.rest.BaseController;
import com.dgp.test.application.TestAppService;
import com.dgp.test.entity.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api(tags = "测试模块")
public class TestController extends BaseController<TestAppService> {

    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public ObjectResponse add(@RequestBody Test test) {
        return ObjectResponse.success(appService.add(test));
    }

}
