package com.hahazql.tools.msg.mq.rocketmq;/**
 * Created by zql on 16/3/9.
 */

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.MQPullConsumerScheduleService;
import com.hahazql.tools.msg.IMessageExchange;
import com.hahazql.tools.msg.mq.IConsumer;

/**
 * Created by zql on 16/3/9.
 * @className RocketMQConsumer
 * @classUse
 *
 *
 */
public class RocketMQConsumer extends IConsumer
{
    public DefaultMQPushConsumer consumer;

    public RocketMQConsumer(IMessageExchange exchange) {
        super(exchange);
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }
}
