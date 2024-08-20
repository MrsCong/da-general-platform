package com.dgp.core.web.rest;

import com.dgp.core.web.application.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController<AppService extends BaseAppService> {

    @Autowired
    public AppService appService;

}
