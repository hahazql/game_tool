package com.muyangren.dubbox.controller.process;

/**
 * Created by zql on 15/11/26.
 */

import com.alibaba.dubbo.config.ServiceConfig;
import com.muyangren.dubbox.controller.annotation.ServiceImpl;
import com.muyangren.dubbox.controller.config.DubboConfig;
import com.muyangren.dubbox.utils.PackageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 15/11/26.
 * 
 * @className ServiceExport
 * @classUse
 *
 *
 */
public class ServiceExport {
	private DubboConfig dubboConfig = new DubboConfig();
	
	


	private List<String> packageNames = new ArrayList<String>();
	private boolean running = true;

	public ServiceExport() {
		
	}
	
	
	public DubboConfig getDubboConfig() {
		return dubboConfig;
	}






	public void setDubboConfig(DubboConfig dubboConfig) {
		this.dubboConfig = dubboConfig;
	}
	
	public ServiceExport addPackage(String packageName) {
		this.packageNames.add(packageName);
		return this;
	}

	public void start() {
		for (String packageName : packageNames) {
			export(packageName);
		}
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				synchronized (ServiceExport.class) {
					running = false;
					ServiceExport.class.notify();
				}
			}
		});
		synchronized (this) {
			while (running) {
				try {
					ServiceExport.class.wait();
				} catch (Throwable e) {
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void export(String packageName) {
		List<Class<?>> classList = PackageUtil.find(packageName);
		for (Class<?> clazz : classList) {
			ServiceImpl impl = clazz.getAnnotation(ServiceImpl.class);
			if (impl == null)
				continue;

			Class _interface = impl.value();
			if (_interface == null) {
				/***** 必须抛出异常 ********/
			}
			if (!clazz.isAssignableFrom(_interface)) {
				/**** 必须抛出异常说明设置的注解有问题 ***/
			}
			export(impl.name(), _interface, clazz,impl.version(),impl.group());
		}
	}

	@SuppressWarnings("rawtypes")
	private <T> boolean export(String name, Class<T> _interface,
			Class<? extends T> clazz) {
		try {
			ServiceConfig serviceConfig =getServiceConfig(_interface,
					clazz,"","");
			serviceConfig.export();
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	private <T> boolean export(String name,Class<T> _interface,Class<? extends T> clazz,String version,String group)
	{
		try {
			ServiceConfig serviceConfig =getServiceConfig(_interface,
					clazz,version,group);
			serviceConfig.export();
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	private  <T> ServiceConfig<T> getServiceConfig(Class<T> _interface,
			Class<? extends T> clazz,String version,String group) throws IllegalAccessException,
			InstantiationException {
		T t = clazz.newInstance();
		ServiceConfig<T> config = new ServiceConfig<T>();
		config.setApplication(getDubboConfig().getApplicationConfig());
		config.setRegistry(getDubboConfig().getRegistryConfig());
		config.setProtocols(getDubboConfig().getProtocolConfig());
		if(version!=null&&!version.isEmpty())
			config.setVersion(version);
		if(group!=null&&!group.isEmpty())
			config.setGroup(group);
		config.setInterface(_interface);
		config.setRef(t);
		return config;
	}

}
