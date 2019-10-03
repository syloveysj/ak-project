package com.yunjian.ak.dao.annotation.transaction.parser;

import com.yunjian.ak.dao.annotation.transaction.AkRuleBasedTransactionAttribute;
import com.yunjian.ak.dao.annotation.transaction.AkTransactional;
import com.yunjian.ak.dao.core.DynamicDataSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.TransactionAnnotationParser;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/31 12:24
 * @Version 1.0
 */
public class AkTransactionAnnotationParser implements TransactionAnnotationParser, Serializable {
    private static final long serialVersionUID = -1998442217875751674L;

    public AkTransactionAnnotationParser() {
    }

    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae) {
        AkTransactional ann = (AkTransactional) AnnotationUtils.getAnnotation(ae, AkTransactional.class);
        return ann != null ? this.parseTransactionAnnotation(ann) : null;
    }

    public TransactionAttribute parseTransactionAnnotation(AkTransactional ann) {
        AkRuleBasedTransactionAttribute rbta = new AkRuleBasedTransactionAttribute();
        rbta.setPropagationBehavior(ann.propagation().value());
        rbta.setIsolationLevel(ann.isolation().value());
        rbta.setTimeout(ann.timeout());
        rbta.setReadOnly(ann.readOnly());
        rbta.setQualifier(ann.value());
        if (ann.sys()) {
            rbta.setDatasourceId(DynamicDataSource.getDSIDFromFeature("sys"));
        } else {
            rbta.setDatasourceId(ann.datasourceID());
        }

        ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList();
        Class[] rbf = ann.rollbackFor();
        Class[] rbf1 = rbf;
        int length = rbf.length;

        int i;
        for(i = 0; i < length; ++i) {
            Class rbRule = rbf1[i];
            RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }

        String[] rbfc = ann.rollbackForClassName();
        String[] rbfc1 = rbfc;
        i = rbfc.length;

        int i1;
        for(i1 = 0; i1 < i; ++i1) {
            String rbRule = rbfc1[i1];
            RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }

        Class[] nrbf = ann.noRollbackFor();
        Class[] nrbf1 = nrbf;
        i1 = nrbf.length;

        int i2;
        for(i2 = 0; i2 < i1; ++i2) {
            Class rbRule = nrbf1[i2];
            NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }

        String[] nrbfc = ann.noRollbackForClassName();
        String[] nrbfc1 = nrbfc;
        i2 = nrbfc.length;

        for(int i3 = 0; i3 < i2; ++i3) {
            String rbRule = nrbfc1[i3];
            NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }

        rbta.getRollbackRules().addAll(rollBackRules);
        return rbta;
    }
}