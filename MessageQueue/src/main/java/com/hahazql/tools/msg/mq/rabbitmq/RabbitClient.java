package com.hahazql.tools.msg.mq.rabbitmq;/**
 * Created by zql on 16/3/8.
 */

import com.hahazql.tools.helper.LogMgr;
import com.hahazql.tools.msg.mq.IMQClient;
import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.MessageRecognizer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zql on 16/3/8.
 *
 * @className RabbitClient
 * @classUse
 */
public class RabbitClient implements IMQClient<RabbitMQParam, RabbitMQProducer, RabbitMQConsumer> {
    ConnectionFactory factory;
    private String userName = "guest";
    private String userPassword = "guest";
    private String ip = "localhost";
    private int port = 5672;
    private Connection connection;

    public RabbitClient() throws IOException, TimeoutException {
        initConnectionFactory();
        this.connection = createConnection();
    }


    public RabbitClient(String userName, String userPassword, String ip, int port) throws IOException, TimeoutException {
        this.userName = userName;
        this.userPassword = userPassword;
        this.ip = ip;
        this.port = port;
        initConnectionFactory();
        connection = createConnection();
    }

    /**
     * 初始化连接工厂类
     */
    private void initConnectionFactory() {
        factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(userPassword);
        factory.setHost(ip);
        factory.setPort(port);
    }

    private Connection createConnection() throws IOException, TimeoutException {
        return factory.newConnection();
    }

    /***
     * 根据当前的客户端配置创建新的客户端
     *
     * @return
     * @throws IOException
     * @throws TimeoutException
     * @TODO:每个客户端拥有独立的连接
     */
    public RabbitClient createNewClient() throws IOException, TimeoutException {
        RabbitClient client = new RabbitClient(this.userName, this.userPassword, this.ip, this.port);
        return client;
    }

    /**
     * 为Channel设置参数
     *
     * @param channel
     * @param params
     * @return
     * @throws IOException
     */
    private Channel setParams(final Channel channel, RabbitMQParam params) throws IOException {
        channel.queueDeclare(params.getChannelName(), params.isDurable(), params.isExclusive(), params.isAutoDelete(), null);
        return channel;
    }


    /**
     * 为消息队列创建生产者
     *
     * @param param 生产者参数
     * @return 生产者实例
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMQProducer createProducer(RabbitMQParam param) throws IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel = setParams(channel, param);
        return new RabbitMQProducer(channel,param);
    }

    /**
     * 为客户端绑定一个消费者
     *
     * @param param          消费者参数
     * @param rabbitConsumer 待绑定的消费者实例
     * @param recognizer     消息解析器
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMQConsumer bindConsumer(RabbitMQParam param, final RabbitMQConsumer rabbitConsumer, final MessageRecognizer recognizer) throws IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel = setParams(channel, param);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                IMessage message = null;
                try {
                    message = recognizer.recognizer(body);
                    if (message == null)
                        rabbitConsumer.recMsg(null);
                    else
                        rabbitConsumer.recMsg(message);
                } catch (Exception e) {
                    LogMgr.error(RabbitClient.class, "处理消息时失败,错误信息: " + e.getMessage());
                }
            }
        };
        channel.basicConsume(param.getChannelName(), true, consumer);
        rabbitConsumer.setConsumer(consumer);
        return rabbitConsumer;
    }


}
