package com.yunjian.ak.web.utils.http;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.context.OperationContextHolder;
import com.yunjian.ak.context.WebOperationContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/3 10:55
 * @Version 1.0
 */
class StatefullHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatefullHttpComponentsClientHttpRequestFactory.class);
    private final HttpContext httpContext;
    private static Boolean isApikeyEnabled = null;
    private static String apiKey = null;

    public StatefullHttpComponentsClientHttpRequestFactory(HttpClient httpClient, HttpContext httpContext) {
        super(httpClient);
        this.httpContext = httpContext;
    }

    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        return this.httpContext;
    }

    protected void postProcessHttpRequest(HttpUriRequest request) {
        try {
            if (isApikeyEnabled == null) {
                apiKey = ConfigManager.getInstance().getConfig("ak_sso_proxy_apigateway_apikey");
                if (StringUtils.isEmpty(apiKey)) {
                    apiKey = ConfigManager.getInstance().getConfig("ak.sso.proxy.apigateway.apikey");
                }

                isApikeyEnabled = !StringUtils.isEmpty(apiKey);
            }

            if (isApikeyEnabled) {
//                request.addHeader("X-API-KEY", apiKey);
//                this.synRequestFluentInfo(request);

//                User user = (User)((WebOperationContext)OperationContextHolder.getContext()).getUser();
//                if (user != null && user.size() != 0) {
//                    Map<String, String> userMap = new HashMap();
//                    userMap.put("un", user.getRealname());
//                    userMap.put("uid", user.getId());
//                    String code = Base64.encodeBase64String(JSON.toJSONString(userMap).getBytes(Charset.forName("UTF-8")));
//                    request.addHeader("X-API-OPERATOR", code);
//                    LOGGER.debug("Request初始化完成:" + request.getURI().toString() + ",ApiKey:" + apiKey, "Operator:" + userMap.toString());
//                }
            }
        } catch (Exception var5) {
        }

        super.postProcessHttpRequest(request);
    }

    private void synRequestFluentInfo(HttpUriRequest request) {
        HttpServletRequest currentRequest = ((WebOperationContext) OperationContextHolder.getContext()).getRequest();
        if (currentRequest != null) {
            String sn = currentRequest.getHeader("X-REG-SN");
            if (sn != null) {
                request.addHeader("X-REG-SN", sn);
                LOGGER.debug("Request:" + request.getURI().toString() + ",流水号:" + sn);
            }

            String apiId = currentRequest.getHeader("X-API-ID");
            if (apiId != null) {
                request.addHeader("X-API-ID", apiId);
                LOGGER.debug("Request:" + request.getURI().toString() + "APIID:" + apiId);
            }
        }

    }
}
