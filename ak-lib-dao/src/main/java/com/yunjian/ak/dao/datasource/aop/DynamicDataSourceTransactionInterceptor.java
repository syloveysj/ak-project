package com.yunjian.ak.dao.datasource.aop;

import com.yunjian.ak.dao.annotation.transaction.AkRuleBasedTransactionAttribute;
import com.yunjian.ak.dao.core.DynamicDataSource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/31 12:20
 * @Version 1.0
 */
public class DynamicDataSourceTransactionInterceptor extends TransactionInterceptor {
    private static final long serialVersionUID = -6778603607693069958L;

    public DynamicDataSourceTransactionInterceptor() {
        super();
    }

    public DynamicDataSourceTransactionInterceptor(PlatformTransactionManager ptm, Properties attributes) {
        super(ptm, attributes);
    }

    public DynamicDataSourceTransactionInterceptor(PlatformTransactionManager ptm, TransactionAttributeSource tas) {
        super(ptm, tas);
    }

    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinpointIdentification) {
        if (txAttr instanceof AkRuleBasedTransactionAttribute) {
            DynamicDataSource.setDataSourceKey(((AkRuleBasedTransactionAttribute)txAttr).getDatasourceId());
        }

        return super.createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        return super.invoke(invocation);
    }
}
