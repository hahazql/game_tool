package com.hahazql.tools.i18n.impl;


import com.hahazql.tools.constants.CommonErrorLogInfo;
import com.hahazql.tools.helper.Assert;
import com.hahazql.tools.helper.ErrorsUtil;
import com.hahazql.tools.helper.LogMgr;
import com.hahazql.tools.helper.PoiUtils;
import com.hahazql.tools.i18n.I18NDictionary;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从Excel文件中加载数据,主键是String类型
 */
public class ExcelDictionaryImpl implements I18NDictionary<String, String> {
    private static final Pattern categoryPattern = Pattern.compile("[a-zA-Z0-9]+");
    private final String[] fileNames;
    private final Map<String, String> data = new HashMap<String, String>();

    public ExcelDictionaryImpl(String[] fileNames) {
        this.fileNames = fileNames;
        load();
    }

    public ExcelDictionaryImpl() {
        this.fileNames = new String[]{""};
    }

    /**
     * 用于将以名字分类的文件的分类名提取出来，规则为，取路径字串的最后一个File.seperator的下一个字符开始到遇到的第一个非字母字符结束
     * 例如：d:\excel\abc.xls取abc d:\zh_CN\lang\npcs_lang.xls取npcs
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getCategory(String filePath) {
        Assert.notNull(filePath);
        int lastSep = filePath.lastIndexOf(File.separator);
        Matcher m = categoryPattern.matcher(filePath);
        if (m.find(lastSep + 1)) {
            return m.group();
        } else {
            throw new IllegalArgumentException(filePath);
        }
    }

    public static void main(String[] args) {
        System.out.println(getCategory("d:\\work\\webzt\\quests.xls"));
        System.out.println(getCategory("d:\\work\\webzt\\quests_lang.xls"));
    }

    public Map<String, String> getData() {
        return data;
    }

    /**
     * 将参数的dataMap合并到自己的map中取
     *
     * @param another
     */
    public void putAll(ExcelDictionaryImpl another) {
        data.putAll(another.data);
    }

    public String read(String val) {
        if (val != null) {
            val = val.trim();
        }
        String _value = data.get(val);
        if (_value != null) {
            return _value;
        } else {
            return val;
        }
    }

    public void load() {

        for (String fileName : fileNames) {
            HSSFWorkbook workbook = null;
            InputStream inp = null;
            try {
                inp = new FileInputStream(fileName);
                String category = getCategory(fileName);
                workbook = new HSSFWorkbook(new POIFSFileSystem(inp));
                HSSFSheet sheet = workbook.getSheetAt(0);
                int rowNumber = sheet.getLastRowNum();
                for (int rowIdxForExcel = 0; rowIdxForExcel <= rowNumber; rowIdxForExcel++) {
                    HSSFRow row = sheet.getRow(rowIdxForExcel);
                    if (row == null) {
                        continue;
                    } else {
                        String key = PoiUtils.getStringValue(row.getCell(0));
                        // 转化为全局唯一的key
                        key = category + "_" + key;
                        String value = PoiUtils.getStringValue(row.getCell(1));
                        if (key != null && key.trim().length() > 0) {
                            if (!data.containsKey(key)) {
                                data.put(key, value);
                            } else {
                                LogMgr.warn(ExcelDictionaryImpl.class, ErrorsUtil.error(CommonErrorLogInfo.CONFIG_DUP_FAIL, "Warn", fileName
                                        + "[key:" + key + ",value:" + value + "]"));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                String msg = String.format("multi-language(%s) excel load error", fileNames.toString());
                LogMgr.error(ExcelDictionaryImpl.class, msg);
            } finally {
                if (inp != null) {
                    try {
                        inp.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
