package com.hahazql.tools.msg.mq;/**
 * Created by zql on 16/3/7.
 */


import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.IMessageExchange;

/**
 * Created by zql on 16/3/7.
 *
 * @className IConsumer
 * @classUse 消息队列的消费者
 */
public abstract class IConsumer {
    private IMessageExchange exchange;

    public IConsumer(IMessageExchange exchange) {
        this.exchange = exchange;
    }

    public void recMsg(IMessage message) {
        exchange.recMsg(message);
    }
}
