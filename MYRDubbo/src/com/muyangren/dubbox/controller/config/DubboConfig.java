package com.muyangren.dubbox.controller.config;

/**
 * Created by zql on 15/11/25.
 */

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.sun.xml.internal.stream.events.NamedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 15/11/25.
 *
 * @className DubboConfig
 * @classUse
 */
public class DubboConfig {
	private ApplicationConfig applicationConfig = new ApplicationConfig();
	private RegistryConfig registryConfig = new RegistryConfig();
	private List<ProtocolConfig> protocolConfig = new ArrayList<ProtocolConfig>();

	public DubboConfig() {
		initProtocolConfig();
	}

	public DubboConfig(String name, String address) {
		initProtocolConfig();
		setAppName(name);
		initRegistryConfig(address);

	}

	// ////////////////////////////
	// initRegistryConfig
	public void initRegistryConfig(String address) {

		this.registryConfig.setAddress(address);

	}

	public RegistryConfig getRegistryConfig() {
		return registryConfig;
	}

	// public void setRegistryConfig(RegistryConfig registryConfig) {
	// this.registryConfig = registryConfig;
	// }

	// initRegistryConfigEND
	// ///////////////////////////

	// //////////////////////////////
	// initProtocolConfig
	public void initProtocolConfig() {
		initSerializationConfig();
		initNetNettyConfig();
	}

	public void initSerializationConfig() {

		ProtocolConfig serializationConfig = new ProtocolConfig();
		serializationConfig.setName("dubbo");
		serializationConfig.setSerialization("kryo");
		this.protocolConfig.add(serializationConfig);

	}

	public void initNetNettyConfig() {

		ProtocolConfig netNettyConfig = new ProtocolConfig();
		netNettyConfig.setName("rest");
		netNettyConfig.setPort(8888);
		netNettyConfig.setKeepAlive(true);
		netNettyConfig.setServer("netty");
		netNettyConfig.setIothreads(5);
		netNettyConfig.setThreads(100);
		netNettyConfig.setContextpath("services");
		this.protocolConfig.add(netNettyConfig);

	}

	public List<ProtocolConfig> getProtocolConfig() {
		return protocolConfig;
	}

	// public void setProtocolConfig(List<ProtocolConfig> protocolConfig) {
	// this.protocolConfig = protocolConfig;
	// }

	// initProtocolConfigEnd
	// ///////////////////////////////////

	public String getAppName() {
		return applicationConfig.getName();
	}

	public void setAppName(String name) {
		applicationConfig.setName(name);
	}

	public ApplicationConfig getApplicationConfig() {
		return applicationConfig;
	}

	public void setApplicationConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

}
