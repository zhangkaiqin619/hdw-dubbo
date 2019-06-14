package com.hdw.upms.controller;

import com.hdw.common.base.BaseController;
import com.hdw.common.result.ResultMap;
import com.hdw.mq.config.ActiveMQJmxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descripton com.hdw.upms.controller
 * @Author TuMinglong
 * @Date 2019/6/14 12:04
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private ActiveMQJmxUtil activeMQJmxUtil;

    @GetMapping("/getInfo")
    public Object evalExecuteSubmit() {
        return ResultMap.ok().put("list", activeMQJmxUtil.getQueueList());
    }

}
