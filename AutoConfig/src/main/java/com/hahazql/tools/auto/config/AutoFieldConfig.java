package com.hahazql.tools.auto.config;

import java.io.Serializable;

/**
 * Created by zql on 2015/9/28.
 */
public class AutoFieldConfig<T extends AutoParam> implements Serializable {
    /**
     * 类中的标识
     */
    protected String name;
    /**
     * 属性名
     */
    protected String fieldName;
    /**
     * 属性类型
     */
    protected Class fieldType;
    /**
     * 属性描述
     */
    protected String fieldDesc;
    /**
     * 属性值的正则判断
     */
    protected String regular;
    /**
     * 数组元素的类
     */
    protected Class elementClazz;

    /**
     * 当属性类型为数组时
     * 数组元素的配置
     */
    protected AutoConfig<T> fieldConfig;

    protected T param;

    protected boolean isArray = false;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }

    public Class getElementClazz() {
        return elementClazz;
    }

    public void setElementClazz(Class elementClazz) {
        this.elementClazz = elementClazz;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public AutoConfig<T> getFieldConfig() {
        return fieldConfig;
    }

    public void setFieldConfig(AutoConfig<T> fieldConfig) {
        this.fieldConfig = fieldConfig;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
