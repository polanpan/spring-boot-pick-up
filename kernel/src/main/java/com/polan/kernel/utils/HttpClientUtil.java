package com.polan.kernel.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * <p>HTTPClient 工具</p>
 *
 * @author panliyong  2020-04-28 11:16
 */
@Slf4j
public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager poolConnManager = null;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String UTF8 = "UTF-8";
    private static final String GBK = "GBK";
    private static final int MAX_TOTAL_POOL = 200;
    private static final int MAX_PER_ROUTE = 20;
    private static final int SOCKET_TIMEOUT = 10000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;
    private static final int CONNECTION_TIMEOUT = 5000;

    static {
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, PlainConnectionSocketFactory.getSocketFactory())
                    .register(HTTPS, sslFactory)
                    .build();

            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolConnManager.setMaxTotal(MAX_TOTAL_POOL);
            poolConnManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build();
            poolConnManager.setDefaultSocketConfig(socketConfig);
        } catch (Exception e) {
            log.error("http client init pool error ", e);
        }
    }

    /**
     * <p>发送 post 请求</p>
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param heads  请求header
     * @return 请求结果字符串
     * @author panliyong  2020-04-26 17:47
     */
    public static String sendPost(String url, String params, Header... heads) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            StringEntity paramEntity = new StringEntity(params);
            paramEntity.setContentEncoding(UTF8);
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            if (heads != null) {
                httpPost.setHeaders(heads);
            }
            response = getConnection().execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            String result = EntityUtils.toString(response.getEntity());
            log.debug("【HttpClientUtil】sendPost 请求:{} ,请求参数:{},结果:{}", url, params, result);
            if (code == HttpStatus.SC_OK) {
                return result;
            } else {
                log.error("【HttpClientUtil】sendPost 请求:{}返回错误码:{},请求参数:{},结果:{}", url, code, params, result);
                return null;
            }
        } catch (IOException e) {
            log.error("【HttpClientUtil】 sendPost 配置http请求异常", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("【HttpClientUtil】sendPost 配置http请求异常", e);
            }
        }
        return null;
    }

    /**
     * <p>发送 get 请求</p>
     *
     * @param url 请求地址
     * @return 请求结果字符串
     * @author panliyong  2020-04-26 17:47
     */
    public static String sendGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = getConnection().execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                log.debug("【HttpClientUtil】sendGet 请求:{} 返回:{}", url, result);
                return result;
            } else {
                log.error("【HttpClientUtil】sendGet 请求:{} 返回错误码:{},返回结果:{}", url, code, result);
                return null;
            }
        } catch (IOException e) {
            log.error("【HttpClientUtil】sendGet http请求异常，{}", url, e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * <p>获取 client 连接 </p>
     *
     * @author panliyong  2020-04-26 17:49
     */
    private static CloseableHttpClient getConnection() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();
        if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
            log.info("【HttpClientUtil】 now client pool {}", poolConnManager.getTotalStats().toString());
        }
        return httpClient;
    }

}
