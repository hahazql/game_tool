package com.hahazql.tools.random.name.china;/**
 * Created by zql on 15/12/17.
 */

import com.hahazql.tools.random.RandHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zql on 15/12/17.
 *
 * @className RandomChinaEntity
 * @classUse 缓存随机生成中文名相关数据
 */
public class RandomChinaEntity {
    private static HashMap<Integer, List<Name>> _ix_name = new HashMap<Integer, List<Name>>();

    private static ArrayList<String> _ix_surName = new ArrayList<String>();


    public static void addName(Name name) {
        List<Name> list = _ix_name.get(name.getWeight());
        if (list == null)
            list = new ArrayList<Name>();
        list.add(name);
        _ix_name.put(name.getWeight(), list);
    }

    public static void addSurName(String name) {
        _ix_surName.add(name);
    }

    public static String getSurName() {
        int surNameRandom = RandHelper.randInt(_ix_surName.size() - 1);
        String surName = _ix_surName.get(surNameRandom);
        return surName;
    }

    public static int maxRandoSurName() {
        return _ix_surName.size() - 1;
    }

    public static Name getName() {
        List<Integer> rateList = new ArrayList<Integer>();
        rateList.addAll(_ix_name.keySet());
        int rate = RandHelper.getRate(rateList);
        List<Name> list = _ix_name.get(rate + 1);
        int nameRandom = RandHelper.randInt(list.size() - 1);
        return list.get(nameRandom);
    }
}
