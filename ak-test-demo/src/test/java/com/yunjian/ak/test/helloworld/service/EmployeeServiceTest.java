package com.yunjian.ak.test.helloworld.service;

import com.yunjian.ak.test.helloworld.dao.EmployeeDao;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/8 23:10
 * @Version 1.0
 */
public class EmployeeServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetTotalEmployeeWithMock() {
        EmployeeDao employeeDao = PowerMockito.mock(EmployeeDao.class);
        PowerMockito.when(employeeDao.getTotal()).thenReturn(10);
        EmployeeService service = new EmployeeService(employeeDao);
        int total = service.getTotalEmployee();
        assertEquals(10, total);
    }

    @Ignore
    @Test
    public void getTotalEmployee() {
        final EmployeeDao employeeDao = new EmployeeDao();
        final EmployeeService service = new EmployeeService(employeeDao);
        int total = service.getTotalEmployee();
        assertEquals(10, total);
    }
}