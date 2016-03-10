package com.hahazql.tools.event;


import com.hahazql.tools.Quartz.QuartzL;
import com.hahazql.tools.common.Global;
import com.hahazql.tools.common.InitializeRequired;
import com.hahazql.tools.helper.LogMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zql on 15/12/23.
 *
 * @className EventListenerAdapter
 * @classUse 事件监听适配器
 */
@SuppressWarnings("unchecked")
public class EventListenerAdapter implements InitializeRequired {
    private final Map<Class, List<IEventListener>> eventListener = new HashMap<>();
    private final ConcurrentLinkedQueue<AsyncEventListener> _async_event_queue = new ConcurrentLinkedQueue<AsyncEventListener>();

    /**
     * 注册事件监听器
     *
     * @param eventClass
     * @param listener
     * @throws IllegalArgumentException
     */
    public void registerListener(Class<? extends IEvent> eventClass,
                                 IEventListener listener) {
        List<IEventListener> _list = eventListener.get(eventClass);
        if (_list == null) {
            _list = new ArrayList<IEventListener>();
            this.eventListener.put(eventClass, _list);
        }
        for (IEventListener _listener : _list) {
            if (_listener.getClass() == listener.getClass()) {
                throw new IllegalArgumentException("The dup class listener ["
                        + listener.getClass() + "]");
            }
        }
        LogMgr.info(EventListenerAdapter.class, "[#EventManager.addListener] [Register event listener (event:"
                + eventClass.getName()
                + " listener:"
                + listener.getClass().getName() + "]");
        _list.add(listener);
    }

    /**
     * 触发事件
     */
    public void executeEvent(final IEvent event) {

        if (event instanceof CronScheduleEvent)// 定时事件
            executeScheduleEvent(event);
        else
            executeDefaultEvent(event);
    }


    //执行定时事件
    private void executeScheduleEvent(final IEvent event) {

        CronScheduleEvent _event = (CronScheduleEvent) event;
        if (_event.getState() == ScheduleEvent.MESSAGE_STATE_INITIALIZED) {
            QuartzL.schedule(_event, this);
            return;
        }
        executeDefaultEvent(event);
    }

    @SuppressWarnings("rawtypes")
    private void executeDefaultEvent(final IEvent event) {
        List<IEventListener> _eventListeners = this.eventListener.get(event
                .getClass());
        if (_eventListeners == null || _eventListeners.isEmpty()) {
            return;
        }
        for (IEventListener _listener : _eventListeners) {
            if (eventListener instanceof AsyncEventListener)
                executeAsyncEventListener(event, _listener);
            else
                _listener.fireEvent(event);
        }
    }


    //执行时间的异步监听
    private void executeAsyncEventListener(final IEvent event, IEventListener _listener) {

        AsyncEventListener copyListener = ((AsyncEventListener) eventListener)
                .getInstance();
        copyListener.setIEvent(event);
        _async_event_queue.offer(copyListener);

    }

    @Override
    public void init() {
        Global.threadPool.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (_async_event_queue.isEmpty()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                        } finally {
                            continue;
                        }
                    }
                    @SuppressWarnings("rawtypes")
                    AsyncEventListener listener = _async_event_queue.poll();
                    Global.eventThreadPool.submit(listener);
                }
            }
        });

    }

}
