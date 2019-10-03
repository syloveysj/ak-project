package com.yunjian.ak.dao.config;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.context.OperationContextHolder;
import com.yunjian.ak.dao.core.DynamicDataSource;
import com.yunjian.ak.dao.mybatis.DefaultMybatisDao;
import com.yunjian.ak.dao.mybatis.dialect.*;
import com.yunjian.ak.dao.mybatis.interceptor.ComplexQueryInterceptor;
import com.yunjian.ak.dao.mybatis.interceptor.MybatisEnhanceInterceptor;
import com.yunjian.ak.dao.mybatis.interceptor.SYSPageInterceptor;
import com.yunjian.ak.dao.mybatis.interceptor.SchemeInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description: 数据源配置
 * @Author: yong.sun
 * @Date: 2019/5/23 18:06
 * @Version 1.0
 */
@Configuration
@ImportResource("classpath:transaction.xml")
public class DataSourceConfig {

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        return dynamicDataSource;
    }

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties p = new Properties();
        p.setProperty("SQL Server", "sqlserver");
        p.setProperty("DB2", "db2");
        p.setProperty("Oracle", "oracle");
        p.setProperty("MySQL", "mysql");
        p.setProperty("DM DBMS", "dm");
        p.setProperty("PostgreSQL", "postgresql");
        databaseIdProvider.setProperties(p);
        return databaseIdProvider;
    }

    @Bean
    public List<Dialect> dialects() {
        List<Dialect> dialects = new ArrayList<Dialect>();
        dialects.add(new OracleDialect());
        dialects.add(new MysqlDialect());
        dialects.add(new SqlServer2012Dialect());
        dialects.add(new DMDialect());
        dialects.add(new PostgreSqlDialect());
        return dialects;
    }

    @Bean
    public Interceptor[] defaultPlugins(List<Dialect> dialects) {
        MybatisEnhanceInterceptor mybatisEnhanceInterceptor = new MybatisEnhanceInterceptor();
        mybatisEnhanceInterceptor.setDialects(dialects);

        SYSPageInterceptor sysPageInterceptor = new SYSPageInterceptor();
        sysPageInterceptor.setDialects(dialects);

        ComplexQueryInterceptor complexQueryInterceptor = new ComplexQueryInterceptor();

        Interceptor[] defaultPlugins = null;
        if(OperationContextHolder.enableTenant()) {
            SchemeInterceptor schemeInterceptor = new SchemeInterceptor();

            defaultPlugins = new Interceptor[]{
                schemeInterceptor,
                mybatisEnhanceInterceptor,
                sysPageInterceptor,
                complexQueryInterceptor
            };
        } else {
            defaultPlugins = new Interceptor[]{
                mybatisEnhanceInterceptor,
                sysPageInterceptor,
                complexQueryInterceptor
            };
        }

        return defaultPlugins;
    }

    @Bean
    public SqlSessionFactoryBean defaultSqlSessionFactory(DynamicDataSource dynamicDataSource, DatabaseIdProvider databaseIdProvider, Interceptor[] defaultPlugins) throws Exception {
        ConfigManager config = ConfigManager.getInstance();
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);
        factoryBean.setDatabaseIdProvider(databaseIdProvider);

        //设置mybatis的配置文件路径
        factoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        //配置路径匹配器，获取匹配的文件
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(config.getConfig("mybatis.mapper-locations")));

        factoryBean.setPlugins(defaultPlugins);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate defaultSqlSessionTemplate(SqlSessionFactory defaultSqlSessionFactory) {
        return new SqlSessionTemplate(defaultSqlSessionFactory);
    }

    @Bean
    public DefaultMybatisDao baseDao(SqlSessionTemplate defaultSqlSessionTemplate) throws Exception {
        DefaultMybatisDao baseDao = new DefaultMybatisDao();
        baseDao.setSqlSessionTemplate(defaultSqlSessionTemplate);
        return baseDao;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dynamicDataSource);
        return transactionManager;
    }

}
