package com.muyangren.config;

public class SysConfig {

	private String zookeeperip;
	private int zookeeperport;
	private String dubbopackage;
	
	

	public static SysConfig instance = new SysConfig();

	public static SysConfig getInstnce() {
		return instance;
	}
	
	public void initConfig() {
		setDubbopackage();
		setZookeeperip();
		setZookeeperport();
		
	}

	public String getZookeeperip() {
		if (zookeeperip.isEmpty())
			setZookeeperip();
		return zookeeperip;
	}

	public void setZookeeperip() {

		this.zookeeperip = Config.get("zookeeperip");
	}

	public int getZookeeperport() {
		if (zookeeperport == 0)
			setZookeeperport();
		return zookeeperport;
	}

	public void setZookeeperport() {
		this.zookeeperport = Integer.parseInt(Config.get("zookeeperport"));

	}
	
	
	public String getDubbopackage() {
		if (dubbopackage==null || dubbopackage.isEmpty() )
			setDubbopackage();
		return dubbopackage;
	}

	public void setDubbopackage() {
		this.dubbopackage = Config.get("dubbopackage");;
	}

	
	
	public String  getZookeeperaddress(){
		
		return "zookeeper://"+getZookeeperip()+":"+getZookeeperport();
		
		
	}

}
