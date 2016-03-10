package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import java.util.*;

/**
 * Created by zql on 16/2/29.
 *
 * @className TemplateObjectConfig
 * @classUse 区块模板配置
 */
public class TemplateObjectConfig implements BaseObjectConfig, Cloneable {
    private int id;
    private Map<Integer, UnitCreateObjectConfig> _ix_id = new HashMap<Integer, UnitCreateObjectConfig>();
    private Map<Integer, Integer> _create_num = new HashMap<Integer, Integer>();

    public List<UnitCreateObjectConfig> getCreateList() {
        List<UnitCreateObjectConfig> ret = new ArrayList<UnitCreateObjectConfig>();
        ret.addAll(_ix_id.values());
        Collections.sort(ret);
        return ret;
    }

    public int getCreateNum(int configID) {
        return this._create_num.get(configID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TemplateObjectConfig addConfig(UnitCreateObjectConfig config, int create_num) {
        _ix_id.put(config.getId(), config);
        _create_num.put(config.getId(), create_num);
        return this;
    }

}
