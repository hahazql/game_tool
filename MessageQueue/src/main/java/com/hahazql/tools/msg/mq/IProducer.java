package com.hahazql.tools.msg.mq;/**
 * Created by zql on 16/3/7.
 */

import com.hahazql.tools.msg.IMessage;

/**
 * @interface IProducer
 * @interfaceUse 生产者的接口类
 */
public interface IProducer {

    /**
     * 发送消息
     *
     * @param msg
     * @throws Exception
     */
    public void sendMsg(IMessage msg) throws Exception;

    public void setParam(IMQParam param);
}
