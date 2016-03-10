package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.util.IndexedEnum;

/**
 * Created by zql on 16/2/29.
 *
 * @className ModelType
 * @classUse
 */
public enum ModelType implements IndexedEnum {
    //空地
    clear(0, "草地"),
    //草地
    grass(1, "草地"),
    //山地
    mountain(2, "山地"),
    //河流
    river(3, "河流"),
    //农田
    farmland(4, "农田"),
    //湖泊
    lakes(5, "湖泊"),
    //林地
    forest(6, "林地", 6),
    //山地2x2
    mountain_2(7, "山地2", 4),
    //山地3x3
    mountain_3(8, "山地3"),
    //林地_2
    forest_2(9, "林地2", 7),
    //林地_3
    forest_3(10, "林地3", 8);

    private int id = 0;
    private int model_id = -1;
    private String imageName = "";

    private ModelType(int id, String imageName) {
        this.id = id;
        this.imageName = imageName;
        this.model_id = -1;
    }

    private ModelType(int id, String imageName, int model_id) {
        this.id = id;
        this.imageName = imageName;
        this.model_id = model_id;
    }

    public int getIndex() {
        return id;
    }

    public int getModel_id() {
        return model_id;
    }

    public String getImageName() {
        return imageName;
    }

}
