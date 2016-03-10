package com.muyangren.dubbox.controller.config;/**
 * Created by zql on 15/11/25.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 15/11/25.
 * @className ControllerConfig
 * @classUse
 *
 *
 */
public class ControllerConfig
{
    /**
     * ID
     */
    private int id;
    /**
     * 管理服务器验证字段
     */
    private String tokenKey;
    /**
     * 管理服务器地址
     */
    private String address;
    /**
     * 待扫描的包路径
     */
    private List<String> packagePath = new ArrayList<String>();

    public int getId() {
        return id;
    }

    public ControllerConfig setId(int id) {
        this.id = id;
        return this;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public ControllerConfig setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ControllerConfig setAddress(String address) {
        this.address = address;
        return this;
    }

    public List<String> getPackagePath() {
        return packagePath;
    }

    public ControllerConfig addPackagePath(String path)
    {
        packagePath.add(path);
        return this;
    }
}
