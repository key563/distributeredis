package com.key.distributeredis.common.http;

import java.net.HttpURLConnection;

public class MyURLConnectionInitializer implements RestfulHttpClient.URLConnectionInitializer {
    @Override
    public HttpURLConnection init(HttpURLConnection connection, RestfulHttpClient.HttpClient client) {
        // 添加证书

        return connection;
    }
}
