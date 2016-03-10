package com.muyangren.dubbox.consumer.client;

/**
 * Created by zql on 15/11/24.
 */

import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;

/**
 * Created by zql on 15/11/24.
 * 
 * @className ConsumerClient
 * @classUse
 *
 *
 */
public class ConsumerClient {
	ClientConfig config;

	public ConsumerClient() {

	}

	public ClientConfig getConfig() {
		return config;
	}

	public void setConfig(ClientConfig config) {
		this.config = config;
	}

	public <T> T getProvide(Class<T> clazz) {
		ReferenceConfigCache cache = ReferenceConfigCache.getCache(getConfig()
				.getAPPName());
		ReferenceConfig<T> reference = getConfig().getReferenceConfig(clazz);
        return cache.get(reference);
	}

	/**
	 * 获取远程调用接口
	 * @param clazz
	 * @param isAsync <p>是否一定要同步等待结果
	 * 				   	 true 等待结果
	 * 					 false 异步通知不等待结果</p>
	 * @param <T>
     * @return
     */
	public <T> T getProvide(Class<T> clazz,boolean isAsync) {
		ReferenceConfigCache cache = ReferenceConfigCache.getCache(getConfig()
				.getAPPName());
		ReferenceConfig<T> reference = getConfig().getReferenceConfig(clazz);
		reference.setAsync(isAsync);
		return cache.get(reference);
	}


	/**
	 * 获取远程调用接口
	 * @param clazz
	 * @param cacheType <p>以调用参数为key，缓存返回结果，可选：lru, threadlocal, jcache等</p>
	 * @param <T>
     * @return
     */
	public <T> T getProvide(Class<T> clazz,String cacheType)
	{
		ReferenceConfigCache cache = ReferenceConfigCache.getCache(getConfig()
				.getAPPName());
		ReferenceConfig<T> reference = getConfig().getReferenceConfig(clazz);
		ConsumerConfig config = reference.getConsumer();
		config.setCache(cacheType);
		reference.setConsumer(config);
		return cache.get(reference);
	}


	public <T> void clearClassCache(Class<T> clazz) {
		ReferenceConfigCache cache = ReferenceConfigCache.getCache(getConfig()
				.getAPPName());
		ReferenceConfig<T> reference = new ReferenceConfig<T>();
		reference.setApplication(getConfig().applicationConfig);
		reference.setRegistry(getConfig().registryConfig);
		reference.setProtocol("dubbo");
		reference.setInterface(clazz);
		String Version = getConfig().getVersion();
		if (Version != null && !Version.isEmpty())
			reference.setVersion(getConfig().getVersion());

		String group = getConfig().getGroup();
		if (group != null && !group.isEmpty())
			reference.setGroup(getConfig().getGroup());
		cache.destroy(reference);
	}
}
