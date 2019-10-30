package com.yunjian.ak.swagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/30 22:19
 * @Version 1.0
 */
@Controller
@RequestMapping("/v1/swagger/")
public class SwaggerController {
    @GetMapping("/index")
    public String test(HttpServletRequest request, @RequestParam(value = "id", required = true) String id){
        System.out.println(id);
        request.setAttribute("id", id);
        return "index";
    }
}
