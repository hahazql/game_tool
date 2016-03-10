package com.hahazql.tools.router;

public @interface RequestMapping {
    /**
     * 方法调用的路径
     * 用于客户端请求时的方法路径名
     *
     * @return
     */
    public String value() default "";
}
