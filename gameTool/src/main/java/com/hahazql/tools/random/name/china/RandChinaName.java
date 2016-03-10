package com.hahazql.tools.random.name.china; /**
 * Created by zql on 15/12/17.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by zql on 15/12/17.
 *
 * @className RandChinaName
 * @classUse
 */
public class RandChinaName {
    private static final String DICT_CHINA_NAME = "/src/main/resources/dict/china/name.dict";
    private static final String DICT_CHINA_SURNAME = "/src/main/resources/dict/china/surname.dict";
    private static boolean isLoader = false;

    public static void load() {
        loaderSurName();
        loaderName();
        isLoader = true;
    }

    public static String random() {
        if (!isLoader)
            load();
        String surName = RandomChinaEntity.getSurName();
        Name name = RandomChinaEntity.getName();
        return surName + name.getName();
    }


    private static void loaderName() {
        String names = readFile(DICT_CHINA_NAME);
        String[] _name = names.split("&");
        for (String nameInfo : _name) {
            String[] info = nameInfo.split(" ");
            if (info.length < 3)
                continue;
            int weight = Integer.valueOf(info[0]);
            String name = info[1];
            String des = info[2];
            Name nameData = new Name();
            nameData.setWeight(weight);
            nameData.setName(name);
            nameData.setDes(des);
            RandomChinaEntity.addName(nameData);
        }
    }

    private static void loaderSurName() {
        String names = readFile(DICT_CHINA_SURNAME);
        names = names.replace("&", " ");
        String[] allName = names.split(" ");
        for (String name : allName) {
            name = name.replace(" ", "");
            if (name.isEmpty())
                continue;
            RandomChinaEntity.addSurName(name);
        }
    }

    public static String readFile(String filePath) {
        StringBuffer ret = new StringBuffer();
        try {
            String encoding = "UTF-8";
            File directory = new File("");
            File file = new File(directory.getAbsolutePath() + filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    ret.append(lineTxt + "&");
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return ret.toString();
    }
}
