package com.hahazql.tools.msg;/**
 * Created by zql on 16/3/7.
 */

import java.util.HashMap;

/**
 * Created by zql on 16/3/7.
 *
 * @className MessageRecognizer
 * @classUse 消息识别器
 */
public class MessageRecognizer implements IMessageRecognizer {
    private HashMap<Integer, Class<? extends IMessage>> _type_message = new HashMap<Integer, Class<? extends IMessage>>();

    public IMessage recognizer(byte[] message) throws Exception {
        IMessageHeader header = IMessageHeader.getHeader(message);
        if (header == null)
            return null;
        Class<? extends IMessage> iMessage = _type_message.get(header.getMessageType());
        if (iMessage == null)
            return null;
        IMessage msg = iMessage.newInstance();
        byte[] data = new byte[header.getMessageDataLength()];
        System.arraycopy(message, header.getHeadLength(), data, 0, header.getMessageDataLength());
        return msg.decode(header, data);
    }

    public void putMessage(IMessage msg) {
        _type_message.put(msg.getMessageType(), msg.getClass());
    }


    public IMessage createMessageImpl(Integer msgType) throws MessageParseException, IllegalAccessException, InstantiationException {
        Class<? extends IMessage> iMessage = _type_message.get(msgType);
        if (iMessage == null)
            return null;
        IMessage msg = iMessage.newInstance();
        return msg;
    }
}
