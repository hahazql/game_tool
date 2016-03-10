package com.hahazql.tools.msg.mq.rabbitmq;/**
 * Created by zql on 16/3/8.
 */

import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.mq.IMQParam;
import com.hahazql.tools.msg.mq.IProducer;
import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * Created by zql on 16/3/8.
 *
 * @className RabbitMQProducer
 * @classUse
 */
public class RabbitMQProducer implements IProducer {

    private Channel channel;
    private RabbitMQParam param;

    public RabbitMQProducer(Channel channel,IMQParam param) {
        this.channel = channel;
        setParam(param);
    }



    public void sendMsg(IMessage rabbitMessage) throws IOException {
        channel.basicPublish(param.getExchange(), param.getRoutingKey(), param.getProps(), rabbitMessage.getBytes());
    }

    @Override
    public void setParam(IMQParam param) {
        this.param = (RabbitMQParam) param;
    }
}
