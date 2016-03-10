package com.hahazql.tools.sort;/**
 * Created by zql on 15/6/23.
 */


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 15/6/23.
 *
 * @className MergeSort3
 * @classUse 3路快排
 * @note 速度仅次于快速排序，为稳定排序算法，一般用于对总体无序，但是各子项相对有序的数列。
 * @timeUse nlog3(n)
 */
public class MergeSort3<V extends SortData> implements SortUtil<V> {

    private ArrayList<V> data = new ArrayList<V>();

    public void put(V data) {
        this.data.add(data);
    }

    public List<V> getList(SortType type) {
        return sort(type, data.size());
    }

    public List<V> getList(SortType type, Integer size) {
        return sort(type, size);
    }

    @SuppressWarnings("unchecked")
    public List<V> sort(SortType type, int size) {
        ArrayList<V> list = (ArrayList<V>) data.clone();
        sort(list, 0, list.size() - 1, type);
        size = Math.min(list.size(), size);
        return list.subList(0, size);
    }

    public int random(int start, int end) {
        return (start + (int) (Math.random() * (end - start + 1)));
    }

    public void sort(List<V> a, int low, int high, SortType type) {
        if (low >= high) return;

        int proviet = random(low, high);
        swap(a, proviet, low);
        V provietValue = a.get(low);
        int lt = low, gt = high;
        int i = low;

        while (i <= gt) {
            boolean result = a.get(i).isLess(provietValue);
            if (type == SortType.desc)
                result = !result;
            if (result)
                swap(a, lt++, i++);
            else if (!result)
                swap(a, i, gt--);
            else
                i++;

        }
        sort(a, low, lt - 1, type);
        sort(a, gt + 1, high, type);
    }

    private void swap(List<V> a, int i, int j) {
        V temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

}
