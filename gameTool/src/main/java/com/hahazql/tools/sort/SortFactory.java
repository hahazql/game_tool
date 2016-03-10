package com.hahazql.tools.sort;/**
 * Created by zql on 15/6/23.
 */

/**
 * Created by zql on 15/6/23.
 *
 * @className SortFactory
 * @classUse
 */
public class SortFactory {
    @SuppressWarnings("rawtypes")
    public static <V extends SortData> SortUtil getInstance(V v, SortMethod method) {
        switch (method) {
            case bitMap:
                return new BitMapSort();
            case mergeSort3:
                return new MergeSort3<V>();
            case mergeSort:
                return new MergeSort<V>();
        }
        return null;
    }

}
