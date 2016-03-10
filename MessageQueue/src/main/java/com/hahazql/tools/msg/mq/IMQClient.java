package com.hahazql.tools.msg.mq;/**
 * Created by zql on 16/3/8.
 */

import com.hahazql.tools.msg.MessageRecognizer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by zql on 16/3/8.
 *
 * @param <T> 消息队列的配置类型
 * @param <M> 生产者实例类型
 * @param <N> 消费者实例类型
 * @className IMQClient
 * @classUse 消息队列客户端接口
 */
public interface IMQClient<T extends IMQParam, M extends IProducer, N extends IConsumer> {
    /**
     * 获取生产者
     *
     * @param param 生产者参数
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public M createProducer(T param) throws Exception;

    /**
     * 初始化消费者
     *
     * @param param      消费者参数
     * @param consumer   消费者实例
     * @param recognizer 消息解析器
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public N bindConsumer(T param, N consumer, MessageRecognizer recognizer) throws Exception;


}
