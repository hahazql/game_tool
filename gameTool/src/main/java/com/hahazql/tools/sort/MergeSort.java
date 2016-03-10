package com.hahazql.tools.sort;/**
 * Created by zql on 15/6/23.
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zql on 15/6/23.
 *
 * @className MergeSort
 * @classUse 2路归并
 * @note 速度仅次于快速排序，为稳定排序算法，一般用于对总体无序，但是各子项相对有序的数列。
 * @time O(nlogn)
 */

public class MergeSort<V extends SortData> implements SortUtil<V> {
    ArrayList<V> list = new ArrayList<V>();

    public void mergeSort(ArrayList<V> input, int left, int right) {
        int t = 1;
        int size = right - left + 1;
        while (t < size) {
            int s = t;
            t = 2 * s;
            int i = left;
            while (i + (t - 1) < size) {
                merge(input, i, i + (s - 1), i + (t - 1));
                i += t;
            }
            if (i + (s - 1) < right)
                merge(input, i, i + (s - 1), right);
        }
    }

    @SuppressWarnings("unchecked")
    private void merge(ArrayList<V> data, int p, int q, int r) {
        ArrayList<V> temp = (ArrayList<V>) data.clone();
        int s = p;
        int t = q + 1;
        int k = p;
        while (s <= q && t <= r) {
            boolean result = data.get(s).isLess(data.get(t));
            if (result) {
                temp.set(k, data.get(s));
                s++;
            } else {
                temp.set(k, data.get(t));
                t++;
            }
            k++;
        }
        if (s == q + 1)
            temp.set(k++, data.get(t++));
        else
            temp.set(k++, data.get(s++));
        for (int i = p; i <= r; i++)
            data.set(i, temp.get(i));
    }


    @SuppressWarnings("unchecked")
    public List<V> getMergeSort(SortType type, Integer size) {
        ArrayList<V> input = (ArrayList<V>) list.clone();

        mergeSort(input, 0, input.size() - 1);

        return toList(input, type, size);
    }

    public void put(V data) {
        list.add(data);
    }

    public List<V> getList(SortType type) {
        return getMergeSort(type, list.size());
    }

    public List<V> getList(SortType type, Integer size) {
        return getMergeSort(type, size);
    }

    /**
     * 按照排序方式要求处理数组
     *
     * @param input
     * @param type
     * @param size
     * @return
     */
    @SuppressWarnings("incomplete-switch")
    protected List<V> toList(List<V> input, SortType type, int size) {
        int maxSize = input.size();
        switch (type) {
            case desc:
                Collections.reverse(input);
        }
        size = Math.min(size, maxSize);
        return input.subList(0, size);
    }
}
