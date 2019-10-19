package com.yunjian.ak.web.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/3 10:56
 * @Version 1.0
 */
class CommonErrorHandler extends DefaultResponseErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonErrorHandler.class);

    CommonErrorHandler() {
    }

    public void handleError(ClientHttpResponse response) throws IOException {
        try {
            super.handleError(response);
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException) {
                LOGGER.error("请求发生异常,异常信息为:" + ((HttpClientErrorException)e).getResponseBodyAsString());
                throw (HttpClientErrorException)e;
            } else if (e instanceof HttpServerErrorException) {
                LOGGER.error("请求发生异常,异常信息为:" + ((HttpServerErrorException)e).getResponseBodyAsString());
                throw (HttpServerErrorException)e;
            } else {
                throw (RestClientException)e;
            }
        }
    }
}
