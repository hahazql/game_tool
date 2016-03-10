package com.hahazql.tools.random.name.china;/**
 * Created by zql on 15/12/17.
 */

/**
 * Created by zql on 15/12/17.
 *
 * @className Name
 * @classUse 用于保存姓名中名字相关的信息
 */
public class Name {
    /**
     * 取此名的权重
     */
    private int weight;
    /**
     * 名
     */
    private String name;
    /**
     * 此名字的含义
     */
    private String des;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
