package com.muyangren.dubbox.consumer.constant;/**
 * Created by zql on 15/11/24.
 */

/**
 * Created by zql on 15/11/24.
 * @className ProtocolType
 * @classUse
 *
 *
 */
public enum ProtocolType
{
    zookeeper("zookeeper");

    private String type;

    private ProtocolType(String t)
    {
        type = t;
    }

    public String toString()
    {
        return type;
    }

    public ProtocolType parse(String type)
    {
        if(type.equals("zookeeper"))
            return zookeeper;
        return null;
    }

}
