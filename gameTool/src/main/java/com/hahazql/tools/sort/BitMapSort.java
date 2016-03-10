package com.hahazql.tools.sort;/**
 * Created by zql on 15/6/23.
 */

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zql on 15/6/23.
 *
 * @className BitMapSort
 * @classUse 位图排序
 * @note 用于待排序数量特别多的情况，节省内存，重复的数会被移除
 */

public class BitMapSort implements SortUtil<Integer> {
    private BitSet bitSet = new BitSet();
    private HashMap<Integer, Integer> _same_num = new HashMap<Integer, Integer>();


    public void put(Integer data) {
        if (bitSet.get(data)) {
            if (!_same_num.containsKey(data))
                _same_num.put(data, 1);
            _same_num.put(data, _same_num.get(data) + 1);
        }
        bitSet.set(data);
    }

    public List<Integer> getList(SortType type) {
        switch (type) {
            case desc:
                return getListDesc(bitSet.size());
            default:
                return getListAsc(bitSet.size());
        }
    }


    public List<Integer> getList(SortType type, Integer size) {
        switch (type) {
            case desc:
                return getListDesc(size);
            default:
                return getListAsc(size);
        }
    }

    /**
     * 获取正序排列的数组
     *
     * @param size 最大获取元素数量
     * @return
     */
    private List<Integer> getListAsc(Integer size) {
        List<Integer> result = new ArrayList<Integer>();
        Integer len = bitSet.length();
        Integer maxSize = Math.min(len, size);
        for (int i = 0; i < maxSize; i++) {
            if (bitSet.get(i)) {
                Integer count = _same_num.get(i);
                if (count == null)
                    count = 1;
                for (int j = 0; j < count; j++) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    /**
     * 获取倒序排列的数组
     *
     * @param size 最大获取元素数量
     * @return
     */
    private List<Integer> getListDesc(Integer size) {
        List<Integer> result = new ArrayList<Integer>();
        Integer len = bitSet.length();
        Integer maxSize = Math.min(len, size);
        for (int i = (len - 1); i > (len - maxSize - 1); i--) {
            if (bitSet.get(i))
                result.add(i);
        }
        return result;
    }


}
