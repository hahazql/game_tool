package com.muyangren.dubbox.consumer.client;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public class ClientConfig {

    public ApplicationConfig applicationConfig = new ApplicationConfig();
    public RegistryConfig registryConfig = new RegistryConfig();
    public ConsumerConfig consumerConfig = new ConsumerConfig();
    private String version;


    private String group;
    private String protocol;

    public ClientConfig(String name, String address) {
        setAPPName(name);
        setProtocol("dubbo");
        this.registryConfig.setAddress(address);
    }

    public ClientConfig(String name, String address, String group) {
        setAPPName(name);
        setGroup(group);
        setProtocol("dubbo");
        this.registryConfig.setAddress(address);
    }

    public ClientConfig(String name, String address, String group,
                        String version) {
        setAPPName(name);
        setGroup(group);
        setVersion(version);
        setProtocol("dubbo");
        this.registryConfig.setAddress(address);
    }

    /**
     * 远程服务调用超时时间(毫秒)
     * 默认 1000
     *
     * @param time
     */
    public void setTimeOut(Integer time) {
        this.consumerConfig.setTimeout(time);
    }

    /**
     * 远程服务调用重试次数，不包括第一次调用，不需要重试请设为0
     * 默认 2
     *
     * @param num
     */
    public void setRetries(Integer num) {
        this.consumerConfig.setRetries(num);
    }

    /**
     * 负载均衡策略，
     * 可选值：random,roundrobin,leastactive，
     * 分别表示：随机，轮循，最少活跃调用
     *
     * @param loadbalance
     */
    public void setLoadbalance(String loadbalance) {
        this.consumerConfig.setLoadbalance(loadbalance);
    }


    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public String getAPPName() {
        return this.applicationConfig.getName();
    }

    public void setAPPName(String name) {
        this.applicationConfig.setName(name);
    }

    public String getVersion() {
        return version;
    }

    public ClientConfig setVersion(String version) {
        if (version == null)
            version = "";
        this.version = version;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public ClientConfig setGroup(String group) {
        if (group == null)
            group = "";
        this.group = group;
        return this;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public <T> ReferenceConfig<T> getReferenceConfig(Class<T> clazz) {

        ReferenceConfig<T> reference = new ReferenceConfig<T>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setProtocol(getProtocol());
        reference.setInterface(clazz);
        reference.setClient("netty");
        String Version = getVersion();
        if (Version != null && !Version.isEmpty())
            reference.setVersion(getVersion());

        String group = getGroup();
        if (group != null && !group.isEmpty())
            reference.setGroup(getGroup());

        return reference;

    }


}
