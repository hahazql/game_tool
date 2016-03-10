package com.hahazql.tools.msg;/**
 * Created by zql on 16/3/9.
 */

import com.hahazql.tools.helper.LogMgr;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zql on 16/3/9.
 * @className MessageProcessMulti
 * @classUse
 *
 *
 */
public class MessageProcessMulti<T extends IMessageHeader>
{
    private final int threadNum = 10;
    /**
     * 每个处理线程的缓存
     */
    private HashMap<Integer,ConcurrentLinkedQueue<T>> processCache = new HashMap<Integer, ConcurrentLinkedQueue<T>>();
    /**
     * 消息缓存
     */
    private ConcurrentLinkedQueue<T> messageCache = new ConcurrentLinkedQueue<T>();
    /**
     * 线程池
     */
    private ExecutorService service =Executors.newFixedThreadPool(threadNum+1);
    /**
     * 加锁类型计数器
     */
    private ConcurrentHashMap<Integer,AtomicInteger> typeCounter = new ConcurrentHashMap<Integer, AtomicInteger>();

    private void start()
    {
        for(int i=0;i<threadNum;i++)
        {
            //生成线程缓存
            processCache.put(i,new ConcurrentLinkedQueue<T>());
        }
    }

    private void listen()
    {
        service.submit(new Runnable() {
            public void run() {
                while(1>0) {
                    final int size = messageCache.size();

                    try {
                        //休眠1秒
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        LogMgr.error(MessageProcessMulti.class, "监听线程休眠出错");
                    }
                }
            }
        });
    }


    private class ThreadProcess implements Runnable
    {
        private final int threadID;

        public ThreadProcess(int threadID)
        {
            this.threadID = threadID;
        }

        public void run()
        {
            while(1>0) {
                T message = messageCache.poll();
                if (message == null)//若消息队列中的消息已经处理完成
                    message = processCache.get(threadID).peek();
                if(message == null)
                    break;

            }

        }
    }
}
