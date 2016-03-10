package com.hahazql.tools.msg.mq.rocketmq;/**
 * Created by zql on 16/3/9.
 */

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.hahazql.tools.helper.LogMgr;
import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.MessageRecognizer;
import com.hahazql.tools.msg.mq.IMQClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by zql on 16/3/9.
 *
 * @className RocketClient
 * @classUse
 */
public class RocketClient implements IMQClient<RocketMQParam, RocketMQProducer, RocketMQConsumer> {
    /**
     * 创建生产者
     *
     * @param param 生产者参数
     * @return
     * @throws MQClientException
     */
    public RocketMQProducer createProducer(RocketMQParam param) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(param.getProducerGroup());
        producer.start();
        RocketMQProducer ret = new RocketMQProducer(producer, param);
        return ret;
    }

    /**
     * 绑定消费者
     *
     * @param param      消费者参数
     * @param consumer   消费者实例
     * @param recognizer 消息解析器
     * @return
     * @throws IOException
     * @throws TimeoutException
     * @throws MQClientException
     */
    public RocketMQConsumer bindConsumer(RocketMQParam param, final RocketMQConsumer consumer, final MessageRecognizer recognizer) throws IOException, TimeoutException, MQClientException {
        return getPushConsumer(param,consumer,recognizer);
    }


    /**
     * 获取push模式的消费者
     * @param param
     * @param consumer
     * @param recognizer
     * @return
     * @throws MQClientException
     */
    private RocketMQConsumer getPushConsumer(RocketMQParam param, final RocketMQConsumer consumer, final MessageRecognizer recognizer) throws MQClientException {
        DefaultMQPushConsumer rocketConsumer = new DefaultMQPushConsumer(param.getProducerGroup());
        rocketConsumer.setNamesrvAddr(param.getAddr());
        //订阅消息
        rocketConsumer.subscribe(param.getTopic(),param.getTag());
        //设置读取方式(从队列头)
        rocketConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //注册消息监听器
        rocketConsumer.registerMessageListener(
                new MessageListenerConcurrently() {
                    public ConsumeConcurrentlyStatus consumeMessage(
                            List<MessageExt> list,
                            ConsumeConcurrentlyContext Context) {
                        for (MessageExt msg : list) {
                            IMessage message;
                            try {
                                message = recognizer.recognizer(msg.getBody());
                            } catch (Exception e) {
                                LogMgr.error(RocketClient.class, "consumer监听器解析消息失败");
                                break;
                            }
                            consumer.recMsg(message);
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                }
        );
        rocketConsumer.start();
        consumer.setConsumer(rocketConsumer);
        return consumer;
    }
}
