package com.etalk.crm.ApiDemo;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class ApiDemo {
    private static final String account = "N00000023135";//替换为您的账户
    private static final String secret = "f90b0280-3e0f-11e8-b318-57a8debd96b0";//替换为您的api密码
    private static final String host = "http://apis.7moor.com";

    public static void main(String[] args) {
        String time = getDateTime();
        String sig = md5(account + secret + time);
        //查询坐席状态接口
        String interfacePath = "/v20160818/call/dialout/";
        String url = host + interfacePath + account + "?sig=" + sig;
        System.out.println(url);
        String auth = base64(account + ":" + time);
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type","application/json;charset=utf-8");
        post.addHeader("Authorization",auth);
        StringEntity requestEntity;
        //根据需要发送的数据做相应替换
        requestEntity = new StringEntity("{\"FromExten\":\"8038\",\"Exten\":\"18159020751\",\"ExtenType\":\"sip\"}","UTF-8");
        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            System.out.println("the response is : " + EntityUtils.toString(entity,"utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String md5 (String text) {
        return DigestUtils.md5Hex(text).toUpperCase();
    }
    public static String base64 (String text) {
        byte[] b = text.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }
    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
}

