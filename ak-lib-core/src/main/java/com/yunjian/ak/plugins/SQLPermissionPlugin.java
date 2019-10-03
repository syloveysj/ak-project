package com.yunjian.ak.plugins;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 21:04
 * @Version 1.0
 */
public interface SQLPermissionPlugin {
    /**
     * 根据sqlid获取数据的过滤sql片段
     * @param mappedId
     * @return
     */
    String getExpr(String mappedId);
}
