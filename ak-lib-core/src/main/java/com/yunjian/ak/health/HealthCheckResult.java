package com.yunjian.ak.health;

/**
 * @Description: 健康检测结果
 * @Author: yong.sun
 * @Date: 2019/5/22 11:02
 * @Version 1.0
 */
public class HealthCheckResult {
    private String name;
    private HealthStatus status;
    private String desc;

    public HealthCheckResult(String name, HealthStatus status, String desc) {
        this.name = name;
        this.status = status;
        this.desc = desc;
    }

    public HealthCheckResult(String name, HealthStatus status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HealthStatus getStatus() {
        return this.status;
    }

    public void setStatus(HealthStatus status) {
        this.status = status;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

