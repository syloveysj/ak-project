package com.yunjian.ak.dao.mybatis.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.yunjian.ak.dao.mybatis.enhance.Conditions;
import com.yunjian.ak.dao.mybatis.enhance.MybatisExtractor;
import com.yunjian.ak.dao.mybatis.enhance.ResultMappingMeta;
import com.yunjian.ak.dao.mybatis.enhance.Rule;
import com.yunjian.ak.dao.mybatis.exception.InvalidConditionParamException;
import com.yunjian.ak.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 条件Sql生成器
 * @Author: yong.sun
 * @Date: 2019/5/24 10:15
 * @Version 1.0
 */
public class ConditionSqlBuilder {
    private String pageSql;
    private Conditions condition;
    private MappedStatement ms;
    private List<ResultMappingMeta> sortedResultMappingModels;
    private String parsedSql;
    private Configuration configuration;
    private static final Map<String, String> simpleOps = new HashMap();
    private static final Map<String, String> likeOps;
    private static final Map<String, String> IncludeOps;
    private static final String CONDITION_SQL_TEMPLATE = "SELECT * FROM (%s) complex_query_tabName WHERE %s";
    private static final List<String> groupOps;

    public ConditionSqlBuilder(String pageSql, Conditions condition, MappedStatement ms) {
        this.pageSql = pageSql;
        this.condition = condition;
        this.ms = ms;
        this.sortedResultMappingModels = new ArrayList();
        this.configuration = (Configuration)MetaObject.forObject(ms, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory()).getValue("configuration");
    }

    public ConditionSqlBuilder build() throws ParseException {
        StringBuilder whereBy = new StringBuilder();
        this.checkConditionsIfInPhraseOverlength(this.condition);
        this.buildSqlByCondition(whereBy, this.condition);
        this.parsedSql = String.format("SELECT * FROM (%s) complex_query_tabName WHERE %s", this.pageSql, "(" + whereBy.toString() + ")");
        return this;
    }

    private void checkConditionsIfInPhraseOverlength(Conditions condition) {
        List<Rule> rules = condition.getRules();
        if (rules != null && rules.size() != 0) {
            Conditions addCond = null;
            Iterator it = rules.iterator();

            while(true) {
                Rule rule;
                Rule r;
                Object datas;
                int j;
                do {
                    while(true) {
                        do {
                            do {
                                do {
                                    if (!it.hasNext()) {
                                        if (condition.getGroups() != null && condition.getGroups().size() != 0) {
                                            Iterator iterator = condition.getGroups().iterator();

                                            while(iterator.hasNext()) {
                                                Conditions conditions = (Conditions)iterator.next();
                                                this.checkConditionsIfInPhraseOverlength(conditions);
                                            }
                                        }

                                        if (addCond != null) {
                                            if (condition.getRules().size() == 0) {
                                                condition.setGroupOp("OR");
                                                condition.setRules(addCond.getRules());
                                            } else {
                                                condition.append(addCond);
                                            }
                                        }

                                        return;
                                    }

                                    rule = (Rule)it.next();
                                } while(rule == null);
                            } while(StringUtils.isBlank(rule.getOp()));
                        } while(!"in".equals(rule.getOp()) && !"ni".equals(rule.getOp()));

                        if (List.class.isAssignableFrom(rule.getData().getClass())) {
                            datas = rule.getData() == null ? new ArrayList() : (List)rule.getData();
                            break;
                        }

                        if (rule.getData().getClass().isArray()) {
                            Object[] objects = rule.getData() == null ? new Object[0] : (Object[])rule.getData();
                            if (objects.length >= 1000) {
                                List<Object> temp = Arrays.asList(objects);
                                List<Rule> addRule = new ArrayList();

                                for(j = 0; j < Math.round((float)(objects.length / 1000)); ++j) {
                                    r = new Rule(rule.getField(), rule.getOp(), temp.subList(1000 * j, 1000 * (j + 1)));
                                    addRule.add(r);
                                }

                                if (temp.size() % 1000 != 0) {
                                    int lastsize = temp.size() - j * 1000;
                                    r = new Rule(rule.getField(), rule.getOp(), temp.subList(1000 * j, 1000 * j + lastsize));
                                    addRule.add(r);
                                }

                                addCond = new Conditions("OR", addRule);
                                it.remove();
                            }
                        }
                    }
                } while(((List)datas).size() < 1000);

                List<Rule> addRule = new ArrayList();

                for(j = 0; j < Math.round((float)(((List)datas).size() / 1000)); ++j) {
                    r = new Rule(rule.getField(), rule.getOp(), ((List)datas).subList(1000 * j, 1000 * (j + 1)));
                    addRule.add(r);
                }

                if (((List)datas).size() % 1000 != 0) {
                    j = ((List)datas).size() - j * 1000;
                    r = new Rule(rule.getField(), rule.getOp(), ((List)datas).subList(1000 * j, 1000 * j + j));
                    addRule.add(r);
                }

                addCond = new Conditions("OR", addRule);
                it.remove();
            }
        }
    }

    private StringBuilder buildSqlByCondition(StringBuilder whereBy, Conditions condition) throws ParseException {
        if (StringUtils.isNotBlank(condition.getGroupOp()) && !groupOps.contains(condition.getGroupOp().toLowerCase().trim())) {
            throw new InvalidConditionParamException("Conditions连接符入参出现非法字符...");
        } else {
            int j;
            if (condition.getGroups() != null && condition.getGroups().size() != 0) {
                for(j = 0; j < condition.getGroups().size(); ++j) {
                    StringBuilder sub = this.buildSqlByCondition(new StringBuilder(), (Conditions)condition.getGroups().get(j));
                    if (sub.length() != 0) {
                        if (j != 0) {
                            whereBy.append(" " + condition.getGroupOp() + " ");
                        }

                        whereBy.append("(" + sub.toString() + ")");
                    }
                }
            }

            if (condition.getRules() == null) {
                return whereBy;
            } else {
                if (whereBy.length() != 0) {
                    whereBy.append(" " + condition.getGroupOp() + " ");
                }

                for(j = 0; j < condition.getRules().size(); ++j) {
                    if (j == 0) {
                        whereBy.append(this.getRuleSql((Rule)condition.getRules().get(j)));
                    } else {
                        whereBy.append(" " + condition.getGroupOp() + " " + this.getRuleSql((Rule)condition.getRules().get(j)));
                    }
                }

                return whereBy;
            }
        }
    }

    private String getRuleSql(Rule rule) throws ParseException {
        ResultMappingMeta fieldMapping = null;
        fieldMapping = MybatisExtractor.extractColumnMapping(rule.getField(), this.ms, this.configuration);
        Preconditions.checkArgument(fieldMapping != null, "复杂查询解析过程出现异常,属性为:" + rule.getField() + "未在resultMap中找到定义...");
        if (simpleOps.containsKey(rule.getOp())) {
            if (rule.getData() != null && !rule.getData().getClass().isAssignableFrom(fieldMapping.getJavaType())) {
                try {
                    fieldMapping.setData(JSON.parseObject(rule.getData().toString(), fieldMapping.getJavaType()));
                } catch (Exception e) {
                    if (Date.class.isAssignableFrom(fieldMapping.getJavaType()) && rule.getData() != null && rule.getData() instanceof String) {
                        fieldMapping.setData(JSON.parseObject(this.getDateMillis(rule.getData().toString()), fieldMapping.getJavaType()));
                    }
                }
            } else {
                fieldMapping.setData(rule.getData());
            }

            if (!rule.getOp().matches("^(nu|nn)$")) {
                this.sortedResultMappingModels.add(fieldMapping);
            }

            return fieldMapping.getColumnName() + ((String)simpleOps.get(rule.getOp())).replaceFirst("data", "?");
        } else if (likeOps.containsKey(rule.getOp())) {
            if (rule.getData() != null) {
                Preconditions.checkArgument(rule.getData().getClass() == String.class, "复杂查询解析过程出现异常,属性为:" + rule.getField() + "使用字符串匹配时入参必须为字符串类型...");
            }

            fieldMapping.setData(((String)likeOps.get(rule.getOp())).split("LIKE")[1].trim().replaceFirst("data", rule.getData().toString()));
            this.sortedResultMappingModels.add(fieldMapping);
            return fieldMapping.getColumnName() + ((String)likeOps.get(rule.getOp())).replaceFirst("%?data%?", "?");
        } else if (!IncludeOps.containsKey(rule.getOp())) {
            throw new RuntimeException("复杂查询解析过程出现异常...没有匹配的谓词:" + rule.getOp());
        } else {
            StringBuilder parameterStr = new StringBuilder();
            if (List.class.isAssignableFrom(rule.getData().getClass())) {
                List<Object> datas = rule.getData() == null ? new ArrayList() : (List)rule.getData();
                Iterator iterator = ((List)datas).iterator();

                while(iterator.hasNext()) {
                    Object obj = iterator.next();
                    if (obj != null) {
                        Preconditions.checkArgument(obj.getClass() == fieldMapping.getJavaType(), "复杂查询解析过程出现异常,入参:" + rule.getField() + "在resultMap中的定义类型为" + fieldMapping.getJavaTypeStr() + ",实际传入类型却为:" + obj.getClass());
                    }

                    if (parameterStr.length() != 0) {
                        parameterStr.append(",");
                    }

                    parameterStr.append("?");
                    ResultMappingMeta fieldMappingItem = fieldMapping.clone();
                    fieldMappingItem.setData(obj);
                    this.sortedResultMappingModels.add(fieldMappingItem);
                }
            } else {
                int k;
                if (rule.getData().getClass().isArray()) {
                    Object[] datas = rule.getData() == null ? new Object[0] : (Object[])((Object[])rule.getData());
                    Object[] objects = datas;
                    k = datas.length;

                    for(int i = 0; i < k; ++i) {
                        Object obj = objects[i];
                        ResultMappingMeta fieldMappingItem = fieldMapping.clone();
                        if (parameterStr.length() != 0) {
                            parameterStr.append(",");
                        }

                        if (obj != null && !obj.getClass().isAssignableFrom(fieldMapping.getJavaType())) {
                            try {
                                fieldMappingItem.setData(JSON.parseObject(obj.toString(), fieldMapping.getJavaType()));
                            } catch (Exception e) {
                                if (Date.class.isAssignableFrom(fieldMapping.getJavaType()) && rule.getData() != null && obj instanceof String) {
                                    fieldMappingItem.setData(JSON.parseObject(this.getDateMillis(obj.toString()), fieldMapping.getJavaType()));
                                }
                            }
                        } else {
                            fieldMappingItem.setData(obj);
                        }

                        parameterStr.append("?");
                        this.sortedResultMappingModels.add(fieldMappingItem);
                    }
                } else if (rule.getData().getClass() == String.class) {
                    String[] strings = rule.getData().toString().split(",");
                    int length = strings.length;

                    for(k = 0; k < length; ++k) {
                        String data = strings[k];
                        if (parameterStr.length() != 0) {
                            parameterStr.append(",");
                        }

                        parameterStr.append("?");
                        fieldMapping.setData(JSON.parseObject(data, fieldMapping.getJavaType()));
                        this.sortedResultMappingModels.add(fieldMapping);
                    }
                } else {
                    if (rule.getData() != null) {
                        Preconditions.checkArgument(rule.getData().getClass() == fieldMapping.getJavaType(), "复杂查询解析过程出现异常,入参:" + rule.getField() + "在resultMap中的定义类型为" + fieldMapping.getJavaTypeStr() + ",实际传入类型却为:" + rule.getData().getClass());
                    }

                    parameterStr.append("?");
                }
            }

            return fieldMapping.getColumnName() + ((String)IncludeOps.get(rule.getOp())).replaceFirst("data", parameterStr.toString());
        }
    }

    public List<ResultMappingMeta> getSortedResultMappingModels() {
        return this.sortedResultMappingModels;
    }

    public String getSql() {
        return this.parsedSql;
    }

    private String getDateMillis(String date) throws ParseException {
        String toDateFormater = "yyyy-MM-dd";
        String toTimeFormater = "HH:mm:ss";
        int colon;
        if (date.matches("\\d{4}(-(0\\d{1}|1[0-2]))?(-(0\\d{1}|[12]\\d{1}|3[01]))?(\\s+)?(0\\d{1}|1\\d{1}|2[0-3])?(:[0-5]\\d{1})?(:([0-5]\\d{1}))?")) {
            colon = date.split("-").length - 1;
            if (colon < 2) {
                toDateFormater = StringUtil.getMatchedStr(toDateFormater, "^[^-]+-{" + colon + "}[^-]+", 2);
            }
        } else {
            toDateFormater = "";
        }

        if (date.matches("\\d{4}-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s+(0\\d{1}|1\\d{1}|2[0-3])(:[0-5]\\d{1})?(:([0-5]\\d{1}))?")) {
            colon = date.split(":").length - 1;
            if (colon < 2) {
                toTimeFormater = StringUtil.getMatchedStr(toTimeFormater, "^[^:]+:{" + colon + "}[^:]+", 2);
            }
        } else {
            toTimeFormater = "";
        }

        return String.valueOf((new SimpleDateFormat((toDateFormater + " " + toTimeFormater).trim())).parse(date).getTime());
    }

    static {
        simpleOps.put("eq", " = data");
        simpleOps.put("ne", " != data");
        simpleOps.put("lt", " < data");
        simpleOps.put("le", " <= data");
        simpleOps.put("gt", " > data");
        simpleOps.put("ge", " >= data");
        simpleOps.put("nu", " is null");
        simpleOps.put("nn", " is not null");
        likeOps = new HashMap();
        likeOps.put("bw", " LIKE data%");
        likeOps.put("bn", " NOT LIKE data%");
        likeOps.put("ew", " LIKE %data");
        likeOps.put("en", " NOT LIKE %data");
        likeOps.put("cn", " LIKE %data%");
        likeOps.put("nc", " NOT LIKE %data%");
        IncludeOps = new HashMap();
        IncludeOps.put("in", " IN (data)");
        IncludeOps.put("ni", " NOT IN (data)");
        groupOps = new ArrayList();
        groupOps.add("and");
        groupOps.add("or");
    }
}
