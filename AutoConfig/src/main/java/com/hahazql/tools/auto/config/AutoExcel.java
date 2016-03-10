package com.hahazql.tools.auto.config;

import java.io.Serializable;

/**
 * Created by zql on 2015/10/8.
 */
public class AutoExcel extends AutoParam implements Serializable {

    /**
     * 是否是数组
     */
    private boolean isArray;
    /**
     * 非数组时
     * 记录属性保存所在列的索引
     */
    private int cell;
    /**
     * 数组时
     * 记录属性所在表名的索引
     */
    private int sheetNameCell;
    /**
     * 数组时
     * 记录属性开始行的索引
     */
    private int rowStartCell;
    /**
     * 数组时
     * 记录属性结束行的索引
     */
    private int rowEndCell;

    public boolean isArray() {
        return isArray;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getSheetNameCell() {
        return sheetNameCell;
    }

    public void setSheetNameCell(int sheetNameCell) {
        this.sheetNameCell = sheetNameCell;
    }

    public int getRowStartCell() {
        return rowStartCell;
    }

    public void setRowStartCell(int rowStartCell) {
        this.rowStartCell = rowStartCell;
    }

    public int getRowEndCell() {
        return rowEndCell;
    }

    public void setRowEndCell(int rowEndCell) {
        this.rowEndCell = rowEndCell;
    }
}
