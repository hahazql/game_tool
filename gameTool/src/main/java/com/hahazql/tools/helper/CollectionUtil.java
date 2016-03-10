package com.hahazql.tools.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集合相关的工具类
 */
public class CollectionUtil {
    /**
     * 构建泛型类型的HashMap,该Map的初始容量是0
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> buildHashMap() {
        return new HashMap<K, V>(0);
    }

    /**
     * 构建泛型类型的ConcurrentHashMap,该Map的初始容量是0
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> buildConcrrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }
}
