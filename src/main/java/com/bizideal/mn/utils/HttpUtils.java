package com.bizideal.mn.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : liulq
 * @date: 创建时间: 2017/10/23 16:49
 * @version: 1.0
 * @Description:
 */
public class HttpUtils {

    public static JSONObject get(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String res = "{}";
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
        }
        return JSONObject.parseObject(res);
    }

    public static JSONObject postJson(String url, JSONObject json) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = "{}";
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
        }
        return JSONObject.parseObject(res);
    }

    public static JSONObject postForm(String url, Map<String, String> params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> pairList = new ArrayList<>();
        if (null != params && !params.isEmpty()) {
            params.entrySet().forEach(entry -> {
                pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            });
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = "{}";
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
        }
        return JSONObject.parseObject(res);
    }
}
