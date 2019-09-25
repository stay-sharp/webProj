package com.ruiyang.du.utils;

import com.ruiyang.du.bo.HttpClientResult;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class HttpClientUtils {


    private static final String CHARSET_UTF8 = "utf-8";
    private static final int CONN_TIME_OUT = 5000;
    private static final int READ_TIME_OUT = 5000;

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static volatile CloseableHttpClient httpClient = null;
    private static final Object SYNC_LOCK = new Object();

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doGet(String url, int connTimeout, int readTimeout, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(readTimeout).build();
        httpGet.setConfig(requestConfig);
        // 设置请求头
        packageHeader(headers, httpGet);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求并获得响应结果
            return requestAndGetResult(getHttpClient(), httpGet);
        } finally {
            // 释放资源
            release(httpResponse);
        }
    }

    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url         请求地址
     * @param connTimeout
     * @param readTimeout
     * @param headers     请求头集合
     * @param params      请求参数集合字符串
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, int connTimeout, int readTimeout, Map<String, String> headers, String params) throws Exception {
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(readTimeout).build();
        httpPost.setConfig(requestConfig);
        // 设置请求头
        packageHeader(headers, httpPost);
        // 封装请求参数
        httpPost.setEntity(new StringEntity(params, CHARSET_UTF8));
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求并获得响应结果
            return requestAndGetResult(getHttpClient(), httpPost);
        } catch (Exception e) {
            logger.error("doPost() 请求外部接口异常:", e);
            return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            // 释放资源
            release(httpResponse);
        }
    }
    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url         请求地址
     * @param connTimeout
     * @param readTimeout
     * @param headers     请求头集合
     * @param params      请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPost(String url, int connTimeout, int readTimeout, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(readTimeout).build();
        httpPost.setConfig(requestConfig);
        // 设置请求头
        packageHeader(headers, httpPost);
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            // 设置到请求的http对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, CHARSET_UTF8));
        }
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求并获得响应结果
            return requestAndGetResult(getHttpClient(), httpPost);
        } catch (Exception e) {
            logger.error("doPost() 请求外部接口异常:", e);
            return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            // 释放资源
            release(httpResponse);
        }
    }


    /**
     * Description: 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 请求并获得响应结果
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    private static HttpClientResult requestAndGetResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        // 执行请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpMethod);
        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF8);
            }
            return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
        }
        logger.warn("requestAndGetResult() 响应为空");
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Description: 释放资源
     *  @param httpResponse
     *
     */
    private static void release(CloseableHttpResponse httpResponse) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
    }

    private static CloseableHttpClient getHttpClient(){
        if (httpClient == null) {
            synchronized (SYNC_LOCK) {
                if (httpClient == null) {
                    httpClient = createHttpClient();
                }
            }
        }
        return httpClient;
    }

    private static CloseableHttpClient createHttpClient(){
        // 创建httpClient对象
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(5);
        connManager.setMaxTotal(20);
        HttpClientBuilder custom = HttpClientBuilder.create();
        custom.setConnectionManager(connManager);
        custom.setConnectionTimeToLive(30, TimeUnit.SECONDS);
        return custom.build();
    }

}
