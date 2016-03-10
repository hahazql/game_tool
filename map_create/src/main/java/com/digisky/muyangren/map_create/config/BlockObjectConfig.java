package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.bean.Block;

/**
 * Created by zql on 16/2/29.
 *
 * @className BlockObjectConfig
 * @classUse 区块初始配置
 */
public class BlockObjectConfig implements BaseObjectConfig {
    /**
     * 大地图区块X坐标
     */
    private int xBlockInMap;
    /**
     * 大地图区块Y坐标
     */
    private int yBlockInMap;
    /**
     * 区块类型
     */
    private int type;
    /**
     * 所属州
     */
    private int province;
    /**
     * 区块是否可用
     */
    private boolean isEffective;

    public int getxBlockInMap() {
        return xBlockInMap;
    }

    public void setxBlockInMap(int xBlockInMap) {
        this.xBlockInMap = xBlockInMap;
    }

    public int getyBlockInMap() {
        return yBlockInMap;
    }

    public void setyBlockInMap(int yBlockInMap) {
        this.yBlockInMap = yBlockInMap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public boolean isEffective() {
        return isEffective;
    }

    public void setEffective(boolean effective) {
        isEffective = effective;
    }

    public int getId() {
        return Block.getId(xBlockInMap, yBlockInMap);
    }
}
