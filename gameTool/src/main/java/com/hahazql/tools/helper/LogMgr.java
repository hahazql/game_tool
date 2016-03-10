package com.hahazql.tools.helper;/**
 * Created by zql on 15/12/22.
 */


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zql on 15/12/22.
 *
 * @className LogMgr
 * @classUse
 */
public class LogMgr {
    public static void error(String source, String message) {
        Logger logger = LogManager.getLogger(source);
        logger.error(message);
    }

    public static void error(Class clazz, String message) {
        Logger logger = LogManager.getLogger(clazz.getName());
        logger.error(message);
    }

    public static void info(Class clazz, String message) {
        Logger logger = LogManager.getLogger(clazz.getName());
        logger.info(message);
    }

    public static void info(String type, String message) {
        Logger logger = LogManager.getLogger(type);
        logger.info(message);
    }

    public static void warn(Class clazz, String message) {
        Logger logger = LogManager.getLogger(clazz.getName());
        logger.warn(message);
    }

    public static void warn(String type, String message) {
        Logger logger = LogManager.getLogger(type);
        logger.warn(message);
    }

    public static void debug(Class clazz, String message) {
        Logger logger = LogManager.getLogger(clazz.getName());
        logger.debug(message);
    }

    public static void debug(String type, String message) {
        Logger logger = LogManager.getLogger(type);
        logger.debug(message);
    }

}
