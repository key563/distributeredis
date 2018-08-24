package com.key.distributeredis.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * HttpClient 请求调用demo
 */
public class HttpClientDemo {

    public void UrlDemo() throws IOException {
        // 建立URL连接的基本操作
        URL url = new URL("http://test.npacn.com:8082/");
        URLConnection connection = url.openConnection();

        //从url中获取数据的操作
        //1.读文件流
        InputStream is = connection.getInputStream();
//        InputStream is2 = url.openStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer bs = new StringBuffer();
        String s = null;
        while ((s = br.readLine()) != null) {
            bs.append(s).append("\n");
        }
        System.out.println(bs.toString());

    }

    public void restfulHttpClientDemo() throws IOException {
        String url = "http://localhost:8080/key/getDemo/{id}";

        // 创建客户端
        RestfulHttpClient.HttpClient httpClient = RestfulHttpClient.getClient(url);

        // 设置请求方式，restful风格方式有：get,post,put,delete,patch，默认get请求
        httpClient.get();
        // 设置请求头
        // 1,全局默认请求头
        RestfulHttpClient.setDefaultHeaders(new HashMap<>());
        // 2.请求客户端中设置
        httpClient.addHeader("Content-Type", "application/json");
        httpClient.addHeaders(new HashMap<>());

        // 添加路径参数，pathParams
        // 这是restful风格中特殊的用法，与后台接收的restful风格接口的方式有点类似
        httpClient.addPathParam("id", "200");
//        httpClient.pathParams();

        // 添加请求参数，queryParams
        // 即url中?.&等拼接的参数
        httpClient.addQueryParam("page", "1");
//        httpClient.queryParams();
        // 添加表单参数，postParams
        httpClient.addPostParam("name","zhangsan");
//        httpClient.postParams(new HashMap<>());

        // 添加请求体 body
        // 请求体参数可以是一个值或一个对象，如果是对象最终会转为json字符串提交。
        // 请求体参数body与表单参数postParams，不能同时添加，如果同时添加了两个参数最终只会发送请求体参数body。
        httpClient.body(new Object());

        // 发送请求并返回响应结果
        RestfulHttpClient.HttpResponse response = httpClient.request();
        // 根据状态码判断请求是否成功
        if(response.getCode() == 200){
            // 获取响应内容
            String content = response.getContent();
        }

        //发送get请求获取数据
        try {
            RestfulHttpClient.HttpResponse httpResponse = RestfulHttpClient
                    .getClient(url)
                    .addPathParam("id", "20")
                    .request();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 发送put请求，提交数据
        try {
            RestfulHttpClient.HttpResponse httpResponse = RestfulHttpClient
                    .getClient(url)
                    .put()
                    .addPathParam("id", "20")
                    .body(null)
                    .request();

            if (httpResponse.getCode() == 200) {
                String content = httpResponse.getContent();
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
