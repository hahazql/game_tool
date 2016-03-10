package com.hahazql.tools.common;/**
 * Created by zql on 15/12/22.
 */

import com.hahazql.tools.event.EventListenerAdapter;
import com.hahazql.tools.guid.GUID;
import com.hahazql.tools.time.SystemTimeService;
import com.hahazql.tools.time.TimeService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zql on 15/12/22.
 *
 * @className Global
 * @classUse
 */
public class Global {

    public static boolean isClose = false;

    /**
     * 全局线程池
     * 全服务器公用的线程池
     */
    public static ExecutorService threadPool = Executors.newCachedThreadPool(new NamedThreadFactory("Server-Global-Pool"));
    /**
     * 事件管理器
     */
    public static EventListenerAdapter eventListenerAdapter = new EventListenerAdapter();

    /**
     * 异步事件线程池
     */
    public static ExecutorService eventThreadPool = Executors.newFixedThreadPool(50, new NamedThreadFactory("Event-Aysnc-Pool"));

    /**
     * 时间获取
     * 所有的时间均通过此方法获取
     * 方便服务器改变时区时进行调整
     */
    public static TimeService timeService = new SystemTimeService(false);
    /**
     * 唯一ID生成器
     */
    public static GUID dBuild = new GUID();

}
