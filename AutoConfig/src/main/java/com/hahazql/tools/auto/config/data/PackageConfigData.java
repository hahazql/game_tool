package com.hahazql.tools.auto.config.data;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by zql on 2015/10/20.
 */
public class PackageConfigData {
    private HashMap<String, ClassConfigData> _ix_clazz_name = new HashMap<String, ClassConfigData>();
    private ClassLoader classLoader;

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void put(ClassConfigData data) {
        _ix_clazz_name.put(data.getClassName(), data);
    }

    public ClassConfigData get(String clazzName) {
        return _ix_clazz_name.get(clazzName);
    }

    public Collection<ClassConfigData> getAllClazzConfig() {
        return _ix_clazz_name.values();
    }
}
