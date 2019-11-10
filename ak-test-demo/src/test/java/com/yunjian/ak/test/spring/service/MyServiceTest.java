package com.yunjian.ak.test.spring.service;

import com.yunjian.ak.TestApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/8 22:49
 * @Version 1.0
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
// 这里 Ignore的类都会通过spring的ClassLoader来加载，尽量要让类都从spring的ClassLoader来加载，否则有问题
@PowerMockIgnore({ "com.*", "io.*", "org.*", "ch.*", "javax.*" })
@PrepareForTest({OvertimeUtil.class})
@SpringBootTest(classes = { TestApp.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyServiceTest {

    @Autowired
    private MyService myService;
    @Mock
    private MySecondService mySecondService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void sayHello() {
        PowerMockito.mockStatic(OvertimeUtil.class);
//        Mockito.when(OvertimeUtil.calculationTime(Mockito.any(), Mockito.any())).thenReturn("4d");
//        System.out.println(OvertimeUtil.calculationTime(new Date(), new Date()));
        System.out.println(myService.sayHello("Da."));
//        Mockito.when(mySecondService.show(Mockito.anyString())).thenReturn("show test!");
        System.out.println(mySecondService.show("asd"));
    }
}