package com.hahazql.tools.common;/**
 * Created by zql on 15/12/29.
 */

import com.hahazql.util.clazz.PackageUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 15/12/29.
 *
 * @className ScanInitializeClass
 * @classUse
 */
public class ScanInitializeClass {
    List<InitializeClass> initializeClassesList = new ArrayList<>();
    public static List<Class> clazzList = new ArrayList<>();
    String packageName = "";

    /**
     * 扫描的包名
     *
     * @param packageName
     */
    public ScanInitializeClass(String packageName) {
        this.packageName = packageName;
    }

    public void scan() throws Exception {
        initializeClassesList.clear();
        clazzList.clear();
        for (String clazzName : PackageUtil.getClassName(packageName)) {
            Class clazz = Class.forName(clazzName);
            //判断是否为抽象类
            if (Modifier.isAbstract(clazz.getModifiers()))
                continue;
            //判断是否是自动初始化实例
            if (!InitializeClass.class.isAssignableFrom(clazz)) {
                addClazz(clazz);//若不是则添加到class列表中
                continue;
            }
            InitializeClass initializeClass = null;
            try {
                initializeClass = (InitializeClass) clazz.newInstance();
            } catch (Exception e) {
                continue;
            }
            addInitializeClass(initializeClass);
        }
        initClass();
    }


    public void addInitializeClass(InitializeClass initialize) {
        initializeClassesList.add(initialize);
    }

    public void addClazz(Class clazz) {
        clazzList.add(clazz);
    }

    /**
     * 调用所有的自动初始化处理类的INIT
     *
     * @throws Exception
     */
    public void initClass() throws Exception {
        for (InitializeClass initializeClass : initializeClassesList) {
            initializeClass.init();
        }
    }
}
