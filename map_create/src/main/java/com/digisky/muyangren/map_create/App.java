package com.digisky.muyangren.map_create;

import com.digisky.muyangren.map_create.bean.MapInit;
import com.digisky.muyangren.map_create.process.MapCreateL;
import com.digisky.muyangren.map_create.util.LogMgr;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        LogMgr.debug(App.class, "开始生成地图");
        MapInit map = MapCreateL.createMap();
        LogMgr.debug(App.class, "开始生成json文件");
        MapCreateL.printJsonFile(map);
//        LogMgr.debug(App.class, "开始将生成的地图写入到图片中");
//        MapCreateL.printMap(map);
//        LogMgr.debug(App.class, "图片已生成，地图生成程序结束");
    }
}
