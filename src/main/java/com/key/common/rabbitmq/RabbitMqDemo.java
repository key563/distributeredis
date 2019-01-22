package com.key.common.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitMqDemo {

    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        RabbitMqDemo demo = new RabbitMqDemo();
        String exchangeName = "exchangeDemoName";
        String routingKey = "routingKey";
        String direct = "direct";
        Connection connection = demo.getConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName,direct,true);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,exchangeName,routingKey);
        channel.close();
        connection.close();

    }

    public Connection getConnection() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.204.129");
        connectionFactory.setPort(15672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
//        connectionFactory.setVirtualHost("");
        connectionFactory.setUri("amqp://userName:password@hostName:portNumber/virtualHost");

        return connectionFactory.newConnection();
    }

    public void closeConnection(){

    }
}
