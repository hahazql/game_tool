package com.hahazql.tools.i18n.impl;

import com.hahazql.tools.constants.CommonErrorLogInfo;
import com.hahazql.tools.helper.ErrorsUtil;
import com.hahazql.tools.helper.LogMgr;
import com.hahazql.tools.helper.PoiUtils;
import com.hahazql.tools.i18n.I18NDictionary;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 从Excel文件中加载数据,主键是Integer类型
 */
public class ExcelIntDictionaryImpl implements I18NDictionary<Integer, String> {

    private final String fileName;
    private final Map<Integer, String> data = new HashMap<Integer, String>();

    public ExcelIntDictionaryImpl(String fileName) {
        this.fileName = fileName;
        load();
    }

    public String read(Integer val) {
        String _val = data.get(val);
        if (_val == null) {
            return val.toString();
        } else {
            return _val;
        }
    }

    public void load() {
        HSSFWorkbook workbook = null;
        InputStream inp = null;
        try {
            inp = new FileInputStream(fileName);
            workbook = new HSSFWorkbook(new POIFSFileSystem(inp));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowNumber = sheet.getLastRowNum();
            for (int rowIdxForExcel = 0; rowIdxForExcel <= rowNumber; rowIdxForExcel++) {
                HSSFRow row = sheet.getRow(rowIdxForExcel);
                if (row == null) {
                    continue;
                } else {
                    int key = PoiUtils.getIntValue(row.getCell(0));
                    String value = PoiUtils.getStringValue(row.getCell(1));
                    if (key > 0) {
                        if (!data.containsKey(key)) {
                            data.put(key, value);
                        } else {
                            LogMgr.warn(ExcelIntDictionaryImpl.class, ErrorsUtil.error(CommonErrorLogInfo.CONFIG_DUP_FAIL, "Warn", fileName + "[key:" + key
                                    + ",value:" + value + "]"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogMgr.error(ExcelIntDictionaryImpl.class, String.format("multi-language(%s) excel load error", fileName));
            throw new RuntimeException(e);
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
