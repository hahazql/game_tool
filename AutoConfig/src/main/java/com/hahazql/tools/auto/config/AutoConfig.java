package com.hahazql.tools.auto.config;

import com.hahazql.tools.auto.config.data.ClassConfigData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zql on 2015/9/28.
 */
public class AutoConfig<T extends AutoParam> implements Serializable {
    /**
     * 配置名
     */
    protected String configName;
    /**
     * 配置的描述
     */
    protected String configDesc = "";
    /**
     * 配置的项目名
     */
    protected String configDirName = "";

    /**
     * 配置的类
     */
    protected Class<? extends Comparable> clazz;
    /**
     * 配置的属性列表
     */
    protected ArrayList<AutoFieldConfig<T>> fieldList = new ArrayList<AutoFieldConfig<T>>();

    public AutoConfig(String configName, String configDesc, String configDirName) {
        setConfigName(configName);
        setConfigDesc(configDesc);
        setConfigDirName(configDirName);
    }

    public AutoConfig(ClazzConfig config) {
        setConfigName(config.value());
        setConfigDesc(config.configDesc());
        setConfigDirName(config.configSrcName());
    }

    public AutoConfig(ClassConfigData data, Class<? extends Comparable> clazz) {
        setConfigName(data.getConfigName());
        setConfigDesc(data.getConfigDesc());
        setClazz(clazz);
    }


    public Class<? extends Comparable> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Comparable> clazz) {
        this.clazz = clazz;
    }

    public String getConfigName() {
        configName = configName.replaceAll(" ", "_");
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public String getConfigDirName() {
        return configDirName;
    }

    public void setConfigDirName(String configDirName) {
        configDirName = configDirName.replaceAll(" ", "_");
        this.configDirName = configDirName;
    }

    public ArrayList<AutoFieldConfig<T>> getFieldList() {
        return fieldList;
    }

    public void setFieldList(ArrayList<AutoFieldConfig<T>> fieldList) {
        this.fieldList = fieldList;
    }

    public void addField(AutoFieldConfig config) {
        if (config == null)
            return;
        this.fieldList.add(config);
    }

}
