package com.digisky.muyangren.map_create.bean;/**
 * Created by zql on 16/2/29.
 */

/**
 * Created by zql on 16/2/29.
 *
 * @className Unit
 * @classUse 单元格
 */
public class Unit {
    //单元格类型
    private int type;
    //放入的模型(默认为0 无模型)
    private int modelType = -1;
    //是否占用
    private boolean isOccupy;
    //是否放置的阻挡物
    private boolean isStop;
    //X坐标
    private int xUnitInMap;
    //Y坐标
    private int yUnitInMap;
    //区块中的X坐标
    private int xUnitInBlock;
    //区块中的Y坐标
    private int yUnitInBlock;

    public static int getID(int x, int y) {
        return y * MapInit.xMaxBlockInMap * Block.xMaxUnitInBlock + x;
    }

    public int getId() {
        return yUnitInMap * MapInit.xMaxBlockInMap * Block.xMaxUnitInBlock + xUnitInMap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public boolean isOccupy() {
        return isOccupy;
    }

    public void setOccupy(boolean occupy) {
        isOccupy = occupy;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

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

    public int getxUnitInBlock() {
        return xUnitInBlock;
    }

    public void setxUnitInBlock(int xUnitInBlock) {
        this.xUnitInBlock = xUnitInBlock;
    }

    public int getyUnitInBlock() {
        return yUnitInBlock;
    }

    public void setyUnitInBlock(int yUnitInBlock) {
        this.yUnitInBlock = yUnitInBlock;
    }
}
