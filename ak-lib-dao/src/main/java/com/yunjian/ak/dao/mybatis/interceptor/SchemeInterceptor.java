package com.yunjian.ak.dao.mybatis.interceptor;

import com.yunjian.ak.dao.mybatis.dialect.*;
import com.yunjian.ak.dao.mybatis.enhance.ConnectionTenantScheme;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/26 12:47
 * @Version 1.0
 */
@Intercepts(@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}))
public class SchemeInterceptor implements Interceptor {

    private List<Dialect> dialects;

    public Object intercept(Invocation invocation) throws Throwable {
        Connection connection = (Connection)invocation.getArgs()[0];
        ConnectionTenantScheme.change(connection);

        Object result = invocation.proceed();
        return result;
    }

    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    public void setProperties(Properties properties) {
        if (!properties.containsKey("dialects") && this.dialects == null) {
            this.dialects = new LinkedList();
            this.dialects.add(new OracleDialect());
            this.dialects.add(new MysqlDialect());
            this.dialects.add(new SqlServer2012Dialect());
            this.dialects.add(new PostgreSqlDialect());
        }
    }
}
