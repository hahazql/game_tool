package com.hahazql.tools.msg;/**
 * Created by zql on 15/12/22.
 */

/**
 * @interface IMessageRecognizer
 * @interfaceUse
 */
public interface IMessageRecognizer {
    /**
     * 根据type构建消息的实例
     *
     * @param msgType 消息的类型
     * @return 消息实例
     * @throws MessageParseException 没有与type相匹配的消息类型时,会抛出此异常
     */
    public IMessage createMessageImpl(Integer msgType) throws Exception;
}
