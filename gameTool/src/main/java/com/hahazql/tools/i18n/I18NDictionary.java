package com.hahazql.tools.i18n;

/**
 *
 *
 */
public interface I18NDictionary<K, V> {

    /**
     * 读取指定的数据
     *
     * @param key
     * @return
     */
    public abstract V read(K key);

    /**
     * 加载数据
     *
     * @return
     */
    public abstract void load();

}