package com.hahazql.tools.msg;/**
 * Created by zql on 16/3/7.
 */

import java.nio.ByteBuffer;

/**
 * Created by zql on 16/3/7.
 *
 * @className IMessageHeader
 * @classUse 消息头
 */
public class IMessageHeader {
    /**
     * 消息头长度 (单位byte)
     */
    protected static final int headLength = 16;
    /**
     * 消息ID
     */
    protected long messageID = 0;

    protected int _messageType = -1;

    protected int messageDataLength = 0;

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public int getMessageType() {
        return _messageType;
    }

    public int getMessageDataLength() {
        return messageDataLength;
    }

    protected void setMessageType(int messageType) {
        this._messageType = messageType;
    }

    public void setMessageDataLength(int messageDataLength) {
        this.messageDataLength = messageDataLength;
    }

    public int getHeadLength() {
        return headLength;
    }

    /**
     * 获取头的byte数组
     *
     * @return
     */
    public byte[] headToBytes() {
        /**
         * long 8
         * int 4
         */
        ByteBuffer buffer = ByteBuffer.allocate(headLength);
        buffer.putLong(getMessageID());
        buffer.putInt(getMessageType());
        buffer.putInt(getMessageDataLength());
        return buffer.array();
    }

    /**
     * 从字节数组中获取消息头
     *
     * @param bytes
     * @return
     */
    public static IMessageHeader getHeader(byte[] bytes) {
        if (bytes.length < headLength)
            return null;
        ByteBuffer buffer = ByteBuffer.allocate(headLength);
        buffer.put(bytes, 0, headLength);
        IMessageHeader header = new IMessageHeader();
        buffer.flip();
        header.setMessageID(buffer.getLong());
        header.setMessageType(buffer.getInt());
        header.setMessageDataLength(buffer.getInt());
        buffer.clear();
        return header;
    }

}
