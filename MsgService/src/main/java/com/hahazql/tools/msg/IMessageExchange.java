package com.hahazql.tools.msg;/**
 * Created by zql on 16/3/8.
 */

/**
 * Created by zql on 16/3/8.
 * @className IMessageExchange
 * @classUse 消息转发器(将消息传递给不同的处理器处理)
 *
 */
public abstract class IMessageExchange
{
    public abstract void recMsg(IMessage message);
}
