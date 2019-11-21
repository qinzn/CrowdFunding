package com.qzn.crowdfunding.manager.controller;

import com.qzn.crowdfunding.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author qzn
 * @create 2019/10/22 11:12
 */
@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/test")
    public String test(){
        System.out.println("TestController");

        testService.insert();

        return "success";
    }
}
