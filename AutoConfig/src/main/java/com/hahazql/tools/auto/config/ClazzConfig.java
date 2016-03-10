package com.hahazql.tools.auto.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zql on 2015/9/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClazzConfig {
    /**
     * 当前配置名
     *
     * @return
     */
    public String value();

    /**
     * 配置的描述
     *
     * @return
     */
    public String configDesc() default "";


    /**
     * 配置的项目名
     * 若存在多系统的配置通过此配置来区分
     *
     * @return
     */
    public String configSrcName();
}
