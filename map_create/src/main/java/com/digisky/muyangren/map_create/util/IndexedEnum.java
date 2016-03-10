package com.digisky.muyangren.map_create.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zql on 15/12/22.
 *
 * @className IndexedEnum
 * @classUse 从0开始的枚举, 索引可以不连续, 为了性能最好保持连续性
 */
public interface IndexedEnum {

    /**
     * 获取该枚举的索引值
     *
     * @return 返回>=0的索引值
     */
    public abstract int getIndex();

    public static class IndexedEnumUtil {

        /**
         * 索引警戒上限，发现超过此值的索引可能存在较大的空间浪费
         */
        private static final int WORNNING_MAX_INDEX = 1000;


        /**
         * 将枚举中的元素放到一个List中，每个元素在list中的下表即为他的index，如果有不连续的index，则空缺的index用null填充
         *
         * @param <E>
         * @param enums
         * @return
         */
        public static <E extends IndexedEnum> List<E> toIndexes(E[] enums) {
            int maxIndex = Integer.MIN_VALUE;
            int curIdx = 0;
            // 找到最大index，此值+1就是结果list的size
            for (E enm : enums) {
                curIdx = enm.getIndex();
                // 索引不能为负
                Assert.isTrue(curIdx >= 0, String.format("枚举索引不能为负 index: %1$d type: %2$s ", curIdx, enums.getClass()
                        .getComponentType().getName()));
                if (curIdx > maxIndex) {
                    maxIndex = curIdx;
                }
            }
            if (maxIndex >= WORNNING_MAX_INDEX) {
                LogMgr.warn(enums.getClass().getComponentType().getName(), String.format("警告：枚举类%s中有索引超过%d的索引，如果有很多索引空缺，可能会造成空间浪费"));
            }
            List<E> instances = new ArrayList<E>(maxIndex + 1);
            // 先全用null填充
            for (int i = 0; i < maxIndex + 1; i++) {
                instances.add(null);
            }
            for (E enm : enums) {
                curIdx = enm.getIndex();
                // 索引必须唯一
                Assert.isTrue(instances.get(curIdx) == null, "枚举中有重复的index type= "
                        + enums.getClass().getComponentType().getName());
                instances.set(curIdx, enm);
            }
            return instances;
        }


        /**
         * 获取枚举
         *
         * @param index 索引值
         * @param enums 枚举数组
         * @param <E>   枚举对象
         * @return
         */
        public static <E extends IndexedEnum> E getEnum(int index, E[] enums) {
            List<E> list = toIndexes(enums);
            return list.get(index);
        }
    }
}