package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.util.LogMgr;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by zql on 16/2/29.
 *
 * @className BaseConfig
 * @classUse
 */
public abstract class BaseConfig<T extends BaseObjectConfig> {
    public BaseConfig() {
        LogMgr.info(BaseConfig.class, "初始化配置.....");
        initialize();
    }

    protected HashMap<Integer, T> _ix_id = new HashMap<Integer, T>();


    public abstract Collection<T> readConfig();

    public void initialize() {
        Collection<T> configs = readConfig();
        for (T config : configs) {
            addConfig(config);
        }
    }

    public T get(int id) {
        return _ix_id.get(id);
    }

    public void addConfig(T config) {
        _ix_id.put(config.getId(), config);
    }
}
