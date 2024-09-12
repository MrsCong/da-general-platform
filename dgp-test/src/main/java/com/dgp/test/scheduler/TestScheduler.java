package com.dgp.test.scheduler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class TestScheduler {

    @XxlJob("test")
    private void test() {
        System.out.println("execute test------------");
    }

}
