package com.hahazql.tools.clazz;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.NoSuchFileException;

/**
 * Created by zql on 2015/9/28.
 */
public class ClassLoadUtil {
    private static URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    public static Method addMethod;

    /**
     * 初始化ClassLoader
     *
     * @throws NoSuchMethodException
     */
    private static void init() throws NoSuchMethodException {
        if (addMethod == null)
            initAddMethod();
    }


    private static Method initAddMethod() throws NoSuchMethodException {
        addMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addMethod.setAccessible(true);
        return addMethod;
    }

    /**
     * 通过ClassLoader导入Jar
     *
     * @param jarPath Jar路径
     * @throws NoSuchFileException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws MalformedURLException
     * @throws NoSuchMethodException
     */
    public static URL loadJarPath(String jarPath) throws NoSuchFileException, IllegalAccessException, InvocationTargetException, MalformedURLException, NoSuchMethodException {
        init();
        File file = new File(jarPath);
        if (!file.exists()) {
            throw (new NoSuchFileException(jarPath));
        }
        return loadJarFile(file);
    }

    /**
     * 通过ClassLoader导入Jar文件
     *
     * @param file Jar文件
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private static URL loadJarFile(File file) throws MalformedURLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        init();
        if (!file.exists()) {
            return null;
        }
        URL url = file.toURI().toURL();
        addMethod.invoke(classLoader, new Object[]{file.toURI().toURL()});
        return url;
    }


    /**
     * 通过ClassLoader导入Class文件
     *
     * @param clazzDirPath Class文件所在文件夹路径
     * @param clazzName    Class名
     * @throws MalformedURLException
     * @throws NoSuchFieldException
     */
    public static void loadClassFile(String clazzDirPath, String clazzName) throws MalformedURLException, NoSuchFieldException {
        File file = new File(clazzDirPath);
        if (!file.isDirectory())
            throw (new NoSuchFieldException("类的所在文件夹不存在"));
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL(clazzDirPath)});
    }

}
