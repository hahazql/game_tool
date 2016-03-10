package com.hahazql.tools.i18n.impl;


import com.hahazql.tools.i18n.I18NDictionary;
import com.hahazql.tools.i18n.SysLangService;

import java.text.MessageFormat;

/**
 * 多语言管理器
 */
public class SysLangServiceImpl implements SysLangService {
    /**
     * 多语言容器
     */
    private I18NDictionary<String, String> sysLangs;

    /**
     * @param sysLangConfigFile 多语言配置的文件路径
     */
    public SysLangServiceImpl(String... sysLangConfigFile) {
        sysLangs = new ExcelDictionaryImpl(sysLangConfigFile);
    }

    @Override
    public String read(String key) {
        return sysLangs.read(key);
    }

    @Override
    public String read(String key, Object... params) {
        String _msg = sysLangs.read(key);
        if (_msg == null) {
            return "lang_" + key;
        }
        if (params != null) {
            return MessageFormat.format(_msg, params);
        } else {
            return _msg;
        }
    }

    @Override
    public void init() {
    }

}
