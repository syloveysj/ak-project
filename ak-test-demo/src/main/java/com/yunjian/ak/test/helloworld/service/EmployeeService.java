package com.yunjian.ak.test.helloworld.service;

import com.yunjian.ak.test.helloworld.dao.EmployeeDao;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/8 23:06
 * @Version 1.0
 */
public class EmployeeService {
    private EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    /**
     * 获取所有员工的数量.
     *
     * @return
     */
    public int getTotalEmployee() {
        return employeeDao.getTotal();
    }
}
