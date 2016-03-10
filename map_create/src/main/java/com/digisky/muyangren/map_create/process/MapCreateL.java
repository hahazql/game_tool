package com.digisky.muyangren.map_create.process;/**
 * Created by zql on 16/2/29.
 */

import com.alibaba.fastjson.JSONObject;
import com.digisky.muyangren.map_create.bean.Block;
import com.digisky.muyangren.map_create.bean.MapInit;
import com.digisky.muyangren.map_create.bean.Unit;
import com.digisky.muyangren.map_create.config.BlockConfig;
import com.digisky.muyangren.map_create.config.ModelType;
import com.digisky.muyangren.map_create.config.RetainConfig;
import com.digisky.muyangren.map_create.config.TemplateConfig;
import com.digisky.muyangren.map_create.util.LogMgr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zql on 16/2/29.
 *
 * @className MapCreateL
 * @classUse
 */
public class MapCreateL {
    public static BlockConfig blockConfig = new BlockConfig();
    public static RetainConfig retainConfig = new RetainConfig();
    public static TemplateConfig templateConfig = new TemplateConfig();

    public static MapInit createMap() {
        MapInit map = new MapInit();
        LogMgr.debug(MapCreateL.class, "开始初始化区块信息");
        //初始化所有的区块
        for (int blockX = 0; blockX < MapInit.xMaxBlockInMap; blockX++) {
            for (int blockY = 0; blockY < MapInit.yMaxBlockInMap; blockY++) {
                map = BlockCreateL.createBlock(blockX, blockY, blockConfig.get(Block.getId(blockX, blockY)), retainConfig, templateConfig, map);
            }
        }
        return map;
    }

    private static String imagePath(String imageName) {
        return "image/" + imageName + ".jpg";
    }

    private static File getImageFile(String imageName) {
        return new File(imagePath(imageName));
    }

    /**
     * 地图展示
     *
     * @param map
     * @throws IOException
     */
    public static void printMap(MapInit map) throws IOException {
        //初始化所有类型的图片
        HashMap<Integer, File> unitImages = new HashMap<Integer, File>();
        for (ModelType modelType : ModelType.values()) {
            File file = getImageFile(modelType.getImageName());
            if (file.exists())
                unitImages.put(modelType.getIndex(), file);
        }

        int xNum = MapInit.xMaxUnitInMap;
        int yNum = MapInit.yMaxUnitInMap;
        int num = xNum * yNum;
        List<BufferedImage> buffimages = new ArrayList<BufferedImage>();

        for (Unit[] yUnits : map.getUnits()) {
            for (Unit unit : yUnits) {
                File file = unitImages.get(unit.getModelType());
                if (file == null)
                    LogMgr.error(MapCreateL.class, "生成地图时出错,没能找到对应图片文件,type : " + unit.getType());
                buffimages.add(ImageIO.read(file));
            }
        }

        int imageType = buffimages.get(0).getType();
        int imageWidth = buffimages.get(0).getWidth();
        int imageHeight = buffimages.get(0).getHeight();
        BufferedImage finalImg = new BufferedImage(imageWidth * xNum, imageHeight * yNum, imageType);

        int m = 0;
        for (int i = 0; i < xNum; i++) {
            for (int j = 0; j < yNum; j++) {
                finalImg.createGraphics().drawImage(buffimages.get(m), imageWidth * j, imageHeight * i, null);
                m += 1;
            }
        }

        ImageIO.write(finalImg, "jpg", new File("map_" + MapInit.xMaxBlockInMap + "_" + MapInit.yMaxBlockInMap + ".jpg"));
    }


    public static void printJsonFile(MapInit map) throws IOException {
        String filePath = "map_" + MapInit.xMaxBlockInMap + "_" + MapInit.yMaxBlockInMap + ".json";
        File file = new File(filePath);
        if (file.exists())
            file.delete();
        file.createNewFile();
        PrintStream ps = new PrintStream(new FileOutputStream(file));
        ps.println(toJson(map).toJSONString());
        ps.flush();
        ps.close();
    }

    public static JSONObject toJson(MapInit map) {
        JSONObject json = new JSONObject();

        for (Unit[] yUnits : map.getUnits()) {
            for (Unit unit : yUnits) {
                if (!unit.isOccupy())
                    continue;
                JSONObject value = new JSONObject();
                value.put("used", unit.isOccupy());
                if (unit.getModelType() > -1) {
                    value.put("id", unit.getModelType());
                }
                json.put(String.valueOf(unit.getId()), value);
            }
        }
        return json;
    }
}
