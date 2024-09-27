package com.dgp.test.rest;

import com.dgp.core.http.response.ObjectResponse;
import com.dgp.core.web.rest.BaseController;
import com.dgp.excel.annotation.ExportExcel;
import com.dgp.excel.annotation.ImportExcel;
import com.dgp.file.dto.FileInfo;
import com.dgp.file.service.IStorageService;
import com.dgp.file.utils.ObsUtil;
import com.dgp.job.enums.ScheduleTypeEnum;
import com.dgp.job.http.request.XxlJobInfoAddRequest;
import com.dgp.job.service.XxlJobService;
import com.dgp.kafka.core.KafkaSenderPool;
import com.dgp.redis.util.RedisUtil;
import com.dgp.test.application.TestAppService;
import com.dgp.test.entity.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
@Api(tags = "测试模块")
@Slf4j
public class TestController extends BaseController<TestAppService> {

    @Autowired
    private KafkaSenderPool kafkaSenderPool;

    @Autowired
    private XxlJobService xxlJobService;

    @Autowired
    private IStorageService iStorageService;

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
        kafkaSenderPool.send("APP-LOG", message);
        return ObjectResponse.success();
    }

    @GetMapping("/redisTest")
    @ApiOperation(value = "redis测试")
    public ObjectResponse redisTest(String message) {
        RedisUtil.expireSetStr("test", message, 10, TimeUnit.SECONDS);
        return ObjectResponse.success();
    }

    @PostMapping("/importExcelTest")
    @ApiOperation(value = "excel导入测试")
    public ObjectResponse importExcelTest(@ImportExcel(ignoreEmptyRow = true) List<Test> tests) {
        return ObjectResponse.success(tests);
    }

    @PostMapping("/exportExcelTest")
    @ApiOperation(value = "excel导出测试")
    @ExportExcel(name = "test")
    public List<Test> exportExcelTest() {
        return Arrays.asList(new Test(1L, "test", 18));
    }

    @GetMapping("/xxlJobTest")
    @ApiOperation(value = "xxlJob测试")
    public ObjectResponse xxlJobTest() {
        XxlJobInfoAddRequest request = new XxlJobInfoAddRequest();
        request.setAuthor("wzc");
        request.setJobDesc("测试job");
        request.setScheduleConf("1000");
        request.setScheduleType(ScheduleTypeEnum.FIX_RATE);
        request.setExecutorHandler("test");
        request.setAlarmEmail("wzc971201@163.com");
        Integer jobId = xxlJobService.add(request);
        xxlJobService.start(jobId);
//        xxlJobService.stop(414);
        return ObjectResponse.success();
    }

    @GetMapping("/logStashTest")
    @ApiOperation(value = "logStash测试")
    public ObjectResponse logStashTest(String message) {
        log.info(message);
        return ObjectResponse.success();
    }

    @PostMapping("/fileUpload")
    @ApiOperation(value = "文件上传")
    public ObjectResponse fileUpload(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = iStorageService.uploadFile(file, ObsUtil.getObsConfig("dgp-public"));
        return ObjectResponse.success(fileInfo);
    }

    @PostMapping("/fileDownload")
    @ApiOperation(value = "文件下载")
    public void fileDownload(@RequestBody FileInfo fileInfo, HttpServletResponse response) {
        iStorageService.downloadFile(response, ObsUtil.getObsConfig("dgp-public"), fileInfo);
    }


}
