package com.hahazql.tools.auto.config;

/**
 * Created by zql on 2015/9/28.
 */

import com.alibaba.fastjson.parser.ParserConfig;
import com.hahazql.tools.auto.config.data.ClassConfigData;
import com.hahazql.tools.auto.config.data.FieldConfigData;
import com.hahazql.tools.auto.config.data.PackageConfigData;
import com.hahazql.tools.clazz.ClassLoadUtil;
import com.hahazql.tools.clazz.ClassUtil;
import com.hahazql.tools.clazz.PackageUtil;
import com.hahazql.tools.clazz.TypeUtils;
import com.hahazql.tools.exception.BaseException;
import com.hahazql.tools.helper.ArrayUtil;
import com.hahazql.tools.helper.StringUtils;
import com.hahazql.tools.io.IOTinyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 配置自动生成
 */
public class AutoConfigUtil {
    /**
     * 允许的属性类型
     */
    public static final Class[] fieldTypeList = new Class[]{String.class, Integer.class,
            Long.class, Boolean.class, Float.class,
            int.class, long.class, float.class, double.class, boolean.class};

    /**
     * 将JAR包loader进来并遍历整个项目生成配置
     *
     * @param jarPath JAR包路径
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws MalformedURLException
     * @throws NoSuchFileException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    public static ArrayList<AutoConfig> loadConfig(String jarPath) throws IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        ArrayList<AutoConfig> list = new ArrayList<AutoConfig>();
        for (Class clazz : PackageUtil.getClassFromJar(jarPath)) {
            if (clazz != null) {
                boolean isAnnontation = clazz.isAnnotationPresent(ClazzConfig.class);
                if (!isAnnontation)
                    continue;
                ClazzConfig clazzConfig = (ClazzConfig) clazz.getAnnotation(ClazzConfig.class);
                list.add(initConfigClazz(clazz, clazzConfig));
            }
        }
        return list;
    }

    private static ArrayList<AutoConfig> loadConfigFromClassName(List<String> clazzList) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        ArrayList<AutoConfig> list = new ArrayList<AutoConfig>();
        for (String className : clazzList) {
            Class clazz = null;
            try {
                clazz = Class.forName(className);
                if (clazz != null) {
                    boolean isAnnontation = clazz.isAnnotationPresent(ClazzConfig.class);
                    if (!isAnnontation)
                        continue;
                    ClazzConfig clazzConfig = (ClazzConfig) clazz.getAnnotation(ClazzConfig.class);
                    list.add(initConfigClazz(clazz, clazzConfig));
                }
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        return list;
    }

    public static ArrayList<AutoConfig> loadConfigFromSrc() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        return loadConfigFromClassName(PackageUtil.getSrcClassName());
    }


    public static ArrayList<AutoConfig> loadConfigFromPackage(String packageName) throws IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return loadConfigFromClassName(PackageUtil.getClassName(packageName, true));
    }


    /**
     * 为Class文件生成配置文件
     *
     * @param classDirPath Class文件存放的文件夹路径
     * @param className    Class名
     * @return
     * @throws MalformedURLException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static AutoConfig loadClassConfig(String classDirPath, String className) throws MalformedURLException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        ClassLoadUtil.loadClassFile(classDirPath, className);
        return loadClass(className);
    }

    /**
     * 根据Class导出配置
     *
     * @param className Class名
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static AutoConfig loadClass(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(className);
        if (!clazz.isAnnotationPresent(ClazzConfig.class))
            throw (new RuntimeException("解析的Class没有ClazzConfig注解"));
        ClazzConfig clazzConfig = (ClazzConfig) clazz.getAnnotation(ClazzConfig.class);
        AutoConfig config = initConfigClazz(clazz, clazzConfig);
        return config;
    }


    /**
     * 生成Class的配置
     *
     * @param configClazz     待生成的class类
     * @param clazzAnnotation class类上的ClazzConfig注解
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static AutoConfig initConfigClazz(Class configClazz, ClazzConfig clazzAnnotation) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        AutoConfig autoConfig = new AutoConfig(clazzAnnotation);
        autoConfig.setFieldList(getFieldConfig(configClazz));
        autoConfig.setClazz(configClazz);
        return autoConfig;
    }

    /**
     * 获取类的属性配置
     *
     * @param configClazz 带获取的Class
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static ArrayList<AutoFieldConfig> getFieldConfig(Class configClazz) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        ArrayList<AutoFieldConfig> ret = new ArrayList<AutoFieldConfig>();
        for (Field field : configClazz.getFields()) {
            if (!field.isAnnotationPresent(FieldConfig.class))
                continue;
            ret.add(initFieldConfig(field));
        }
        return ret;
    }

    private static AutoFieldConfig initFieldConfig(Field field, FieldConfigData data) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return initFieldConfig(field, data.getShowName(), data.getShowDesc(), data.getRegular(), data.getElementClassName());
    }

    private static AutoFieldConfig initFieldConfigFromExternJar(Field field, FieldConfigData data, PackageConfigData packageConfigData) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return initFieldConfigFromExternJar(field, data.getShowName(), data.getShowDesc(), data.getRegular(), data.getElementClassName(), packageConfigData);
    }

    private static AutoFieldConfig initFieldConfigFromExternJar(Field field, String showName, String showDesc, String configRegular, String elementClassName, PackageConfigData packageConfigData) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        AutoFieldConfig autoFieldConfig = new AutoFieldConfig();
        String configName = showName;
        if (StringUtils.isEmpty(configName))
            configName = field.getName();
        autoFieldConfig.setFieldName(configName);
        autoFieldConfig.setFieldDesc(showDesc);
        autoFieldConfig.setRegular(configRegular);
        autoFieldConfig.setName(field.getName());
        Class fieldTypeClazz = field.getType();
        autoFieldConfig.setFieldType(fieldTypeClazz);
        if (fieldTypeClazz.isArray())//若属性为数组
        {
            autoFieldConfig.setIsArray(true);
            Class elementClazz = Class.forName(elementClassName, true, packageConfigData.getClassLoader());
            ClassConfigData configData = packageConfigData.get(elementClassName);
            if (configData != null) {
                autoFieldConfig.setElementClazz(elementClazz);
                //需要生成数组元素的配置
                autoFieldConfig.setFieldConfig(initAutoConfigFromExternJar(configData, packageConfigData));
            } else if (ArrayUtil.contains(fieldTypeList, fieldTypeClazz)) {
                autoFieldConfig.setElementClazz(elementClazz);
                return autoFieldConfig;
            }
        }
        return autoFieldConfig;
    }

    private static AutoFieldConfig initFieldConfig(Field field, String showName, String showDesc, String configRegular, String elementClassName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        AutoFieldConfig autoFieldConfig = new AutoFieldConfig();
        String configName = showName;
        if (StringUtils.isEmpty(configName))
            configName = field.getName();
        autoFieldConfig.setFieldName(configName);
        autoFieldConfig.setFieldDesc(showDesc);
        autoFieldConfig.setRegular(configRegular);
        autoFieldConfig.setName(field.getName());
        Class fieldTypeClazz = field.getType();
        autoFieldConfig.setFieldType(fieldTypeClazz);
        if (fieldTypeClazz.isArray())//若属性为数组
        {
            autoFieldConfig.setIsArray(true);
            Class elementClazz = Class.forName(elementClassName);
            if (elementClazz.isAnnotationPresent(ClazzConfig.class)) {
                autoFieldConfig.setElementClazz(elementClazz);
                ClazzConfig elementClazzConfig = (ClazzConfig) elementClazz.getAnnotation(ClazzConfig.class);
                //需要生成数组元素的配置
                autoFieldConfig.setFieldConfig(initConfigClazz(elementClazz, elementClazzConfig));
            } else if (ArrayUtil.contains(fieldTypeList, fieldTypeClazz)) {
                autoFieldConfig.setElementClazz(elementClazz);
                return autoFieldConfig;
            }
        }
        return autoFieldConfig;
    }

    /**
     * 将属性转为属性配置
     *
     * @param field 属性
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static AutoFieldConfig initFieldConfig(Field field) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        if (field == null)
            return null;
        FieldConfig config = field.getAnnotation(FieldConfig.class);
        AutoFieldConfig autoFieldConfig = new AutoFieldConfig();
        String configName = config.value();
        if (StringUtils.isEmpty(configName))
            configName = field.getName();
        autoFieldConfig.setFieldName(configName);
        autoFieldConfig.setFieldDesc(config.desc());
        autoFieldConfig.setRegular(config.regular());
        autoFieldConfig.setName(field.getName());
        Class fieldTypeClazz = field.getType();
        autoFieldConfig.setFieldType(fieldTypeClazz);
        if (fieldTypeClazz.isArray())//若属性为数组
        {
            autoFieldConfig.setIsArray(true);
            Class elementClazz = config.elementClazz();
            if (elementClazz.isAnnotationPresent(ClazzConfig.class)) {
                autoFieldConfig.setElementClazz(elementClazz);
                ClazzConfig elementClazzConfig = (ClazzConfig) elementClazz.getAnnotation(ClazzConfig.class);
                //需要生成数组元素的配置
                autoFieldConfig.setFieldConfig(initConfigClazz(elementClazz, elementClazzConfig));
            } else if (ArrayUtil.contains(fieldTypeList, fieldTypeClazz)) {
                autoFieldConfig.setElementClazz(elementClazz);
                return autoFieldConfig;
            }
        }
        return autoFieldConfig;
    }

    /**
     * 根据配置创建XML节点数据
     *
     * @param config      类的自动装配Config
     * @param list        配置类列表
     * @param rootElement 根节点
     * @param <T>         配置类
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> void createXMLElement(AutoConfig config, T[] list, Element rootElement, boolean isIgnoreError) throws InvocationTargetException, IllegalAccessException, BaseException {
        for (T t : list) {
            Element element = rootElement.addElement(config.getConfigName());
            element.addAttribute("desc", config.getConfigDesc());
            for (Object _fieldConfig : config.getFieldList()) {
                AutoFieldConfig fieldConfig = (AutoFieldConfig) _fieldConfig;
                if (!fieldConfig.isArray())//非数组情况直接使用
                {
                    Object object = null;
                    try {
                        object = ClassUtil.getFieldValue(t, fieldConfig.getName(), fieldConfig.getFieldType());
                        Element fieldElement = addElement(element, fieldConfig.getName(), fieldConfig.fieldDesc);
                        fieldElement.setText(object.toString());
                    } catch (Exception e) {
                        if (!isIgnoreError)
                            throw (new BaseException(e.getCause()));
                    }
                } else {
                    Object[] elementList = null;
                    try {
                        elementList = (Object[]) ClassUtil.getFieldValue(t, fieldConfig.getName(), fieldConfig.getFieldType());
                        Element fieldElement = addElement(element, fieldConfig.getName(), fieldConfig.fieldDesc);
                        createXMLElement(fieldConfig.getFieldConfig(), elementList, fieldElement, isIgnoreError);
                    } catch (Exception e) {
                        if (!isIgnoreError)
                            throw (new BaseException(e.getCause()));
                    }
                }
            }
        }
    }

    private static Element addElement(Element rootElement, String name, String desc) {
        Element fieldElement = rootElement.addElement(name);
        fieldElement.addAttribute("name", name);
        fieldElement.addAttribute("desc", desc);
        return fieldElement;
    }


    /**
     * 创建XML配置
     *
     * @param classPath  类路径
     * @param configList 配置列表
     * @param <T>        配置的类
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T extends Comparable> String createXML(String classPath, List<T> configList) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, BaseException {
        AutoConfig config = AutoConfigUtil.loadClass(classPath);
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement(classPath);
        document.setRootElement(rootElement);
        rootElement.addAttribute("desc", config.getConfigDirName());
        AutoConfigUtil.createXMLElement(config, configList.toArray(), rootElement, false);
        return document.asXML();
    }

    public static <T extends Comparable> void createXML(AutoConfig config, List<T> configList, String xmlPath) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, BaseException {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement(config.getClazz().getName());
        document.setRootElement(rootElement);
        rootElement.addAttribute("desc", config.getConfigDirName());
        AutoConfigUtil.createXMLElement(config, configList.toArray(), rootElement, false);
        IOTinyUtils.createFile(xmlPath);
        File file = new File(xmlPath);
        IOTinyUtils.save(file, document.asXML(), "utf-8");
    }

    public static <T extends Comparable> void createXML(AutoConfig config, List<T> configList, String xmlPath, boolean isIgnoreError) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, BaseException {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement(config.getClazz().getName());
        document.setRootElement(rootElement);
        rootElement.addAttribute("desc", config.getConfigDirName());
        AutoConfigUtil.createXMLElement(config, configList.toArray(), rootElement, isIgnoreError);
        IOTinyUtils.createFile(xmlPath);
        File file = new File(xmlPath);
        IOTinyUtils.save(file, document.asXML(), "utf-8");
    }

    /**
     * 读取XML节点
     *
     * @param config      AutoConfig
     * @param clazz       生成的类
     * @param rootElement 开始的节点
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws BaseException
     * @throws InvocationTargetException
     */
    public static <T extends Comparable> T readXMLElement(AutoConfig config, Class<T> clazz, Element rootElement, boolean isIgnoreError) throws IllegalAccessException, InstantiationException, BaseException, InvocationTargetException {
        HashMap<String, AutoFieldConfig> map = new HashMap<String, AutoFieldConfig>();
        T t = (T) clazz.newInstance();
        List elements = rootElement.elements();
        for (AutoFieldConfig fieldConfig : (ArrayList<AutoFieldConfig>) config.getFieldList()) {
            map.put(fieldConfig.getName(), fieldConfig);
            if (fieldConfig.isArray())
                map.put(fieldConfig.getFieldConfig().getConfigName(), fieldConfig);
        }
        if (elements.size() == 0)
            return null;
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            AutoFieldConfig fieldConfig = map.get(element.getName());//获取对应属性配置
            if (fieldConfig == null)
                continue;
            if (fieldConfig.isArray())//若属性为数组
            {
                List _elements = element.elements();//遍历子节点
                Object array = Array.newInstance(fieldConfig.getElementClazz(), _elements.size());//创建属性数组
                int nowIndex = 0;
                for (Iterator _it = _elements.iterator(); _it.hasNext() && _elements.size() > 0; ) {
                    Element _element = (Element) _it.next();
                    if (_element.getName() != fieldConfig.getFieldConfig().getConfigName())//只获取符合标准的节点
                        continue;
                    Array.set(array, nowIndex, readXMLElement(fieldConfig.getFieldConfig(), fieldConfig.getElementClazz(), _element, isIgnoreError));
                }
                try {
                    ClassUtil.setFieldValue(t, fieldConfig.getName(), array);
                } catch (Exception e) {
                    if (!isIgnoreError)
                        throw (new BaseException(e.getCause()));
                }
            } else {
                try {
                    ClassUtil.setFieldValue(t, fieldConfig.getName(), TypeUtils.cast(element.getText(), fieldConfig.getFieldType(), ParserConfig.getGlobalInstance()));
                } catch (Exception e) {
                    if (!isIgnoreError)
                        throw (new BaseException(e.getCause()));
                }
            }
        }
        return t;
    }

    /***
     * 读取XML
     *
     * @param clazz 生成的配置类
     * @param xml   xml内容
     * @param <T>   配置类泛型
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BaseException
     * @throws DocumentException
     */
    public static <T extends Comparable> List<Comparable> readXml(Class<T> clazz, String xml) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, BaseException, DocumentException {
        AutoConfig config = AutoConfigUtil.loadClass(clazz.getName());
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        List<Comparable> list = new ArrayList<Comparable>();
        List elements = rootElement.elements();
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            T t = readXMLElement(config, clazz, element, false);
            list.add(t);
        }
        return list;
    }

    public static <T extends Comparable> List<Comparable> readXml(AutoConfig config, Class<T> clazz, String xml) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, BaseException, DocumentException {
        return readXml(config, clazz, xml, false);
    }

    public static <T extends Comparable> List<Comparable> readXml(AutoConfig config, Class<T> clazz, String xml, boolean isIgnoreError) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, BaseException, DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        List<Comparable> list = new ArrayList<Comparable>();
        List elements = rootElement.elements();
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            T t = readXMLElement(config, clazz, element, isIgnoreError);
            list.add(t);
        }
        return list;
    }

    /**
     * 将配置写入到Excel中
     *
     * @param configList 配置列表
     * @param clazz      配置类
     * @param excelPath  写入路径
     * @param <T>
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static <T> AutoConfig<AutoExcel> writeExcel(List<T> configList, Class<T> clazz, String excelPath) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException {
        AutoConfig<AutoExcel> autoConfig = AutoConfigUtil.loadClass(clazz.getName());
        HSSFWorkbook book = new HSSFWorkbook(); //建立新HSSFWorkbook对象
        HSSFSheet sheet = book.createSheet(autoConfig.getConfigName()); //建立新的sheet对象
        _instanceExcel(book, sheet, autoConfig);//初始化所有的列
        int row = sheet.getLastRowNum();
        for (T t : configList) {
            _writeExcel(book, sheet, autoConfig, clazz, t, row);
            row++;
        }
        File file = new File(excelPath);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOut = new FileOutputStream(excelPath);
        book.write(fileOut);
        fileOut.close();
        return autoConfig;
    }


    public static <T> AutoConfig<AutoExcel> writeExcel(List configList, Class<T> clazz, AutoConfig<AutoExcel> autoConfig, String excelPath) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException {
        HSSFWorkbook book = new HSSFWorkbook(); //建立新HSSFWorkbook对象
        HSSFSheet sheet = book.createSheet(autoConfig.getConfigName()); //建立新的sheet对象
        _instanceExcel(book, sheet, autoConfig);//初始化所有的列
        int row = sheet.getLastRowNum() + 1;
        for (Object t : configList) {
            _writeExcel(book, sheet, autoConfig, clazz, (T) t, row);
            row++;
        }
        File file = new File(excelPath);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOut = new FileOutputStream(excelPath);
        book.write(fileOut);
        fileOut.close();
        return autoConfig;
    }


    public static <T> AutoConfig<AutoExcel> writeExcel(List configList, Class<T> clazz, AutoConfig<AutoExcel> autoConfig, String excelPath, boolean isIgnoreError) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, BaseException {
        HSSFWorkbook book = new HSSFWorkbook(); //建立新HSSFWorkbook对象
        HSSFSheet sheet = book.createSheet(autoConfig.getConfigName()); //建立新的sheet对象
        _instanceExcel(book, sheet, autoConfig);//初始化所有的列
        int row = sheet.getLastRowNum() + 1;
        for (Object t : configList) {
            _writeExcel(book, sheet, autoConfig, clazz, (T) t, row, isIgnoreError);
            row++;
        }
        File file = new File(excelPath);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOut = new FileOutputStream(excelPath);
        book.write(fileOut);
        fileOut.close();
        return autoConfig;
    }

    /**
     * 将一个配置写入到Excel中
     *
     * @param book          Excel
     * @param sheet         工作表
     * @param config        类结构配置
     * @param clazz         配置类
     * @param t             配置内容
     * @param row           当前写入的行数
     * @param isIgnoreError 是否忽略错误
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> void _writeExcel(final HSSFWorkbook book, final HSSFSheet sheet, final AutoConfig config, Class<T> clazz, Object t, int row, boolean isIgnoreError) throws InvocationTargetException, IllegalAccessException, InstantiationException, BaseException {
        HSSFRow rootRow = sheet.createRow(row);
        Integer cellNum = 0;
        for (AutoFieldConfig<AutoExcel> fieldConfig : (List<AutoFieldConfig<AutoExcel>>) config.getFieldList()) {
            if (fieldConfig.isArray()) {
                HSSFSheet sheetNew = book.getSheet(fieldConfig.getFieldName());//子表
                if (sheetNew == null)//若没有则创建
                    sheetNew = book.createSheet(fieldConfig.getFieldName());
                int startRow = sheetNew.getLastRowNum() + 1;
                Object array = ClassUtil.getFieldValue(t, fieldConfig.getName(), fieldConfig.getElementClazz());
                int elementNum = Array.getLength(array);

                //主表中的数组属性值
                HSSFCell cell = rootRow.createCell(cellNum);
                String sheetName = fieldConfig.getFieldName();
                cell.setCellValue(sheetName);//写入子表名
                cellNum++;


                cell = rootRow.createCell(cellNum);
                cell.setCellValue(startRow);//写入开始行
                cellNum++;


                int maxRow = startRow + elementNum - 1;
                cell = rootRow.createCell(cellNum);
                cell.setCellValue(maxRow);
                cellNum++;

                AutoConfig autoConfig = fieldConfig.getFieldConfig();
                //获取子表
                for (int index = 0; index < elementNum; index++) {
                    Object obj = Array.get(array, index);
                    if (obj == null)
                        continue;
                    _writeExcel(book, sheetNew, autoConfig, fieldConfig.getElementClazz(), obj, startRow + index, isIgnoreError);
                }
            } else {
                HSSFCell cell = rootRow.createCell(cellNum);
                Object result = null;
                try {
                    result = ClassUtil.getFieldValue(t, fieldConfig.getName(), fieldConfig.getFieldType());
                    cell.setCellValue(String.valueOf(result));
                } catch (Exception e) {
                    if (!isIgnoreError) {
                        String message = String.format("未能获取到对应值,ClassName: %s ,FieldName: %s ,FieldType: %s 请检查!", clazz.getName(), fieldConfig.getName(), fieldConfig.getFieldType().getName());
                        throw (new BaseException(message, e.getCause()));
                    }
                } finally {
                    cellNum++;
                }
            }
        }
    }

    /**
     * 将一个配置写入到Excel中
     *
     * @param book   Excel
     * @param sheet  工作表
     * @param config 类结构配置
     * @param clazz  配置类
     * @param t      配置内容
     * @param row    当前写入的行数
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> void _writeExcel(final HSSFWorkbook book, final HSSFSheet sheet, final AutoConfig config, Class<T> clazz, Object t, int row) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        HSSFRow rootRow = sheet.createRow(row);
        Integer cellNum = 0;
        for (AutoFieldConfig<AutoExcel> fieldConfig : (List<AutoFieldConfig<AutoExcel>>) config.getFieldList()) {
            if (fieldConfig.isArray()) {
                HSSFSheet sheetNew = book.getSheet(fieldConfig.getFieldName());//子表
                if (sheetNew == null)//若没有则创建
                    sheetNew = book.createSheet(fieldConfig.getFieldName());
                int startRow = sheetNew.getLastRowNum() + 1;
                Object array = ClassUtil.getFieldValue(t, fieldConfig.getName(), fieldConfig.getElementClazz());
                int elementNum = Array.getLength(array);

                //主表中的数组属性值
                HSSFCell cell = rootRow.createCell(cellNum);
                String sheetName = fieldConfig.getFieldName();
                cell.setCellValue(sheetName);//写入子表名
                cellNum++;


                cell = rootRow.createCell(cellNum);
                cell.setCellValue(startRow);//写入开始行
                cellNum++;


                int maxRow = startRow + elementNum - 1;
                cell = rootRow.createCell(cellNum);
                cell.setCellValue(maxRow);
                cellNum++;

                AutoConfig autoConfig = fieldConfig.getFieldConfig();
                //获取子表
                for (int index = 0; index < elementNum; index++) {
                    Object obj = Array.get(array, index);
                    if (obj == null)
                        continue;
                    _writeExcel(book, sheetNew, autoConfig, fieldConfig.getElementClazz(), obj, startRow + index);
                }
            } else {
                HSSFCell cell = rootRow.createCell(cellNum);
                Object result = ClassUtil.getFieldValue(t, fieldConfig.getName(), fieldConfig.getFieldType());
                cell.setCellValue(String.valueOf(result));
                cellNum++;
            }
        }
    }

    /**
     * 读取配置EXCEL并转为配置类
     *
     * @param autoConfig     配置结构类
     * @param clazz          生成的配置类
     * @param excelPath      excel地址
     * @param indexSheetName 数据表名
     * @param startRow       开始行
     * @param endRow         结束行（若为0则代表全部）
     * @param <T>
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws BaseException
     */
    public static <T extends Comparable> List<T> readExcel(AutoConfig<AutoExcel> autoConfig, Class<T> clazz, String excelPath, String indexSheetName, int startRow, int endRow) throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException, BaseException {
        InputStream is = new FileInputStream(excelPath);
        HSSFWorkbook book = new HSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
        HSSFSheet sheet = book.getSheet(indexSheetName);
        int _startRow = sheet.getFirstRowNum();
        int _endRow = sheet.getLastRowNum();

        startRow = Math.max(_startRow, startRow);
        if (endRow > 0)
            endRow = Math.min(_endRow, endRow);
        else
            endRow = _endRow;

        for (int nowRow = startRow; nowRow <= endRow; nowRow++) {
            HSSFRow row = sheet.getRow(nowRow);
            list.add(getConfigFromExcel(autoConfig, clazz, book, row));
        }
        return list;
    }

    /**
     * 得到Excel表中的值
     *
     * @param hssfCell Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    /**
     * 从Excel中读取一条配置
     *
     * @param autoConfig 配置结构类
     * @param clazz      配置类
     * @param book       excel文档
     * @param row        配置所在行
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws BaseException
     * @throws InvocationTargetException
     */
    private static <T> T getConfigFromExcel(AutoConfig<AutoExcel> autoConfig, Class<T> clazz, HSSFWorkbook book, HSSFRow row) throws IllegalAccessException, InstantiationException, BaseException, InvocationTargetException {
        T t = clazz.newInstance();
        for (AutoFieldConfig<AutoExcel> fieldConfig : autoConfig.getFieldList()) {
            if (fieldConfig.isArray()) {
                AutoExcel excelConfig = fieldConfig.getParam();
                HSSFCell cell = row.getCell(excelConfig.getSheetNameCell());
                String sheetName = cell.getStringCellValue();

                cell = row.getCell(excelConfig.getRowStartCell());
                int startRow = (int) cell.getNumericCellValue();

                cell = row.getCell(excelConfig.getRowEndCell());
                int endRow = (int) cell.getNumericCellValue();

                int elementNum = endRow - startRow + 1;
                Object array = Array.newInstance(fieldConfig.getElementClazz(), elementNum);

                HSSFSheet fieldSheet = book.getSheet(sheetName);
                for (int fieldRowIndex = startRow; fieldRowIndex <= endRow; fieldRowIndex++) {
                    HSSFRow fieldRow = fieldSheet.getRow(fieldRowIndex);
                    Array.set(array, fieldRowIndex - startRow, getConfigFromExcel(fieldConfig.getFieldConfig(), fieldConfig.getElementClazz(), book, fieldRow));
                }
                ClassUtil.setFieldValue(t, fieldConfig.getName(), array);
            } else {
                int cellNum = fieldConfig.getParam().getCell();
                HSSFCell cell = row.getCell(cellNum);
                ClassUtil.setFieldValue(t, fieldConfig.getName(), TypeUtils.cast(getValue(cell), fieldConfig.getFieldType(), ParserConfig.getGlobalInstance()));
            }
        }
        return t;
    }


    /**
     * 初始化Excel
     *
     * @param book   excel
     * @param sheet  工作表
     * @param config AutoConfig
     */
    private static void _instanceExcel(HSSFWorkbook book, HSSFSheet sheet, final AutoConfig<AutoExcel> config) {
        HSSFRow rootRow = sheet.createRow(0);
        Integer cellNum = 0;
        for (AutoFieldConfig<AutoExcel> fieldConfig : (List<AutoFieldConfig<AutoExcel>>) config.getFieldList()) {
            if (fieldConfig.isArray()) {
                HSSFRow newRow = sheet.createRow(1);
                AutoExcel autoExcel = new AutoExcel();
                autoExcel.setIsArray(true);
                //主表中的数组属性值
                HSSFCell cell = rootRow.createCell(cellNum);
                String sheetName = fieldConfig.getFieldName();
                autoExcel.setSheetNameCell(cellNum);
                cell.setCellValue(fieldConfig.getFieldName() + "表名 （" + sheetName + ")");
                cellNum++;


                cell = rootRow.createCell(cellNum);
                autoExcel.setRowStartCell(cellNum);
                cell.setCellValue(fieldConfig.getFieldName() + "开始行");
                cellNum++;


                cell = rootRow.createCell(cellNum);
                autoExcel.setRowEndCell(cellNum);
                cell.setCellValue(fieldConfig.getFieldName() + "结束行");
                cellNum++;

                fieldConfig.setParam(autoExcel);

                AutoConfig<AutoExcel> autoConfig = fieldConfig.getFieldConfig();
                //创建子表
                HSSFSheet sheetNew = book.createSheet(fieldConfig.getFieldName());
                _instanceExcel(book, sheetNew, autoConfig);
            } else {
                AutoExcel autoExcel = new AutoExcel();
                autoExcel.setIsArray(false);
                HSSFCell cell = rootRow.createCell(cellNum);
                cell.setCellValue(fieldConfig.getFieldName() + "(" + fieldConfig.getFieldDesc() + ")");
                autoExcel.setCell(cellNum);
                fieldConfig.setParam(autoExcel);
                cellNum++;
            }
        }
    }

    /**
     * 根据配置类初始化一个Excel
     *
     * @param clazz     配置类
     * @param excelPath 创建的Excel的路径
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static AutoConfig<AutoExcel> instanceExcel(Class clazz, String excelPath) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        AutoConfig config = AutoConfigUtil.loadClass(clazz.getName());
        return instanceExcel(config, excelPath);
    }

    public static AutoConfig<AutoExcel> instanceExcel(AutoConfig autoconfig, String excelPath) throws IOException {
        AutoConfig<AutoExcel> config = (AutoConfig<AutoExcel>) autoconfig;
        HSSFWorkbook book = new HSSFWorkbook(); //建立新HSSFWorkbook对象
        HSSFSheet sheet = book.createSheet(config.getConfigName()); //建立新的sheet对象
        _instanceExcel(book, sheet, config);
        File file = new File(excelPath);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOut = new FileOutputStream(excelPath);
        book.write(fileOut);
        fileOut.close();
        return config;
    }


    /**
     * 初始化Excel配置
     *
     * @param config
     * @return
     */
    public static AutoConfig<AutoExcel> instanceExcelNoFile(AutoConfig config) throws IOException {
        AutoConfig<AutoExcel> excelAutoConfig = (AutoConfig<AutoExcel>) (config);
        HSSFWorkbook book = new HSSFWorkbook(); //建立新HSSFWorkbook对象
        HSSFSheet sheet = book.createSheet(config.getConfigName()); //建立新的sheet对象
        _instanceExcel(book, sheet, excelAutoConfig);
        return excelAutoConfig;
    }

    /**
     * <p>生成配置的扩充保存方法</p>
     * <p>因外部JAR包在读取的时候部分情况无法获取注解，因此提供补充方法来配置Class</p>
     *
     * @param list
     * @param configDirName
     * @param savePath
     * @throws IOException
     */
    public static void createConfigDataXML(List<AutoConfig> list, String configDirName, String savePath) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement(configDirName);
        for (AutoConfig autoConfig : list) {
            createClassConfigXML(autoConfig, rootElement);
        }
        document.setRootElement(rootElement);
        File file = new File(savePath);
        if (file.isDirectory()) {
            savePath += File.separator + configDirName + ".xml";
            file = new File(savePath);
        }
        IOTinyUtils.createFile(savePath);
        System.out.println(document.asXML());
        IOTinyUtils.save(file, document.asXML(), "utf-8");
    }

    public static void createConfigDataXMLFromSrc(String configDirName, String savePath) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException {
        createConfigDataXML(AutoConfigUtil.loadConfigFromSrc(), configDirName, savePath);
    }


    public static void createClassConfigXML(AutoConfig autoConfig, final Element rootElement) {
        ClassConfigData config = new ClassConfigData(autoConfig);
        Element element = rootElement.addElement(config.getClassName());
        element.addAttribute("name", config.getConfigName());
        element.addAttribute("desc", config.getConfigDesc());
        for (FieldConfigData fildConfig : config.getList()) {
            Element fieldElement = element.addElement(fildConfig.getFieldName());
            fieldElement.addAttribute("name", fildConfig.getFieldName());
            fieldElement.addAttribute("isArray", String.valueOf(fildConfig.isArray()));
            fieldElement.addAttribute("elementClass", fildConfig.getElementClassName());
            fieldElement.addAttribute("showName", fildConfig.getFieldName());
            fieldElement.addAttribute("showDesc", fildConfig.getShowDesc());
            fieldElement.addAttribute("fieldType", fildConfig.getFieldType());
        }
    }

    public static List<ClassConfigData> getClassConfigDataFromXML(String xmlPath) throws IOException, DocumentException, BaseException {
        String xml = IOTinyUtils.read(xmlPath, "utf-8");
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        List<ClassConfigData> list = new ArrayList<ClassConfigData>();
        List elements = rootElement.elements();
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            ClassConfigData data = getClassConfigDataFromXML(element);
            list.add(data);
        }
        return list;
    }

    private static ClassConfigData getClassConfigDataFromXML(Element rootElement) throws BaseException {
        ClassConfigData classConfigData = new ClassConfigData();
        classConfigData.setClassName(rootElement.getName());
        classConfigData.setConfigName(rootElement.attributeValue("name"));
        classConfigData.setConfigDesc(rootElement.attributeValue("desc"));
        List elements = rootElement.elements();
        List<FieldConfigData> list = new ArrayList<FieldConfigData>();
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            FieldConfigData fieldConfigData = getFieldConfigDataFromXML(element);
            list.add(fieldConfigData);
        }
        classConfigData.setList(list);
        return classConfigData;
    }

    private static FieldConfigData getFieldConfigDataFromXML(Element element) throws BaseException {
        FieldConfigData fieldConfigData = new FieldConfigData();
        fieldConfigData.setFieldName(element.getName());
        fieldConfigData.setShowName(element.attributeValue("showName"));
        fieldConfigData.setShowDesc(element.attributeValue("showDesc"));
        fieldConfigData.setIsArray(TypeUtils.castToBoolean(element.attributeValue("isArray")));
        fieldConfigData.setElementClassName(element.attributeValue("elementClass"));
        fieldConfigData.setFieldType(element.attributeValue("fieldType"));
        return fieldConfigData;
    }

    public static AutoConfig initAutoConfigFromExternJar(ClassConfigData configData, PackageConfigData packageConfigData) throws ClassNotFoundException {
        Class clazz = Class.forName(configData.getClassName(), true, packageConfigData.getClassLoader());
        AutoConfig autoConfig = new AutoConfig(configData, clazz);
        ArrayList<AutoFieldConfig> autoFieldConfigList = new ArrayList<AutoFieldConfig>();
        for (FieldConfigData fieldConfigData : configData.getList()) {
            try {
                Field field = clazz.getField(fieldConfigData.getFieldName());
                autoFieldConfigList.add(initFieldConfigFromExternJar(field, fieldConfigData, packageConfigData));
            } catch (Exception e) {
                continue;
            }
        }
        autoConfig.setFieldList(autoFieldConfigList);
        return autoConfig;
    }


    public static List<AutoConfig> loaderFromExternJar(String configDataXMLPath, ClassLoader classLoader) throws DocumentException, BaseException, IOException, ClassNotFoundException {
        List<AutoConfig> list = new ArrayList<AutoConfig>();
        File file = new File(configDataXMLPath);
        if (!file.exists())
            return list;
        PackageConfigData packageConfigData = new PackageConfigData();
        packageConfigData.setClassLoader(classLoader);
        for (ClassConfigData configData : getClassConfigDataFromXML(configDataXMLPath)) {
            packageConfigData.put(configData);
        }
        for (ClassConfigData configData : packageConfigData.getAllClazzConfig()) {
            list.add(initAutoConfigFromExternJar(configData, packageConfigData));
        }
        return list;
    }

    public static List<AutoConfig> loaderFromExternJar(String configDataXMLPath, String jarFilePath) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, BaseException, DocumentException, ClassNotFoundException {
        ClassLoader loader = PackageUtil.getClassLoader(jarFilePath);
        return loaderFromExternJar(configDataXMLPath, loader);
    }
}
