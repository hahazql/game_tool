package com.hahazql.tools.auto.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zql on 2015/9/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldConfig {
    /**
     * 配置文件中的属性名
     *
     * @return
     */
    public String value() default "";

    /**
     * 此属性的描述
     *
     * @return
     */
    public String desc() default "";

    /**
     * 此数据的正则判断
     * 正则判断功能还未添加暂时不可用
     *
     * @return
     */
    public String regular() default "";

    /**
     * 是否需要隐藏
     *
     * @return
     */
    public boolean ishidden() default false;

    /**
     * 若属性为数组则数组元素的类
     *
     * @return
     */
    public Class elementClazz() default Object.class;
}
