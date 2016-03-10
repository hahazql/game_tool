package com.hahazql.tools.auto.config.data;

import com.hahazql.tools.auto.config.AutoConfig;
import com.hahazql.tools.auto.config.AutoFieldConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 2015/10/16.
 */
public class ClassConfigData {
    private String className;
    private String configName;
    private String configSrcName;
    private String configDesc;
    private List<FieldConfigData> list = new ArrayList<FieldConfigData>();

    public ClassConfigData() {
    }

    ;

    public ClassConfigData(AutoConfig autoConfig) {
        setConfigName(autoConfig.getConfigName());
        setConfigSrcName(autoConfig.getConfigDirName());
        setConfigDesc(autoConfig.getConfigDesc());
        setClassName(autoConfig.getClazz().getName());
        List<FieldConfigData> list = new ArrayList<FieldConfigData>();
        for (Object _fieldConfig : autoConfig.getFieldList()) {
            AutoFieldConfig fieldConfig = (AutoFieldConfig) _fieldConfig;
            FieldConfigData fieldConfigData = new FieldConfigData(fieldConfig);
            list.add(fieldConfigData);
        }
        setList(list);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigSrcName() {
        return configSrcName;
    }

    public void setConfigSrcName(String configSrcName) {
        this.configSrcName = configSrcName;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public List<FieldConfigData> getList() {
        return list;
    }

    public void setList(List<FieldConfigData> list) {
        this.list = list;
    }
}
