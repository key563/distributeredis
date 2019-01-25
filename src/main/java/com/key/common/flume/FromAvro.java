package com.key.common.flume;


import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;

public class FromAvro {
    private RpcClient client;
    private int port;
    private String hostname;

    public FromAvro(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;

        //创建rpc客户端连接
        this.client = RpcClientFactory.getDefaultInstance(hostname, port);
    }

    //发送event
    public void sendDataToFlume(String data) {
        //创建Event对象
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));

        //发送event
        try {
            client.append(event);
        } catch (EventDeliveryException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        client.close();
    }

    public static void main(String[] args) {
        FromAvro f = new FromAvro(FlumeConstant.PORT, FlumeConstant.HOSTNAME);

        String data1 = "testforflume:this is a test ";
        String data2 = "testforflume2:this is a test ";
        for (int i = 0; i < 50; i++) {
            f.sendDataToFlume(data1 + i);
            f.sendDataToFlume(data2 + i);
        }
        f.cleanUp();
    }
}
