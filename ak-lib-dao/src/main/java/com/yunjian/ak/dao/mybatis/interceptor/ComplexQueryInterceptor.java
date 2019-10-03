package com.yunjian.ak.dao.mybatis.interceptor;

import com.yunjian.ak.dao.mybatis.enhance.ComplexQueryContext;
import com.yunjian.ak.dao.mybatis.enhance.ResultMappingMeta;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Properties;

/**
 * @Description: 复杂查询插件
 * @Author: yong.sun
 * @Date: 2019/5/23 19:03
 * @Version 1.0
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class, ResultHandler.class}
)})
public class ComplexQueryInterceptor implements Interceptor {
    public ComplexQueryInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler)invocation.getTarget();
        Object obj = null;
        MetaObject handlerObj = null;

        try {
            handlerObj = MetaObject.forObject(handler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            obj = handlerObj.getValue("delegate.rowBounds");
        } catch (Exception e) {
            return invocation.proceed();
        }

        if (obj != null && obj instanceof ComplexQueryContext && ((ComplexQueryContext)obj).getRmModels().size() != 0) {
            ComplexQueryContext context = (ComplexQueryContext)obj;
            Object[] args = invocation.getArgs();
            PreparedStatement ps = (PreparedStatement)args[0];
            ResultHandler resultHandler = (ResultHandler)args[1];
            BoundSql boundSql = (BoundSql)handlerObj.getValue("delegate.boundSql");
            int i = boundSql.getParameterMappings().size();
            Configuration configuration = (Configuration)handlerObj.getValue("delegate.configuration");
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            Iterator iterator = context.getRmModels().iterator();

            while(iterator.hasNext()) {
                ResultMappingMeta resultMapping = (ResultMappingMeta)iterator.next();
                TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(resultMapping.getJavaType(), resultMapping.getJdbcType());
                ++i;
                typeHandler.setParameter(ps, i, resultMapping.getData(), resultMapping.getJdbcType());
            }

            return handler.query(ps, resultHandler);
        } else {
            return invocation.proceed();
        }
    }

    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    public void setProperties(Properties properties) {
    }
}
