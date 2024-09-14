package com.dgp.test.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "test", url = "http://localhost:9000")
public interface TestFeign {
}
