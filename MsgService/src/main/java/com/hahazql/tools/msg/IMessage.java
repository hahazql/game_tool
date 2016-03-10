package com.hahazql.tools.msg;/**
 * Created by zql on 16/3/7.
 */

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by zql on 16/3/7.
 *
 * @className IMessage
 * @classUse 消息基础类
 */
public abstract class IMessage extends IMessageHeader {

    public IMessage() {
        setMessageType(getMsgType());
    }

    /**
     * 消息类型
     *
     * @return
     */
    public abstract int getMsgType();


    /**
     * 消息的数据转为字节数组
     *
     * @return
     */
    protected abstract byte[] dataToBytes();

    /**
     * 将数据块的字节数组解析为自身的消息
     *
     * @param message
     * @return
     */
    public abstract IMessage decode(IMessageHeader header, byte[] message) throws UnsupportedEncodingException;

    /**
     * 获取消息转化的字节数组
     *
     * @return
     */
    public byte[] getBytes() {
        byte[] dataByte = dataToBytes();
        int dataLength = dataByte.length;
        setMessageDataLength(dataLength);
        ByteBuffer buffer = ByteBuffer.allocate(headLength + dataLength);
        buffer.put(headToBytes());
        buffer.put(dataByte);
        return buffer.array();
    }


    /**
     * 根据头数据设置头
     *
     * @param header
     */
    public void setHeader(IMessageHeader header) {
        this._messageType = header.getMessageType();
        this.messageDataLength = header.messageDataLength;
        this.messageID = header.messageID;
    }
}
