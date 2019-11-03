package com.yunjian.ak.api.proxy;

import com.yunjian.ak.config.AkConfigProperties;
import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.config.properties.sso.Proxy;
import com.yunjian.ak.context.AppOperationContext;
import com.yunjian.ak.context.OperationContextHolder;
import com.yunjian.ak.ioc.ApplicationContextManager;
import com.yunjian.ak.web.utils.http.RestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/3 10:31
 * @Version 1.0
 */
@Controller
@RequestMapping({"/proxy"})
public class ProxyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyController.class);

    private static Proxy proxy;
    private static boolean valid = true;
    private static Pattern pattern = null;
    private static HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    private static Set<String> EXCLUDED_REQUEST_HEADERS;
    private static Set<String> EXCLUDED_RESPONSE_HEADERS;

    static {
        try {
            AkConfigProperties akConfigProperties = ApplicationContextManager.getContext().getBean(AkConfigProperties.class);
            proxy = akConfigProperties.getSso().getProxy();
            factory.setHttpClient(RestUtils.getHttpClient());
            pattern = Pattern.compile(proxy.getApigateway().getPattern());

            if (StringUtils.isEmpty(proxy.getApigateway().getHost())) {
                LOGGER.error("API网关的HOST配置为空，API代理将无法正常工作");
                valid = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("初始化API网关参数报错,初始化失败.");
            valid = false;
        }

        if (valid) {
            LOGGER.info("API网关代理初始化完成," +
                    " apikey:" + proxy.getApigateway().getApikey() +
                    ", schema:" + proxy.getApigateway().getSchema() +
                    ", host:" + proxy.getApigateway().getHost() +
                    ", port:" + proxy.getApigateway().getPort());
        }

        EXCLUDED_REQUEST_HEADERS = new HashSet();
        EXCLUDED_REQUEST_HEADERS.add("cookie");
        EXCLUDED_REQUEST_HEADERS.add("authorization");
        EXCLUDED_REQUEST_HEADERS.add("host");
        EXCLUDED_REQUEST_HEADERS.add(AppOperationContext.AK_TENANT_SCHEME.toLowerCase());
        EXCLUDED_REQUEST_HEADERS.add(AppOperationContext.AK_USER_ID.toLowerCase());
        EXCLUDED_REQUEST_HEADERS.add(AppOperationContext.AK_LOGIN_NAME.toLowerCase());
        EXCLUDED_REQUEST_HEADERS.add(AppOperationContext.AK_SESSION_ID.toLowerCase());

        EXCLUDED_RESPONSE_HEADERS = new HashSet();
        EXCLUDED_RESPONSE_HEADERS.add("transfer-encoding");
        EXCLUDED_RESPONSE_HEADERS.add("set-cookie");
    }

    public ProxyController() {
    }

    @RequestMapping({"/**"})
    public void proxy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!valid) {
            response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "API代理不可用，请联系管理员检查配置");
        } else {
            URI uri;
            try {
                uri = this.resolveRemoteUri(request);
            } catch (Exception e) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
                return;
            }

            ClientHttpRequest newRequest = factory.createRequest(uri, HttpMethod.valueOf(request.getMethod()));
            this.handleRequestHeaders(request, newRequest.getHeaders());

            if(StringUtils.startsWithIgnoreCase(request.getHeader("Content-Type"), MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                List<NameValuePair> parameters = new ArrayList<>();
                Map<String, String[]> data = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : data.entrySet()) {
                    String name = entry.getKey();
                    String[] values = entry.getValue();
                    for(String value : values) {
                        parameters.add(new BasicNameValuePair(name, value));
                    }
                }
                String body = URLEncodedUtils.format(parameters, Charset.forName("UTF-8"));
                if(StringUtils.isNotEmpty(body)) {
                    InputStream requestBody = new ByteArrayInputStream(body.getBytes());
                    IOUtils.copy(requestBody, newRequest.getBody());
                }
            } else {
                InputStream requestBody = null;
                if ((requestBody = request.getInputStream()) != null) {
                    IOUtils.copy(requestBody, newRequest.getBody());
                }
            }

            ClientHttpResponse remoteResponse = null;

            try {

                if(StringUtils.equalsIgnoreCase("true", ConfigManager.getInstance().getConfig("ak.cat.enable"))) {
                    // 添加Cat调用链监控
//                    Transaction t = Cat.newTransaction(CatConstants.TYPE_CALL, newRequest.getURI().toString());
//                    try {
//                        HttpHeaders headers = newRequest.getHeaders();

                        // 保存和传递CAT调用链上下文
//                        Cat.Context ctx = new CatContext();
//                        Cat.logRemoteCallClient(ctx);
//                        headers.add(CatHttpConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID, ctx.getProperty(Cat.Context.ROOT));
//                        headers.add(CatHttpConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID, ctx.getProperty(Cat.Context.PARENT));
//                        headers.add(CatHttpConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID, ctx.getProperty(Cat.Context.CHILD));

                        // 请求继续被执行
                        remoteResponse = newRequest.execute();

//                        t.setStatus(Transaction.SUCCESS);
//                    } catch (Exception e) {
//                        Cat.getProducer().logError(e);
//                        t.setStatus(e);
//                        throw e;
//                    } finally {
//                        t.complete();
//                    }
                } else {
                    remoteResponse = newRequest.execute();
                }

            } catch (Exception e) {
                if (e instanceof HttpClientErrorException) {
                    LOGGER.error("请求【" + uri + "】发生异常,异常信息为:" + ((HttpClientErrorException)e).getResponseBodyAsString());
                } else if (e instanceof HttpServerErrorException) {
                    LOGGER.error("请求【" + uri + "】发生异常,异常信息为:" + ((HttpServerErrorException)e).getResponseBodyAsString());
                }

                e.printStackTrace();
            }

            response.setStatus(remoteResponse.getStatusCode().value());
            this.handleResponseHeaders(remoteResponse.getHeaders(), response);
            InputStream resultBody = null;
            if ((resultBody = remoteResponse.getBody()) != null) {
                IOUtils.copy(resultBody, response.getOutputStream());
            }

        }
    }

    private URI resolveRemoteUri(HttpServletRequest request) throws Exception {
        String uriStr = this.getRemoteAPIUri(request);
        if (StringUtils.isEmpty(uriStr)) {
            LOGGER.error("请求:" + request.getRequestURI() + "截取远程地址失败.");
            throw new RuntimeException();
        } else {
            try {
                String queryStr = request.getQueryString();
                if (StringUtils.isNotBlank(queryStr)) {
                    queryStr = URLDecoder.decode(request.getQueryString(), "UTF-8");
                }

                URI uri = new URI(proxy.getApigateway().getSchema(),
                        null,
                        proxy.getApigateway().getHost(),
                        proxy.getApigateway().getPort(),
                        uriStr, queryStr, null);
                return uri;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private String getRemoteAPIUri(HttpServletRequest request) {
        String path = request.getRequestURI();
        Matcher matcher = pattern.matcher(path);
        return matcher.find() ? matcher.group(2) : null;
    }

    private void handleRequestHeaders(HttpServletRequest request, HttpHeaders targetHeaders) {
        Enumeration names = request.getHeaderNames();

        while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            if (!EXCLUDED_REQUEST_HEADERS.contains(name.toLowerCase())) {
                targetHeaders.add(name, request.getHeader(name));
            }
        }

        if (StringUtils.isNotEmpty(proxy.getApigateway().getApikey())) {
            targetHeaders.set("X-API-KEY", proxy.getApigateway().getApikey());
        }

        AppOperationContext context = (AppOperationContext) OperationContextHolder.getContext();
        targetHeaders.add(AppOperationContext.AK_TENANT_SCHEME, context.getTenantScheme());
        targetHeaders.add(AppOperationContext.AK_USER_ID, context.getUserId());
        targetHeaders.add(AppOperationContext.AK_LOGIN_NAME, context.getLoginName());
        targetHeaders.add(AppOperationContext.AK_SESSION_ID, context.getSessionId());

    }

    private void handleResponseHeaders(HttpHeaders remoteHeaders, HttpServletResponse response) {
        Set<Map.Entry<String, List<String>>> entries = remoteHeaders.entrySet();
        Iterator iterator = entries.iterator();

        while(true) {
            Map.Entry entry;
            String name;
            do {
                if (!iterator.hasNext()) {
                    return;
                }

                entry = (Map.Entry)iterator.next();
                name = (String)entry.getKey();
            } while(EXCLUDED_RESPONSE_HEADERS.contains(name.toLowerCase()));

            Iterator values = ((List)entry.getValue()).iterator();

            while(values.hasNext()) {
                String value = (String)values.next();
                response.addHeader(name, value);
            }
        }
    }
}
