package io.bvzx.service.spider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/9/14.
 */
public abstract class AbstractSpider implements Spider {

    private Object result;
    private static HttpClient httpClient;

    public StringBuffer readHttpResponse(HttpResponse httpResponse, String charset) throws IOException {
        HttpEntity httpEntity = httpResponse.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), charset));

        StringBuffer orginHtml = new StringBuffer();
        String readTemp = null;
        while ((readTemp = reader.readLine()) != null) {
            orginHtml.append(readTemp);
            System.out.println(readTemp);//log
        }

        reader.close();
        return orginHtml;
    }

    public HttpClient getClient() {
        if (httpClient==null){
            HttpClientConnectionManager hcManager = new PoolingHttpClientConnectionManager(1000, TimeUnit.SECONDS);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();

            //  .setProxy(new HttpHost("127.0.0.1",8080))//设置代理
            httpClient = HttpClientBuilder
                    .create()
                    .setConnectionManager(hcManager)
                    .setDefaultRequestConfig(requestConfig)
                    //  .setProxy(new HttpHost("127.0.0.1",8080))//设置代理
                    .build();

        }
        return httpClient;
    }


    @Override
    public Object getResult() {
        return result;
    }
}
