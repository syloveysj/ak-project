package com.yunjian.ak.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @Description: 配置管理
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public class ConfigManager extends PropertySourcesPlaceholderConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private static ConfigManager instance;
    private MutablePropertySources propertySources;
    private ResourcePropertySource mainPropertySource;
    private List<ResourcePropertySource> defaultPropertySources;

    /**
     * 获取配置管理实例
     * @return
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }

        return instance;
    }

    /**
     * 重新加载配置
     */
    public static synchronized void reload() {
        instance = new ConfigManager();
    }

    /**
     * 加载属性配置
     */
    protected ConfigManager() {
        this.setFileEncoding("utf-8");
        StandardEnvironment environment = new StandardEnvironment();
        this.propertySources = environment.getPropertySources();

        try {
            String profile = environment.getProperty("ak.config.profile");
            if (StringUtils.isBlank(profile)) {
                profile = environment.getProperty("ak_config_profile");
            }

            if (StringUtils.isNotEmpty(profile)) {
                try {
                    this.mainPropertySource = new ResourcePropertySource(new ClassPathResource("ak-config-" + profile + ".properties"));
                } catch (FileNotFoundException e) {
                    LOGGER.error("系统指定了profile：[{}]生效，但是平台在classpath中没有找到ak-config-{}.properties文件，切换到默认配置文件(ak-config.properties)!", profile, profile);
                }
            }

            if (this.mainPropertySource == null) {
                try {
                    this.mainPropertySource = new ResourcePropertySource(new ClassPathResource("ak-config.properties"));
                } catch (FileNotFoundException ffe) {
                }
            }

            if (this.mainPropertySource != null) {
                this.propertySources.addLast(this.mainPropertySource);
            }

            Resource[] matchedResources = (new PathMatchingResourcePatternResolver()).getResources("classpath*:application*.properties");
            if (matchedResources != null && matchedResources.length > 0) {
                this.defaultPropertySources = new ArrayList(matchedResources.length);
                Resource[] resources = matchedResources;
                int length = matchedResources.length;

                for(int i = 0; i < length; ++i) {
                    Resource resource = resources[i];
                    ResourcePropertySource source = new ResourcePropertySource(resource);
                    this.defaultPropertySources.add(source);
                    this.propertySources.addLast(source);
                }
            }
        } catch (IOException e) {
            LOGGER.error("加载属性文件时发生未知错误", e);
        }

        this.setPropertySources(this.propertySources);
        instance = this;
    }

    /**
     * 获取配置
     * @param name
     * @return
     */
    public String getConfig(String name) {
        Iterator iterator = this.propertySources.iterator();

        while(iterator.hasNext()) {
            PropertySource<?> propertySource = (PropertySource)iterator.next();
            if (propertySource.containsProperty(name)) {
                return (String)propertySource.getProperty(name);
            }
        }

        return null;
    }

    /**
     * 获取配置
     * @param name
     * @return
     */
    public Integer getIntegerConfig(String name, Integer defaultValue) {
        try {
            Iterator iterator = this.propertySources.iterator();

            while(iterator.hasNext()) {
                PropertySource<?> propertySource = (PropertySource)iterator.next();
                if (propertySource.containsProperty(name)) {
                    Object answer = propertySource.getProperty(name);
                        if(answer != null) return Integer.valueOf(answer.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取多配置
     * @param name
     * @return
     */
    public String[] getMultiConfigs(String name) {
        List<String> configs = new ArrayList();
        Iterator iterator = this.propertySources.iterator();

        while(iterator.hasNext()) {
            PropertySource<?> propertySource = (PropertySource)iterator.next();
            if (propertySource.containsProperty(name)) {
                configs.add((String)propertySource.getProperty(name));
            }
        }

        return (String[])configs.toArray(new String[configs.size()]);
    }

    /**
     * 获取所有配置名称
     * @return
     */
    public Enumeration<String> getConfigNames() {
        Hashtable<String, String> hashtable = new Hashtable();
        if (this.defaultPropertySources != null) {
            Iterator iterator = this.defaultPropertySources.iterator();

            while(iterator.hasNext()) {
                ResourcePropertySource source = (ResourcePropertySource)iterator.next();
                String[] strings;
                int length = (strings = source.getPropertyNames()).length;

                for(int i = 0; i < length; ++i) {
                    String name = strings[i];
                    hashtable.put(name, "");
                }
            }
        }

        String[] strings;
        int length = (strings = this.mainPropertySource.getPropertyNames()).length;

        for(int i = 0; i < length; ++i) {
            String name = strings[i];
            hashtable.put(name, "");
        }

        return hashtable.keys();
    }
}
