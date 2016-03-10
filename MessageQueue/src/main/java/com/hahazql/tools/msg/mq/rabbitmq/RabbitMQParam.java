package com.hahazql.tools.msg.mq.rabbitmq;/**
 * Created by zql on 16/3/8.
 */

import com.hahazql.tools.msg.mq.IMQParam;
import com.rabbitmq.client.AMQP;

/**
 * Created by zql on 16/3/8.
 *
 * @className RabbitMQParam
 * @classUse
 */
public class RabbitMQParam implements IMQParam {
    private String channelName = "";
    private boolean durable = false;
    private boolean exclusive = false;
    private boolean autoDelete = false;

    private String exchange = "";
    private String routingKey = channelName;
    private AMQP.BasicProperties props = null;

    public RabbitMQParam(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public RabbitMQParam setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public boolean isDurable() {
        return durable;
    }

    public RabbitMQParam setDurable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public RabbitMQParam setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        return this;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public RabbitMQParam setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public AMQP.BasicProperties getProps() {
        return props;
    }

    public void setProps(AMQP.BasicProperties props) {
        this.props = props;
    }
}
