package com.hahazql.tools.sort;

import java.util.List;

public interface SortUtil<V> {
    /**
     * 添加数据
     *
     * @param data
     */
    public void put(V data);

    /**
     * 获取排序后的列表
     *
     * @param type 排序方式
     * @return
     */
    public List<V> getList(SortType type);

    /**
     * 获取排序后的列表
     *
     * @param size 最大元素数量
     * @param type 排序方式
     * @return
     */
    public List<V> getList(SortType type, Integer size);
}
