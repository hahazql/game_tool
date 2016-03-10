package com.digisky.muyangren.map_create.process;/**
 * Created by zql on 16/2/29.
 */

import com.digisky.muyangren.map_create.bean.Block;
import com.digisky.muyangren.map_create.bean.MapInit;
import com.digisky.muyangren.map_create.bean.Unit;
import com.digisky.muyangren.map_create.config.*;
import com.digisky.muyangren.map_create.util.LogMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zql on 16/2/29.
 *
 * @className BlockCreateL
 * @classUse
 */
public class BlockCreateL {
    public static MapInit createBlock(int xBlockInMap, int yBlockInMap, BlockObjectConfig blockConfig, RetainConfig retainConfig, TemplateConfig templateConfig, MapInit map) {
        Block block = new Block();
        //设置区块X坐标
        block.setxBlockInMap(xBlockInMap);
        //设置区块Y坐标
        block.setyBlockInMap(yBlockInMap);

        //根据区块配置初始化区块
        block = setBlockFromBlockConfig(blockConfig, block);

        LogMgr.debug(BlockCreateL.class, block.getId(xBlockInMap, yBlockInMap) + "号区块开始生成");
        //初始化区块中的单元格
        for (int xUnitInBlock = 0; xUnitInBlock < Block.xMaxUnitInBlock; xUnitInBlock++) {
            for (int yUnitInBlocK = 0; yUnitInBlocK < Block.yMaxUnitInBlock; yUnitInBlocK++) {
                Unit unit = block.initUnit(xUnitInBlock, yUnitInBlocK, retainConfig);
                map.setUnit(unit);
            }
        }

        //将区块信息加入到MAP中
        map.setBlock(xBlockInMap, yBlockInMap, block);


        LogMgr.debug(BlockCreateL.class, "开始生成摆件");
        //根据规则生成单元格
        unitCreate(block, getTemplateConfig(block, templateConfig), map);

        return map;
    }


    /**
     * 根据区块配置初始化区块
     *
     * @param blockConfig
     * @param block
     * @return
     */
    public static Block setBlockFromBlockConfig(BlockObjectConfig blockConfig, Block block) {
        if (blockConfig != null) {
            //设置区块类型
            block.setType(blockConfig.getType());
            //设置区块是否可用
            block.setEffective(blockConfig.isEffective());
        }
        return block;
    }

    /**
     * 获取使用模板
     *
     * @return
     */
    public static TemplateObjectConfig getTemplateConfig(Block block, TemplateConfig config) {
        return config.get(0);
    }


    /**
     * 根据规则生成单元格数据
     *
     * @param block
     * @param map
     * @return
     */
    public static void unitCreate(final Block block, TemplateObjectConfig template, MapInit map) {
        List<UnitCreateObjectConfig> createList = template.getCreateList();
        for (UnitCreateObjectConfig create : createList) {
            int num = template.getCreateNum(create.getId());
            List<Integer> freeUnit = (List<Integer>) block.getFreeUnit().clone();
            //循环生成指定数量
            for (int i = 0; i < num; i++) {
                if (!selectUnit(map, block, freeUnit, create))
                    break;
            }
        }
    }

    /**
     * 搜索符合条件的单元格并设置
     *
     * @param block
     * @param free
     * @param createConfig
     * @return
     */
    public static boolean selectUnit(MapInit map, final Block block, final List<Integer> free, UnitCreateObjectConfig createConfig) {
        //是否已经选到合适的格子
        boolean isSelect = true;
        int size = createConfig.getSize();
        int minNum = size * size;
        Random random = new Random(System.currentTimeMillis());
        while (1 > 0) {
            //当空余的格子不足以生成指定物时返回FALSE
            if (free.size() < minNum)
                return false;
            //抽取一个随机单元格
            int freeIndx = random.nextInt(free.size());
            int unitID = free.get(freeIndx);
            Unit unit = block.getUnitByID(unitID);
            //若抽取到的单元格被占用则重新执行
            if (unit.isOccupy()) {
                free.remove(unit.getId());
                continue;
            }
            int xUnitInBlock = unit.getxUnitInBlock();
            int yUnitInBlock = unit.getyUnitInBlock();
            //用于记录当前检验了的单元格
            List<Integer> tempUnitList = new ArrayList<Integer>();

            //检验单元格是否可用
            isSelect = validationUnit(size, block, xUnitInBlock, yUnitInBlock, tempUnitList);


            //将已经检测过了的单元格从空闲的格子中移除
            free.removeAll(tempUnitList);

            // 若找到了合适的位置
            if (isSelect) {
                for (Integer id : tempUnitList) {
                    Unit selectUnit = block.getUnitByID(id);
                    if (selectUnit == null) {
                        LogMgr.error(BlockCreateL.class, "生成单元格过程中出了逻辑错误,获取到了一个不存在的单元格");
                        free.addAll(tempUnitList);
                        continue;
                    }

                    //这些单元格被使用
                    block.removeFreeUnit(id);
                    //将所有的选中的单元格都设置为选中
                    selectUnit.setOccupy(true);
                    //将这些选中的单元格都设置为当前类型
                    selectUnit.setType(createConfig.getType().getIndex());
                    //设置这些单元格是否为刚体
                    selectUnit.setStop(createConfig.isStop());
                    //同步到地图中
                    map.setUnit(selectUnit);
                }
                //设置模型放置点
                setModule(tempUnitList, size, createConfig, map, block);
                break;
            } else //若未能找到合适的位置则重新查找
            {
                continue;
            }
        }
        return isSelect;
    }

    /**
     * 验证单元格是否可用
     *
     * @param size
     * @param block
     * @param xUnitInBlock
     * @param yUnitInBlock
     * @param tempUnitList
     * @return
     */
    private static boolean validationUnit(int size, Block block, int xUnitInBlock, int yUnitInBlock, List<Integer> tempUnitList) {
        boolean isSelect = true;
        //验证单元格有效性(循环检验所有需要的单元格)
        for (int offsetX = 0; offsetX < size; offsetX++) {
            int xUnitInBlockTemp = xUnitInBlock + offsetX;
            if (xUnitInBlockTemp >= Block.xMaxUnitInBlock) {//区块边界检测
                isSelect = false;
                break;
            }
            for (int offsetY = 0; offsetY < size; offsetY++) {
                int yUnitInBlockTemp = yUnitInBlock + offsetY;
                if (yUnitInBlockTemp >= Block.yMaxUnitInBlock) {//区块边界检测
                    isSelect = false;
                    break;
                }
                Unit tempUnit = block.getUnitByBlock(xUnitInBlockTemp, yUnitInBlockTemp);
                tempUnitList.add(tempUnit.getId());
                if (tempUnit.isOccupy()) {
                    isSelect = false;
                    break;
                }
            }
        }
        return isSelect;
    }


    /**
     * 设置模型放置点
     *
     * @param unitList
     * @param size
     * @param createConfig
     */
    private static void setModule(List<Integer> unitList, int size, UnitCreateObjectConfig createConfig, final MapInit map, final Block block) {
        int id = -1;
        switch (size) {
            case 1://1X1,2X2的模型直接放置在第一个单元格
            case 2:
                id = unitList.get(0);
                break;
            default:
                int mid = (size - 1) / 2;
                int index = mid * size + mid;
                id = unitList.get(index);
        }

        Unit unit = block.getUnitByID(id);
        unit.setModelType(createConfig.getModelType().getModel_id());
        map.setUnit(unit);
    }


}
