package com.dgp.test.application;

import cn.hutool.crypto.digest.MD5;
import com.dgp.common.security.md5.Md5EncryptionUtils;
import com.dgp.core.web.application.BaseAppService;
import com.dgp.test.entity.Test;
import com.dgp.test.service.TestService;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class TestAppService extends BaseAppService<TestService, Test> {
    public boolean add(Test test) {
        return bizService.add(test);
    }

    public static void main(String[] args) {
        Supplier s = () -> {
            return "ssss";
        };
        System.out.println(s.get());
        Function<Integer, Integer> f = i -> {
            return 2 * i;
        };
        System.out.println(f.andThen(f).apply(10));
        System.out.println(Md5EncryptionUtils.getMd5("123456"));
    }
}
