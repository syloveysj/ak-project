package com.yunjian.ak.dao.mybatis.enhance;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:11
 * @Version 1.0
 */
public class Conditions {
    private String groupOp;
    private List<Rule> rules;
    private List<Conditions> groups;

    public Conditions() {
    }

    public Conditions(String groupOp) {
        this.groupOp = groupOp;
    }

    public Conditions(String groupOp, List<Rule> rules) {
        this.groupOp = groupOp;
        this.rules = rules;
    }

    public Conditions(String groupOp, List<Rule> rules, List<Conditions> groups) {
        this.groupOp = groupOp;
        this.rules = rules;
        this.groups = groups;
    }

    public Conditions append(String op) {
        this.groupOp = op;
        return this;
    }

    public Conditions append(Conditions condition) {
        if (this.groups == null) {
            this.groups = new ArrayList();
        }

        this.groups.add(condition);
        return this;
    }

    public Conditions append(Rule rule) {
        if (this.rules == null) {
            this.rules = new ArrayList();
        }

        this.rules.add(rule);
        return this;
    }

    public List<Conditions> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Conditions> groups) {
        this.groups = groups;
    }

    public String getGroupOp() {
        return this.groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getRuleSql() {
        return null;
    }

    public String getGroupOpSql() {
        return this.groupOp;
    }

    public static Conditions New() {
        return new Conditions();
    }
}

