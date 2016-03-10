package com.digisky.muyangren.map_create.bean;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.config.GlobalConfig;

/**
 * Created by zql on 16/2/29.
 *
 * @className MapInit
 * @classUse 大地图
 */
public class MapInit {
    /**
     * x轴最大区块数量
     */
    public static final int xMaxBlockInMap = GlobalConfig.xMaxBlockInMap;
    /**
     * Y轴最大区块数量
     */
    public static final int yMaxBlockInMap = GlobalConfig.yMaxBlockInMap;

    /**
     * x轴最大单元格数量
     */
    public static final int xMaxUnitInMap = xMaxBlockInMap * Block.xMaxUnitInBlock;
    /**
     * y轴最大单元格数量
     */
    public static final int yMaxUnitInMap = yMaxBlockInMap * Block.yMaxUnitInBlock;

    /**
     * 区块视图
     */
    private Block[][] blocks = new Block[xMaxBlockInMap][yMaxBlockInMap];

    /**
     * 单元格视图
     */
    private Unit[][] units = new Unit[xMaxUnitInMap][yMaxUnitInMap];


    public static int getxMaxBlockInMap() {
        return xMaxBlockInMap;
    }

    public static int getyMaxBlockInMap() {
        return yMaxBlockInMap;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public Block getBlock(int xBlockInMap, int yBlockInMap) {
        return blocks[xBlockInMap][yBlockInMap];
    }

    public void setBlock(int xBlockInMap, int yBlockInMap, Block block) {
        this.blocks[xBlockInMap][yBlockInMap] = block;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public Unit[][] getUnits() {
        return units;
    }

    public Unit getUnit(int x, int y) {
        return this.units[x][y];
    }

    public void setUnit(Unit unit) {
        this.units[unit.getxUnitInMap()][unit.getyUnitInMap()] = unit;
    }

    public void setUnits(Unit[][] units) {
        this.units = units;
    }
}
