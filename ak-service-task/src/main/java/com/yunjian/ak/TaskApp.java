package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: 调度任务服务
 * @Author: yong.sun
 * @Date: 2019/11/10 17:32
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
@EnableSwagger2
public class TaskApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskApp.class, args);
    }
}
