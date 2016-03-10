package com.hahazql.tools.clazz;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class PackageUtil {
    public static URLClassLoader classLoader;
    private static HashSet<URL> urlHashSet = new HashSet<URL>();
    private static Boolean lockCreateLoader = true;

    /**
     * 获取自己项目下所有类
     *
     * @return
     */
    public static List<String> getSrcClassName() {
        String filePath = ClassLoader.getSystemResource("").getPath();
        List<String> fileNames = getClassNameByFile(filePath, null, true);
        return fileNames;
    }


    /**
     * 获取某包下（包括该包的所有子包）所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) throws UnsupportedEncodingException {
        return getClassName(packageName, true);
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }


    public static ClassLoader getClassLoader(String jarPath) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File jar = new File(jarPath);
        JarFile jf = new JarFile(jar);
        URL jarUrl = jar.toURI().toURL();

        if (urlHashSet.contains(jarUrl))
            return classLoader;

        HashSet<URL> _urls = new HashSet<URL>();
        _urls.add(jarUrl);
        Manifest mf = jf.getManifest(); // If the jar has a class-path in it's manifest add it's entries
        if (mf != null) {
            String cp = mf.getMainAttributes().getValue("class-path");
            if (cp != null) {
                for (String cpe : cp.split("\\s+")) {
                    File lib = new File(jar.getParentFile(), cpe);
                    _urls.add(lib.toURI().toURL());
                }
            }
        }

        if (classLoader == null) {
            URLClassLoader cl = new URLClassLoader(new URL[]{});
            synchronized (lockCreateLoader) {
                if (_urls.size() > 0) {
                    cl = new URLClassLoader(_urls.toArray(new URL[_urls.size()]), ClassLoader.getSystemClassLoader());
                }
                classLoader = cl;
                urlHashSet = _urls;
                Thread.currentThread().setContextClassLoader(classLoader);
            }
        } else {
            Method addUrl = classLoader.getClass().getMethod("addURL", URL.class);
            addUrl.setAccessible(true);
            synchronized (lockCreateLoader) {
                for (URL url : _urls) {
                    if (!urlHashSet.contains(url)) {
                        addUrl.invoke(classLoader, url);
                        urlHashSet.add(url);
                    }
                }
            }
        }
        return classLoader;
    }

    /**
     * 获取JAR包中的类
     *
     * @param jarPath
     * @return
     * @throws MalformedURLException
     */
    public static List<Class> getClassFromJar(String jarPath) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Class> ret = new ArrayList<Class>();
        File file = new File(jarPath);
        if (!file.exists())
            throw (new NoSuchFileException("没有找到JAR文件"));
        ClassLoader classLoader = getClassLoader(jarPath);


        try {
            JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(jarPath));
            JarEntry crunchifyJar;

            while (true) {
                crunchifyJar = crunchifyJarFile.getNextJarEntry();
                if (crunchifyJar == null) {
                    break;
                }
                if ((crunchifyJar.getName().endsWith(".class"))) {
                    String className = crunchifyJar.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    Class<?> clazz = null;
                    // 从加载器中加载Class
                    try {
                        clazz = Thread.currentThread().getContextClassLoader().loadClass(myClass);
                    } catch (Exception e) {
                        /** 无法loader Class进来**/
                        e.printStackTrace();
                    }
                    if (clazz != null)
                        ret.add(clazz);
                }
            }
        } catch (Exception e) {
        }
        return ret;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName, boolean childPackage) throws UnsupportedEncodingException {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", File.separator);
        URL url = loader.getResource(packagePath);
        String filePath = "";
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                filePath = URLDecoder.decode(url.getPath(), "utf-8");
                fileNames = getClassNameByFile(filePath, null, childPackage, packageName);
            } else if (type.equals("jar")) {
                fileNames = getClassNameByJar(url.getPath(), childPackage);
            }
        } else {
            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
        }
        return fileNames;
    }


    public static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage, String packageName) {
        String packagePath = packageName.replace(".", File.separator);
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage, packageName));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    int index = childFilePath.indexOf(packagePath);
                    if (index > -1) {
                        childFilePath = childFilePath.substring(index, childFilePath.lastIndexOf("."));
                        childFilePath = childFilePath.replace(File.separator, ".");
                    }
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }


    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath     文件路径
     * @param className    类名集合
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    if (childFilePath.indexOf("/classes") > -1) {
                        childFilePath = childFilePath.substring(childFilePath.indexOf("/classes") + 9, childFilePath.lastIndexOf("."));
                        childFilePath = childFilePath.replace("/", ".");
                    } else if (childFilePath.indexOf("\\classes") > -1) {
                        childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                        childFilePath = childFilePath.replace("\\", ".");
                    } else if (childFilePath.indexOf("/bin") > -1) {
                        childFilePath = childFilePath.substring(childFilePath.indexOf("/bin") + 5, childFilePath.lastIndexOf("."));
                        childFilePath = childFilePath.replace("/", ".");
                    } else if (childFilePath.indexOf("\\bin") > -1) {
                        childFilePath = childFilePath.substring(childFilePath.indexOf("\\bin") + 5, childFilePath.lastIndexOf("."));
                        childFilePath = childFilePath.replace("\\", ".");
                    }
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath      jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        String[] jarInfo = jarPath.split("!");
        int index = jarInfo[0].indexOf("/");
        if (index < 0)
            return null;
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int _index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (_index != -1) {
                            myPackagePath = entryName.substring(0, _index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls         URL集合
     * @param packagePath  包路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }
}
