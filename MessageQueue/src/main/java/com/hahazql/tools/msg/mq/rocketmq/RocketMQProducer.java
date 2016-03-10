package com.hahazql.tools.msg.mq.rocketmq;/**
 * Created by zql on 16/3/9.
 */

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.hahazql.tools.concurrent.ConcurrentLRUHashMap;
import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.mq.IMQParam;
import com.hahazql.tools.msg.mq.IProducer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zql on 16/3/9.
 * @className RocketMQProducer
 * @classUse
 *
 *
 */
public class RocketMQProducer implements IProducer
{
    private ConcurrentLRUHashMap<Long,SendResult> _ix_result = new ConcurrentLRUHashMap<Long, SendResult>(100);
    private DefaultMQProducer producer;
    private RocketMQParam param;

    public RocketMQProducer()
    {

    }

    public RocketMQProducer(DefaultMQProducer producer,RocketMQParam param)
    {
        this.producer = producer;
        this.param = param;
    }


    public void sendMsg(IMessage msg) throws Exception
    {
        Message message = new Message(param.getTopic(),param.getTag(),msg.getBytes());
        SendResult sendResult = producer.send(message);
        _ix_result.put(msg.getMessageID(),sendResult);
    }

    @Override
    public void setParam(IMQParam param) {
        this.param = (RocketMQParam) param;
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    /**
     * 获取分发状况
     * @param msgID
     * @return
     */
    public SendResult getResult(Long msgID)
    {
        return _ix_result.get(msgID);
    }

}
