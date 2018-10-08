package com.key.common.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.key.modules.sys.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpEntityDemo {

    public static void main(String[] args) throws UnsupportedEncodingException, JsonProcessingException {
        HttpEntityDemo demo = new HttpEntityDemo();
        demo.stringEntity();
    }

    public void basicHttpEntity() throws IOException {
        // 代表底层流的基本实体，通常是在http报文中获取的实体，输入流的内容包装类
        // 只有一个空参的构造方法，刚创建时没有内容，长度为-1L，需要通过两个方法，把值赋进去。
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream is = null;
        // 设置Content，类型为数据流
        entity.setContent(is);
        // 设置长度
        entity.setContentLength(is.available());
        // 设置数据格式和请求头
        entity.setContentEncoding("");
        entity.setContentEncoding(new BasicHeader("", ""));
        entity.setContentType(new BasicHeader("", ""));

    }

    public void byteArrayEntity() {
        // Content为字节数组
        String content = "内容多大事多";
        ByteArrayEntity entity = new ByteArrayEntity(content.getBytes());
    }

    public void stringEntity() throws UnsupportedEncodingException, JsonProcessingException {
        // 传递字符串参数，可以将json对象，对象等转换为字符串
        String params = "";

        // 1.参数为json格式字符串{"name":"zhangsan","value":"asdasd"}格式
        // Header需要设置：Content-Type=ContentType.APPLICATION_JSON
        Map<String, String> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 11 + "");
        map.put("gender", "male");
        map.put("char", "asda");
        JSONObject jsonObject = new JSONObject(map);
        params = jsonObject.toString();
        System.out.println("json格式参数:" + params);
        StringEntity entity1 = new StringEntity(params, "UTF-8");

        // 可以用此方法将对象快速转为json格式字符串
        User user = new User();
        user.setName("zhangsan");
        user.setAge(23);
        user.setGender(1);
        user.setAddress("asdasd");
        String body = new ObjectMapper().writeValueAsString(user);
        System.out.println(body);


        // 2.参数为urlparam格式字符串,name=zhangsan&value=asdasd格式
        // Header需要设置：Content-Type=ContentType.APPLICATION_FORM_URLENCODED/MULTIPART_FORM_DATA
        params = toUrlParams(map);
        System.out.println("url格式参数:" + params);
        // 创建带字符创参数和字符编码的
        StringEntity entity = new StringEntity(params, "UTF-8");


        // 3.参数为xml字符串,
        // Header需要设置：Content-Type=ContentType.TEXT_XML
        params = getXMLString("2018-09-01", "2018-09-30", "test");
        System.out.println("xml格式参数:" + params);
        StringEntity entity2 = new StringEntity(params, "UTF-8");
    }

    /**
     * UrlEncodedFormEntity表单提交
     */
    public void urlEncodedEntity() throws UnsupportedEncodingException {
        // 类form表单方式提交表单参数
        // Header需要设置：Content-Type=ContentType.APPLICATION_FORM_URLENCODED
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        BasicNameValuePair name = new BasicNameValuePair("name", "zhangsan");
        BasicNameValuePair value = new BasicNameValuePair("value", "asda");
        BasicNameValuePair gender = new BasicNameValuePair("gender", "male");
        pairList.add(name);
        pairList.add(value);
        pairList.add(gender);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList);
    }

    /**
     * 复合型entity方式提交
     */
    public void multipartEntityBuilder() throws FileNotFoundException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addTextBody("name", "zhansgan");
        builder.addTextBody("value", "asdasd");
        // 添加二进制流
        builder.addBinaryBody("file", new FileInputStream(""), ContentType.MULTIPART_FORM_DATA, "image.jpeg");
        // 通过multipart方式添加文件
        File uploadFile = new File("");
        builder.addPart("file2", new FileBody(uploadFile, ContentType.create("image/jpeg"), uploadFile.getName()));
        HttpEntity entity = builder.build();
    }

    /**
     * s
     * 构造xml，并转为字符串
     *
     * @param startTime
     * @param endTime
     * @param reportType
     * @return
     */
    public static String getXMLString(String startTime, String endTime, String reportType) {
        String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        StringBuffer sb = new StringBuffer();
        sb.append(XML_HEADER);
        sb.append("<CountingStatisticsDescription>");
        sb.append("<reportType>");
        sb.append(reportType);
        sb.append("</reportType>");
        sb.append("<timeSpanList>");
        sb.append("<timeSpan>");
        sb.append("<startTime>");
        sb.append(startTime);
        sb.append("</startTime>");
        sb.append("<endTime>");
        sb.append(endTime);
        sb.append("</endTime>");
        sb.append("</timeSpan>");
        sb.append("</timeSpanList>");
        sb.append("</CountingStatisticsDescription>");
        // 返回String格式
        return sb.toString();
    }

    /**
     * 将map数据转为url参数形式，如name=a&value=b
     *
     * @param params
     * @return
     */
    public static String toUrlParams(Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            StringBuffer urlParams = new StringBuffer();
            for (String k : params.keySet()) {
                urlParams.append(k + "=" + params.get(k) + "&");
            }
            if (urlParams.length() > 0) {
                return urlParams.substring(0, urlParams.length() - 1);
            }
        }
        return null;
    }

}
