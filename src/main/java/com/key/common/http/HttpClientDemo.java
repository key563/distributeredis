package com.key.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * HttpClient 请求调用demo
 */
public class HttpClientDemo {
    public static void main(String[] args) {
        int nn = 16;
        int MAXIMUM_CAPACITY = 1 << 30;
        int n = 16 - 1;
        System.out.println(n);
        n |= n >>> 1;
        System.out.println(n);
        n |= n >>> 2;
        System.out.println(n);
        n |= n >>> 4;
        System.out.println(n);
        n |= n >>> 8;
        System.out.println(n);
        n |= n >>> 16;
        System.out.println(n);
        int m = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        System.out.println(m);
        HttpClientDemo test = new HttpClientDemo();
//        test.hashMapTest();
    }

    public void hashMapTest() {
        HashMap<Integer, Object> map = new HashMap<>(18);
        for (int i = 100; i < 117; i++) {
            map.put(i, "test_" + i);
        }
        System.out.println(map);
        keySetKey(map);
        keySetValue(map);
        entryKeySet(map);
        iterator(map);
        lambda(map);
    }

    /**
     * 获取keySet
     *
     * @param map
     */
    public void keySetKey(HashMap<Integer, Object> map) {
        System.out.println(" 获取keySet");
        for (Integer key : map.keySet()) {
            System.out.println(key + " : " + map.get(key));
        }
    }

    /**
     * 获取values
     *
     * @param map
     */
    public void keySetValue(HashMap map) {
        System.out.println(" 获取values");
        for (Object value : map.values()) {
            System.out.println(value);
        }
    }

    /**
     * entrySet 获取key and value
     *
     * @param map
     */
    public void entryKeySet(HashMap<Integer, Object> map) {
        System.out.println(" 获取entrySet");
        for (Map.Entry<Integer, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * Iterator entrySet 获取key and value
     *
     * @param map
     */
    public void iterator(HashMap<Integer, Object> map) {
        System.out.println(" 获取Iterator entrySet");
        Iterator<Map.Entry<Integer, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Object> entry = iterator.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * Lambda 获取key and value
     *
     * @param map
     */
    public void lambda(HashMap<Integer, Object> map) {
        System.out.println(" 获取Lambda");
        map.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
    }

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
        httpClient.addPostParam("name", "zhangsan");
//        httpClient.postParams(new HashMap<>());

        // 添加请求体 body
        // 请求体参数可以是一个值或一个对象，如果是对象最终会转为json字符串提交。
        // 请求体参数body与表单参数postParams，不能同时添加，如果同时添加了两个参数最终只会发送请求体参数body。
        httpClient.body(new Object());

        // 发送请求并返回响应结果
        RestfulHttpClient.HttpResponse response = httpClient.request();
        // 根据状态码判断请求是否成功
        if (response.getCode() == 200) {
            // 获取响应内容
            String content = response.getContent();
        }

        //


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

//    public void trustManager() throws Exception {
//        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//        TrustManager[] tm = {new MyX509TrustManager()};
//        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//        sslContext.init(null, tm, new java.security.SecureRandom());
//        // 从上述SSLContext对象中得到SSLSocketFactory对象
//        SSLSocketFactory ssf = sslContext.getSocketFactory();
//        // 创建URL对象
//        URL myURL = new URL("https://ebanks.gdb.com.cn/sperbank/perbankLogin.jsp");
//        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
//        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
//        httpsConn.setSSLSocketFactory(ssf);
//        // 取得该连接的输入流，以读取响应内容
//        InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
//        // 读取服务器的响应内容并显示
//        int respInt = insr.read();
//        while (respInt != -1) {
//            System.out.print((char) respInt);
//            respInt = insr.read();
//        }
//    }

}
