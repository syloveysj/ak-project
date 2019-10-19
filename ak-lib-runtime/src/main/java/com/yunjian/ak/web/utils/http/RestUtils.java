package com.yunjian.ak.web.utils.http;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.ioc.ApplicationContextManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/3 10:51
 * @Version 1.0
 */
public class RestUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtils.class);
    private static int MAX_TOTAL_CONNECTIONS = 400;
    private static int DEFAULT_MAX_PERROUTER = 200;
    public static int REQUEST_TIMEOUT = 3000;
    public static int SOCKET_TIMEOUT = 30000;
    public static int CONNECTION_TIMEOUT = 5000;
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }};
    private static final PoolingHttpClientConnectionManager connectionManager;
    private static final PoolingHttpClientConnectionManager highPriorityConnectionManager;

    static {
        initConfiguration();
        RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create();
        builder.register("http", PlainConnectionSocketFactory.getSocketFactory());

        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
            LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            builder.register("https", sslsf);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("Http连接池SSLConnectionSocketFactory初始化失败，将会无法调用远程https服务，错误原因：", e);
        } catch (KeyManagementException ex) {
            LOGGER.warn("Http连接池SSLConnectionSocketFactory初始化失败，将会无法调用远程https服务，错误原因：", ex);
        }

        connectionManager = new PoolingHttpClientConnectionManager(builder.build());
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PERROUTER);
        RegistryBuilder<ConnectionSocketFactory> hpBuilder = RegistryBuilder.create();
        hpBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());

        try {
            SSLContext hpSslContext = SSLContext.getInstance("TLSv1");
            hpSslContext.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
            LayeredConnectionSocketFactory hpSslsf = new SSLConnectionSocketFactory(hpSslContext);
            hpBuilder.register("https", hpSslsf);
        } catch (NoSuchAlgorithmException var4) {
            LOGGER.warn("HighPriorityHttp连接池SSLConnectionSocketFactory初始化失败，将会无法调用远程https服务，错误原因：", var4);
        } catch (KeyManagementException var5) {
            LOGGER.warn("HighPriorityHttp连接池SSLConnectionSocketFactory初始化失败，将会无法调用远程https服务，错误原因：", var5);
        }

        highPriorityConnectionManager = new PoolingHttpClientConnectionManager(builder.build());
        highPriorityConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        highPriorityConnectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PERROUTER);
    }

    public RestUtils() {
    }

    public static void setMaxTotal(int maxTotal) {
        connectionManager.setMaxTotal(maxTotal);
    }

    private static void initConfiguration() {
        ConfigManager manager = ConfigManager.getInstance();
        if (manager.getConfig("httpclient_max_connection_num") != null) {
            MAX_TOTAL_CONNECTIONS = Integer.parseInt(manager.getConfig("httpclient_max_connection_num"));
        }

        if (manager.getConfig("httpclient_default_perroute_num") != null) {
            DEFAULT_MAX_PERROUTER = Integer.parseInt(manager.getConfig("httpclient_default_perroute_num"));
        }

        if (manager.getConfig("httpclient_socket_timeout") != null) {
            SOCKET_TIMEOUT = Integer.parseInt(manager.getConfig("httpclient_socket_timeout"));
        }

        if (manager.getConfig("httpclient_request_timeout") != null) {
            REQUEST_TIMEOUT = Integer.parseInt(manager.getConfig("httpclient_request_timeout"));
        }

        if (manager.getConfig("httpclient_connection_timeout") != null) {
            CONNECTION_TIMEOUT = Integer.parseInt(manager.getConfig("httpclient_connection_timeout"));
        }

    }

    public static void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
    }

    public static CloseableHttpClient getHttpClient() {
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
        return httpClient;
    }

    public static CloseableHttpClient getHighPriorityHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(highPriorityConnectionManager).setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    public static CloseableHttpClient getCustomHttpClient(RequestConfig config) {
        return config == null ? HttpClients.custom().setConnectionManager(connectionManager).build() : HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
    }

    protected static ClientHttpRequestFactory createStatefullHttpRequestFactory(HttpContext context) {
        return new StatefullHttpComponentsClientHttpRequestFactory(getHttpClient(), context);
    }

    protected static ClientHttpRequestFactory createStatefullHighPriorityHttpRequestFactory(HttpContext context) {
        return new StatefullHttpComponentsClientHttpRequestFactory(getHighPriorityHttpClient(), context);
    }

    protected static ClientHttpRequestFactory createCustomHttpRequestFactory(RequestConfig config) {
        return new StatefullHttpComponentsClientHttpRequestFactory(getCustomHttpClient(config), (HttpContext)null);
    }

    public static RestTemplate createHighPriorityStatefullRestTemplate(HttpContext context) {
        RestTemplate restTemplate = new RestTemplate(createStatefullHighPriorityHttpRequestFactory(context));
        setRestTemplate(restTemplate);
        return restTemplate;
    }

    public static RestTemplate createHighPriorityRestTemplate() {
        RestTemplate restTemplate = createHighPriorityStatefullRestTemplate((HttpContext)null);
        restTemplate.setErrorHandler(new CommonErrorHandler());
        return restTemplate;
    }

    public static RestTemplate createStatefullRestTemplate(HttpContext context) {
        RestTemplate restTemplate = new RestTemplate(createStatefullHttpRequestFactory(context));
        setRestTemplate(restTemplate);
        restTemplate.setErrorHandler(new CommonErrorHandler());
        return restTemplate;
    }

    public static RestTemplate createRestTemplate() {
        RestTemplate restTemplate = createStatefullRestTemplate((HttpContext)null);
        return restTemplate;
    }

    public static RestTemplate createCustomConfigRestTemplate(RequestConfig config) {
        RestTemplate restTemplate = new RestTemplate(createCustomHttpRequestFactory(config));
        setRestTemplate(restTemplate);
        restTemplate.setErrorHandler(new CommonErrorHandler());
        return restTemplate;
    }

    private static void setRestTemplate(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        HttpMessageConverter<?> jsonMessageConverter = (HttpMessageConverter) ApplicationContextManager.getContext().getBean("jsonMessageConverter");
        messageConverters.add(jsonMessageConverter);
        restTemplate.setMessageConverters(messageConverters);

        // 保存和传递调用链上下文
//        if(StringUtils.equalsIgnoreCase("true", ConfigManager.getInstance().getConfig("ak.cat.enable"))) {
//            restTemplate.setInterceptors(Collections.singletonList(new CatRestInterceptor()));
//        }
    }

    public static String genBasicAuth(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        return new String(Base64.encodeBase64(plainCredsBytes));
    }
}
