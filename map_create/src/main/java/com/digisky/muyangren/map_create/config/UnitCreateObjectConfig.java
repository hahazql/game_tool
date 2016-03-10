package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.util.IndexedEnum;

/**
 * Created by zql on 16/2/29.
 *
 * @className UnitCreateObjectConfig
 * @classUse 用于单元格生成配置
 */
public class UnitCreateObjectConfig implements Comparable<UnitCreateObjectConfig>, BaseObjectConfig {
    private int id;
    /**
     * 占据的单元类型
     */
    private UnitType type;
    /**
     * 占据的单元格数量
     * size*size个单元格
     */
    private int size;
    /**
     * 优先级
     * 优先级高则优先生成在地图上
     */
    private int priority;

    /**
     * 是否是阻挡
     */
    private boolean isStop;

    /**
     * 模型类型
     */
    private ModelType modelType;

    public UnitCreateObjectConfig() {
    }

    public UnitCreateObjectConfig(int id, UnitType type, int size, int priority, ModelType modelType) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.priority = priority;
        this.isStop = type.isStop();
        this.modelType = modelType;
    }

    public UnitCreateObjectConfig(int id, int type, int size, int priority, boolean isStop, int modelType) {
        this.id = id;
        this.type = IndexedEnum.IndexedEnumUtil.getEnum(type, UnitType.values());
        this.size = size;
        this.priority = priority;
        this.isStop = isStop;
        this.modelType = IndexedEnum.IndexedEnumUtil.getEnum(modelType, ModelType.values());
        ;
    }

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int compareTo(UnitCreateObjectConfig o) {
        if (this.priority >= o.priority)
            return 1;
        else
            return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }
}
