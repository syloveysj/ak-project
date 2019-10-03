package com.yunjian.ak.dao.annotation.transaction;

import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/31 12:22
 * @Version 1.0
 */
public class AkRuleBasedTransactionAttribute extends RuleBasedTransactionAttribute {
    private static final long serialVersionUID = 3342292373263920487L;
    private String datasourceId;

    public AkRuleBasedTransactionAttribute() {
    }

    public String getDatasourceId() {
        return this.datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }
}
