package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.util.IndexedEnum;

/**
 * Created by zql on 16/2/29.
 *
 * @className UnitType
 * @classUse
 */
public enum UnitType implements IndexedEnum {
    //空地
    clear(0, "草地", false),
    //草地
    grass(1, "草地", false),
    //山地
    mountain(2, "山地", true),
    //河流
    river(3, "河流", false),
    //农田
    farmland(4, "农田", false),
    //湖泊
    lakes(5, "湖泊", true),
    //林地
    forest(6, "林地", false);

    private int id = 0;
    private String imageName = "";
    private boolean isStop = false;

    private UnitType(int id, String imageName, boolean isStop) {
        this.id = id;
        this.imageName = imageName;
        this.isStop = isStop;
    }

    public int getIndex() {
        return id;
    }

    public String getImageName() {
        return imageName;
    }

    public boolean isStop() {
        return isStop;
    }


}
