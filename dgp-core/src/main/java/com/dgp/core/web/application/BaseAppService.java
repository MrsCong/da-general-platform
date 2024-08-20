package com.dgp.core.web.application;

import com.dgp.core.web.service.BaseBiz;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseAppService<Biz extends BaseBiz, Entity>  {

    @Autowired
    protected Biz bizService;

}
