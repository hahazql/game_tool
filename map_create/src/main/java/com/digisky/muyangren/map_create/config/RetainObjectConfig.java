package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */


import com.digisky.muyangren.map_create.bean.Unit;

/**
 * Created by zql on 16/2/29.
 *
 * @className RetainObjectConfig
 * @classUse 保留用地配置
 */
public class RetainObjectConfig implements BaseObjectConfig {
    /**
     * 大地图X坐标
     */
    private int xUnitInMap;
    /**
     * 大地图Y坐标
     */
    private int yUnitInMap;

    public int getxUnitInMap() {
        return xUnitInMap;
    }

    public void setxUnitInMap(int xUnitInMap) {
        this.xUnitInMap = xUnitInMap;
    }

    public int getyUnitInMap() {
        return yUnitInMap;
    }

    public void setyUnitInMap(int yUnitInMap) {
        this.yUnitInMap = yUnitInMap;
    }


    public int getId() {
        return Unit.getID(xUnitInMap, yUnitInMap);
    }
}
