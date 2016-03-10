package com.muyangren.dubbox.consumer;

import com.muyangren.dubbox.consumer.client.ClientConfig;
import com.muyangren.dubbox.consumer.client.ConsumerClient;

public class ConsumerTool {
	
	


	public static ConsumerClient getConsumerClient(String name, String zookeeperip,int zookeeperport){
		
        String address=getAddress(zookeeperip, zookeeperport) ;
		ClientConfig  config=new ClientConfig(name,address);
		ConsumerClient v=new ConsumerClient();
		v.setConfig(config);
		return v;
		 
	}
	
	
	
	
	public static ConsumerClient getConsumerClient(String name, String zookeeperip,int zookeeperport,String group){
		
        String address=getAddress(zookeeperip, zookeeperport) ;
		ClientConfig  config=new ClientConfig(name,address,group);
		ConsumerClient v=new ConsumerClient();
		v.setConfig(config);
		return v;
		 
	}
	
	
	public static ConsumerClient getConsumerClient(String name, String zookeeperip,int zookeeperport,String group,String version){
		
        String address=getAddress(zookeeperip, zookeeperport) ;
		ClientConfig  config=new ClientConfig(name,address,group,version);
		ConsumerClient v=new ConsumerClient();
		v.setConfig(config);
		return v;
		 
	}
	
	
	
	
	private static String getAddress(String zookeeperip,int zookeeperport){
		
		String address="zookeeper://"+zookeeperip+":"+zookeeperport;
		return address;
		
	}
	
	
}
