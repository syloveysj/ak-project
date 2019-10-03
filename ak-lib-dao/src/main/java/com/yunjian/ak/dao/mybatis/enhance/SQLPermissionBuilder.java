package com.yunjian.ak.dao.mybatis.enhance;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.ioc.ApplicationContextManager;
import com.yunjian.ak.plugins.SQLPermissionPlugin;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/6 17:25
 * @Version 1.0
 */
public class SQLPermissionBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLPermissionBuilder.class);

    public SQLPermissionBuilder() {
    }

    public static void build(MappedStatement mappedStatement, BoundSql boundSql) {

        if(StringUtils.equalsIgnoreCase(ConfigManager.getInstance().getConfig("ak.dataPermission.enable"), "false")) return;

        //执行的sql所在的mapper文件
//        String resource = mappedStatement.getResource();
        //执行sql的dao文件的包名+方法名
        String mappedId = mappedStatement.getId();
        //sql语句类型 select、delete、insert、update
//        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        LOGGER.debug("mappedId = {}", mappedId);

        SQLPermissionPlugin sqlPermissionPlugin =  ApplicationContextManager.getContext().getBean(SQLPermissionPlugin.class);
        if(sqlPermissionPlugin != null) {
            String extr = sqlPermissionPlugin.getExpr(mappedId);
            if(StringUtils.isNotEmpty(extr)) {
                MetaObject.forObject(boundSql, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                        new DefaultReflectorFactory()).setValue("sql", "SELECT * FROM ( " + boundSql.getSql() + " ) AK_PERMISSION_VIEW WHERE 1=1 " + extr);
            }
        }

    }

}
