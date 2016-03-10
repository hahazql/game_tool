package com.hahazql.tools.auto.config.data;

import com.hahazql.tools.auto.config.AutoFieldConfig;

/**
 * Created by zql on 2015/10/16.
 */
public class FieldConfigData {
    private String fieldName;
    private String showName;
    private String showDesc;
    private String fieldType;
    private String elementClassName = "";
    private boolean isArray;
    private String regular = "";

    public FieldConfigData() {

    }

    public FieldConfigData(AutoFieldConfig fieldConfig) {
        setFieldName(fieldConfig.getName());
        setShowName(fieldConfig.getFieldName());
        setShowDesc(fieldConfig.getFieldDesc());
        setIsArray(fieldConfig.isArray());
        if (fieldConfig.isArray())
            setElementClassName(fieldConfig.getElementClazz().getName());
        setFieldType(fieldConfig.getFieldType().getName());
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        fieldName = fieldName.replaceAll(" ", "_");
        this.fieldName = fieldName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowDesc() {
        return showDesc;
    }

    public void setShowDesc(String showDesc) {
        this.showDesc = showDesc;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }
}
