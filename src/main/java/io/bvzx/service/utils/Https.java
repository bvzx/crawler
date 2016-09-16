package io.bvzx.service.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * todo
 *
 * @author wugaoda
 * @Description: (类职责详细描述, 可空)
 * @ClassName: Https
 * @date 2016年07月01日 14:53
 * @Copyright (c) 2015-2020 by caitu99
 */
public class Https {


    private static HttpClient httpClient = null;

    private static Https httpUtils;

    private static Object lock=new Object();


    public static Https getInstance() {
        if (httpUtils == null) {
            synchronized (lock){
                if (httpUtils ==null){
                    httpUtils = new Https();
                    HttpClientConnectionManager hcManager = new PoolingHttpClientConnectionManager(25000, TimeUnit.SECONDS);
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(1200000)
                            .setConnectTimeout(25000)
                            .setSocketTimeout(120000)
                            .build();
                    httpClient = HttpClientBuilder
                            .create()
                            .setConnectionManager(hcManager)
                            .setDefaultRequestConfig(requestConfig)
                            .build();
                }
            }

        }
        return httpUtils;
    }


    public String doGet(String url, String charset, Map<String,String> paramMap) throws IOException {
        return doGet(jointParam(url,paramMap),charset);
    }

    public String doGet(String url, String charset) throws IOException {
        HttpGet httpGet=new HttpGet(url);
        HttpResponse gp=httpClient.execute(httpGet);
        int state=gp.getStatusLine().getStatusCode();
        if (state!= HttpStatus.SC_OK){
            throw new RuntimeException("连接未成功!");
        }
        return EntityUtils.toString(gp.getEntity());
    }

    public String doPost(String url, String charset,String jsonParam) throws IOException {
        HttpPost httpPost=new HttpPost(url);
        StringEntity stringEntity=new StringEntity(jsonParam);
        stringEntity.setContentEncoding(charset);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        HttpResponse gp=httpClient.execute(httpPost);
        int state=gp.getStatusLine().getStatusCode();
        if (state!= HttpStatus.SC_OK){
            throw new RuntimeException("连接未成功!");
        }
        return EntityUtils.toString(gp.getEntity());
    }


    private String jointParam(String url,Map<String,String> paramMap){
        StringBuffer stringBuffer=new StringBuffer(url);
        stringBuffer.append("?");
        paramMap.entrySet().forEach((singleParam)->{
            stringBuffer.append(singleParam.getKey())
                    .append("=")
                    .append(singleParam.getValue())
                    .append("&");
        });
        int totalLen=stringBuffer.length();
        stringBuffer.setLength(totalLen-1);
        return stringBuffer.toString();
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        Https.httpClient = httpClient;
    }
}