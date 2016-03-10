package com.hahazql.tools.msg.mq.rabbitmq;/**
 * Created by zql on 16/3/8.
 */

import com.hahazql.tools.msg.mq.IConsumer;
import com.hahazql.tools.msg.IMessageExchange;
import com.rabbitmq.client.Consumer;

/**
 * Created by zql on 16/3/8.
 *
 * @className RabbitMQConsumer
 * @classUse
 */
public class RabbitMQConsumer extends IConsumer {
    private Consumer consumer;

    public RabbitMQConsumer(IMessageExchange exchange) {
        super(exchange);
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
