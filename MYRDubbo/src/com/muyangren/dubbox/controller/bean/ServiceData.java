package com.muyangren.dubbox.controller.bean;/**
 * Created by zql on 15/11/25.
 */

/**
 * Created by zql on 15/11/25.
 * @className ServiceData
 * @classUse
 *
 *
 */
public class ServiceData
{
    /**
     * 服务名
     */
    private String name;
    /**
     * 服务对外接口
     */
    private String _interface;
    /**
     * 服务实体类
     */
    private String clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_interface() {
        return _interface;
    }

    public void set_interface(String _interface) {
        this._interface = _interface;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
