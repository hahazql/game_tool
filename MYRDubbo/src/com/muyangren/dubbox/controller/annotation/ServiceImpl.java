package com.muyangren.dubbox.controller.annotation;/**
 * Created by zql on 15/11/25.
 */

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @interface ServiceImpl
 * @interfaceUse
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceImpl
{
    /**
     * 作为远程调用的接口
     * 被注解类必须继承此接口
     * @return
     */
    public Class value();

    /**
     * 作为管理服务器显示的服务名
     */
    public String name() default "";
    
    /**
     * 接口版本设置
     * 只有consumer的verison一直才能调用
     * @return
     */
    public String version() default "";
    
    /**
     * 接口组设置
     * 用于区分不同组的同一个接口
     * @return
     */
    public String group() default "";
}
