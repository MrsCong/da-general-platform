package com.dgp.test.rest;

import com.dgp.core.http.response.ObjectResponse;
import com.dgp.core.web.rest.BaseController;
import com.dgp.kafka.core.KafkaSenderPool;
import com.dgp.test.application.TestAppService;
import com.dgp.test.entity.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api(tags = "测试模块")
public class TestController extends BaseController<TestAppService> {

    @Autowired
    private KafkaSenderPool kafkaSenderPool;

    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public ObjectResponse add(@RequestBody Test test) {
        return ObjectResponse.success(appService.add(test));
    }

    @GetMapping("/selectById")
    @ApiOperation(value = "主键查询")
    public ObjectResponse selectById(Long id) {
        return ObjectResponse.success(appService.selectById(id));
    }

    @GetMapping("/deleteById")
    @ApiOperation(value = "主键删除")
    public ObjectResponse deleteById(Long id) {
        return ObjectResponse.success(appService.deleteById(id));
    }

    @GetMapping("/kafkaTest")
    @ApiOperation(value = "kafka测试")
    public ObjectResponse kafkaTest(String message) {
        kafkaSenderPool.send("KAFKA_TEST_TOPIC", message);
        return ObjectResponse.success();
    }

}
