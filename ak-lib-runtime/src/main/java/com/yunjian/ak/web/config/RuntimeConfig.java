package com.yunjian.ak.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.yunjian.ak.plugins.SQLPermissionPlugin;
import com.yunjian.ak.web.filter.DefaultOperationContextHandler;
import com.yunjian.ak.web.filter.OperationContextHandler;
import com.yunjian.ak.web.plugins.impl.DefaultSQLPermissionImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/12 10:10
 * @Version 1.0
 */
@Configuration
public class RuntimeConfig {

    @Bean
    public FastJsonHttpMessageConverter jsonMessageConverter() {
        FastJsonHttpMessageConverter jsonMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, // 空字段保留
                SerializerFeature.WriteNullStringAsEmpty,// list字段如果为null，输出为[]，而不是null
                SerializerFeature.WriteNullNumberAsZero,// 数值字段如果为null，输出为0，而不是null
                SerializerFeature.QuoteFieldNames);// 序列化输出字段，使用引号
        jsonMessageConverter.setFastJsonConfig(config);
        List<MediaType> types = new ArrayList<MediaType>();
        types.add(MediaType.APPLICATION_JSON_UTF8);
        jsonMessageConverter.setSupportedMediaTypes(types);
        jsonMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        return jsonMessageConverter;
    }

    @Bean
    @ConditionalOnMissingBean(OperationContextHandler.class)
    public OperationContextHandler operationContextHandler(){
        return new DefaultOperationContextHandler();
    }

    @Bean
    @ConditionalOnMissingBean(SQLPermissionPlugin.class)
    public SQLPermissionPlugin sqlPermissionPlugin(){
        return new DefaultSQLPermissionImpl();
    }
}
