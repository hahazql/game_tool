package com.muyangren.dubbox.serialization; /**
 * Created by zql on 15/11/24.
 */

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.muyangren.dubbox.bean.TestResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zql on 15/11/24.
 * @className KryoSerializationOptimizerImpl
 * @classUse
 *
 *
 */
public class KryoSerializationOptimizerImpl implements SerializationOptimizer {
    @Override
    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<Class>();
        classes.add(TestResult.class);
        return classes;
    }
}
