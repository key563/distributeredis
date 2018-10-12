package com.key.common.http;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Base64编码解码工具类
 */
public class Base64Utils {
    private final static Integer CONNECT_TIME_OUT = 6000;
    private final static Integer SOCKET_TIME_OUT = 6000;

    /**
     * 根据图片文件对象进行base64编码
     *
     * @param file 图片url
     * @return 返回编码后字符串
     */
    public static String encodeFromFile(File file) {
        String base64Str = null;
        try {
            FileInputStream is = new FileInputStream(file);
            base64Str = encodeToString(readInputStream(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Str;
    }

    /**
     * 根据流进行base64编码
     *
     * @param inputStream 图片url
     * @return 返回编码后字符串
     */
    public static String encodeFromStream(InputStream inputStream) {
        String base64Str = null;
        try {
            base64Str = encodeToString(readInputStream(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Str;
    }

    /**
     * 根据图片url获取图片的base64字符串
     *
     * @param url 图片url
     * @return 返回编码后字符串
     */
    public static String encodeFromUrl(String url) {
        if (StringUtils.isBlank(url)) {
            System.out.println("图片url为空");
            return null;
        }
        String base64Str = null;
        try {
            base64Str = encodeToString(getByteFromUrl(url));
        } catch (Exception e) {
            System.out.println("根据图片url获取图片base64字符串失败:" + url);
            e.printStackTrace();
        }
        return base64Str;
    }

    /**
     * 根据url获取图片的字节数组
     *
     * @param strUrl 图片url
     * @return 返回byte[]数组
     * @throws Exception
     */
    private static byte[] getByteFromUrl(String strUrl) throws Exception {
        if (StringUtils.isBlank(strUrl)) {
            return null;
        }
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECT_TIME_OUT);
        InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
        byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
        return btImg;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return 返回byte[]数组
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 将byte[]数组base64编码
     *
     * @param data byte[]数组
     * @return 返回编码后字符串
     */
    private static String encodeToString(byte[] data) {
        if (data == null) {
            return null;
        }
        String encode = new BASE64Encoder().encode(data);
        return encode;
    }

    /**
     * base64解码
     *
     * @param srcImageStr base64编码字符串
     * @return 返回解码后的byte[]数组
     */
    public static byte[] decodeToByte(String srcImageStr) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(srcImageStr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * base64解码并生成文件
     *
     * @param srcImageStr base64编码字符串
     * @param srcImage    文件保存路径
     * @return 返回编码和保存结果
     */
    public static boolean decodeToFile(String srcImageStr, File srcImage) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(srcImageStr);
            return byteToFile(b, srcImage);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将字节数组写入文件
     *
     * @param b            byte[]数组
     * @param dstImagePath 文件对象
     * @return 返回编码和保存结果
     */
    private static boolean byteToFile(byte[] b, File dstImagePath) {
        OutputStream out = null;
        try {
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out = new FileOutputStream(dstImagePath);
            out.write(b);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * base64编码（java.util.Base64）
     *
     * @param data byte[]数组
     * @return 返回编码后的字符串
     */
    public static String encodeToString_2(byte[] data) {
        if (data == null) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * base64编码（java.util.Base64）
     *
     * @param data byte[]数组
     * @return 返回编码后的byte[]数组
     */
    private static byte[] encodeToByte_2(byte[] data) {
        if (data == null) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(data);
    }

    /**
     * base64解码（java.util.Base64）
     *
     * @param srcImageStr base64编码字符串
     * @return 返回解码后的byte[]数组
     */
    public static byte[] decodeToByte_2(String srcImageStr) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(srcImageStr);
    }

    /**
     * base64解码并生成文件（java.util.Base64）
     *
     * @param srcImageStr base64编码字符串
     * @param srcImage    文件对象
     * @return base64编码字符串
     */
    public static boolean decodeToFile_2(String srcImageStr, File srcImage) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] b = decoder.decode(srcImageStr);
        return byteToFile(b, srcImage);
    }

    public static void main(String[] args) {
        File file = new File("D:\\temp\\1.jpg");
        String str = Base64Utils.encodeFromFile(file);
        System.out.println(str);
    }
}
