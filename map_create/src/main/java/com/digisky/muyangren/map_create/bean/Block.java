package com.digisky.muyangren.map_create.bean;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.config.GlobalConfig;
import com.digisky.muyangren.map_create.config.RetainConfig;
import com.digisky.muyangren.map_create.config.RetainObjectConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zql on 16/2/29.
 *
 * @className Block
 * @classUse 区块(18*18的单元格组成)
 */
public class Block {

    /**
     * 区块X轴的单元格数量
     */
    public static final int xMaxUnitInBlock = GlobalConfig.xMaxUnitInBlock;
    /**
     * 区块y轴的单元格数量
     */
    public static final int yMaxUnitInBlock = GlobalConfig.yMaxUnitInBlock;


    /**
     * 大地图区块X坐标
     * (已1个区块作为1个刻度的坐标系)
     */
    private int xBlockInMap = 0;
    /**
     * 大地图区块Y坐标
     * (已1个区块作为1个刻度的坐标系)
     */
    private int yBlockInMap = 0;

    /**
     * KEY:单元格ID(由单元格在区块中的Y轴坐标*X轴最大坐标+X轴坐标)
     * VALUE: 单元格
     */
    private Map<Integer, WeakReference<Unit>> _ix_id = new HashMap<Integer, WeakReference<Unit>>();
    /**
     * 单元格列表
     */
    private int[][] unitList = new int[xMaxUnitInBlock][yMaxUnitInBlock];

    /**
     * 可使用的单元格
     */
    private ArrayList<Integer> freeUnit = new ArrayList<Integer>();
    /**
     * 已占用的单元格
     */
    private ArrayList<Integer> occupyUnit = new ArrayList<Integer>();

    /**
     * 区块类型
     */
    private int type = 0;

    /**
     * 区块是否为有效区块
     */
    private boolean isEffective = true;

    public boolean isEffective() {
        return isEffective;
    }

    public void setEffective(boolean effective) {
        isEffective = effective;
    }

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

    public Map<Integer, WeakReference<Unit>> get_ix_id() {
        return _ix_id;
    }

    public void set_ix_id(Map<Integer, WeakReference<Unit>> _ix_id) {
        this._ix_id = _ix_id;
    }

    public int[][] getUnitList() {
        return unitList;
    }

    public void setUnitList(int[][] unitList) {
        this.unitList = unitList;
    }

    public ArrayList<Integer> getFreeUnit() {
        return freeUnit;
    }

    public void removeFreeUnit(Integer id) {
        this.freeUnit.remove(id);
    }

    public void addOccupyUnit(Integer id) {
        this.occupyUnit.add(id);
    }

    public void setFreeUnit(ArrayList<Integer> freeUnit) {
        this.freeUnit = freeUnit;
    }

    public ArrayList<Integer> getOccupyUnit() {
        return occupyUnit;
    }

    public void setOccupyUnit(ArrayList<Integer> occupyUnit) {
        this.occupyUnit = occupyUnit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 根据区块内坐标返回单元格
     *
     * @param blockX
     * @param blockY
     * @return
     */
    public Unit getUnitByBlock(int blockX, int blockY) {
        int id = Unit.getID(getMapX(blockX), getMapY(blockY));
        WeakReference<Unit> reference = this._ix_id.get(id);
        if (reference == null)
            return null;
        return reference.get();
    }

    public Unit getUnitByID(int id) {
        WeakReference<Unit> reference = this._ix_id.get(id);
        if (reference == null)
            return null;
        return reference.get();
    }

    /**
     * 根据区块内X,Y坐标创建单元格
     *
     * @param xUnitInBlock 区块中的X坐标
     * @param yUnitInBlock 区块中的Y坐标
     * @return
     */
    public Unit initUnit(int xUnitInBlock, int yUnitInBlock, RetainConfig config) {
        Unit unit = new Unit();
        unit.setxUnitInBlock(xUnitInBlock);
        unit.setyUnitInBlock(yUnitInBlock);
        unit.setxUnitInMap(getMapX(xUnitInBlock));
        unit.setyUnitInMap(getMapY(yUnitInBlock));
        if (!isEffective) {
            unit.setOccupy(true);
            unit.setStop(true);
        } else {
            RetainObjectConfig retainConfig = config.get(unit.getId());
            if (retainConfig != null) {
                unit.setOccupy(true);
            }
        }
        addUnit(unit);
        return unit;
    }

    public void addUnit(Unit unit) {
        int id = unit.getId();
        this._ix_id.put(id, new WeakReference<Unit>(unit));
        this.unitList[unit.getxUnitInBlock()][unit.getyUnitInBlock()] = id;
        if (!unit.isOccupy())
            this.freeUnit.add(id);
        else
            this.occupyUnit.add(id);
    }


    /**
     * 根据区块内X坐标换算大地图X坐标
     *
     * @param xUnitInBlock 区块内X坐标
     * @return
     */
    private int getMapX(int xUnitInBlock) {
        return xBlockInMap * xMaxUnitInBlock + xUnitInBlock;
    }

    /**
     * 根据区块内Y坐标换算大地图Y坐标
     *
     * @param yUnitInBlock 区块内Y坐标
     * @return
     */
    private int getMapY(int yUnitInBlock) {
        return yBlockInMap * yMaxUnitInBlock + yUnitInBlock;
    }


    public static int getId(int xBlockInMap, int yBlockInMap) {
        return xBlockInMap + yBlockInMap * MapInit.xMaxBlockInMap;
    }

}
