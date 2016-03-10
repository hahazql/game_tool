package com.hahazql.tools.msg.mq.rocketmq;/**
 * Created by zql on 16/3/9.
 */

import com.hahazql.tools.msg.mq.IMQParam;

/**
 * Created by zql on 16/3/9.
 * @className RocketMQParam
 * @classUse
 *
 *
 */
public class RocketMQParam implements IMQParam
{
    private String ip;
    private int port;
    private String producerGroup;
    private String topic;
    private String[] tags;

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTag() {
        if(tags == null || tags.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for(String t : tags)
        {
            sb.append(t + "|");
        }
        String ret = sb.substring(0,sb.length() - 2);
        return ret;
    }

    public void setTags(String... tags) {
        this.tags = tags;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddr()
    {
        return ip + ":" + port;
    }
}
