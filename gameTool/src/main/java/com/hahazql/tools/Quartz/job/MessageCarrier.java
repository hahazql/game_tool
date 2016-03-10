package com.hahazql.tools.Quartz.job;

import com.hahazql.tools.event.EventListenerAdapter;
import com.hahazql.tools.event.ScheduleEvent;
import com.hahazql.tools.time.TimeService;

import java.util.Date;

/**
 * Created by zql on 15/11/9.
 *
 * @className MessageCarrier
 * @classUse 消息载体, 用于传递定时任务数据
 */
public class MessageCarrier implements Runnable {
    /**
     * 定时任务
     */
    private final ScheduleEvent event;
    /**
     * 时间服务
     */
    private TimeService timeService;
    /**
     * 监听器转发器
     */
    private EventListenerAdapter adapter;

    public MessageCarrier(TimeService timeService, ScheduleEvent event, EventListenerAdapter adapter) {
        this.timeService = timeService;
        this.adapter = adapter;
        this.event = event;
    }

    public void run() {
        event.setState(ScheduleEvent.MESSAGE_STATE_DOING);
        event.setTrigerTimestamp(timeService.now());
        adapter.executeEvent(event);
    }


    public Date now() {
        return timeService.nowDate();
    }
}
