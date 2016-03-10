package com.hahazql.tools.i18n;


import com.hahazql.tools.common.InitializeRequired;

/**
 * 多语言管理服务管理
 */
public interface SysLangService extends InitializeRequired {

    /**
     * 读取系统内部的多语言数据
     *
     * @param key
     * @return
     */
    public String read(String key);

    /**
     * 读取系统内部的多语言数据,如果params不为空,则用其格式化结果
     *
     * @param key
     * @param params
     * @return
     */
    public String read(String key, Object... params);

}
