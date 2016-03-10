package com.hahazql.tools.sort;/**
 * Created by zql on 15/6/23.
 */

/**
 * Created by zql on 15/6/23.
 *
 * @className SortData
 * @classUse
 */
public abstract class SortData {
    /**
     * 大于参数
     *
     * @param data
     * @return
     */
    public boolean isGreater(SortData data) {
        if (isLess(data))
            return false;
        return true;
    }

    /**
     * 小于参数
     *
     * @param data
     * @return
     */
    public abstract boolean isLess(SortData data);

    public abstract <V> V getData();
}
