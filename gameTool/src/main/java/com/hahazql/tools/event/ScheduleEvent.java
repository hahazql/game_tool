package com.hahazql.tools.event;/**
 * Created by zql on 15/12/25.
 */

import java.util.concurrent.ScheduledFuture;

/**
 * Created by zql on 15/12/25.
 *
 * @className ScheduleEvent
 * @classUse 定时时间(定时触发的事件)
 */
public abstract class ScheduleEvent implements IEvent {
    public static final int MESSAGE_STATE_INITIALIZED = 0; // 消息初始化
    public static final int MESSAGE_STATE_WAITING = 1; // 消息处于等待状态，可以被取消
    public static final int MESSAGE_STATE_DOING = 2; // 被执行中
    public static final int MESSAGE_STATE_CANCELED = 3; // 被取消

    private ScheduledFuture<?> future;
    private int state;
    private final long createTimestamp;
    private long trigerTimestamp;
    //延迟时间
    private long delay = 0;

    public ScheduleEvent(long createTime) {
        setState(MESSAGE_STATE_INITIALIZED);
        this.createTimestamp = createTime;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public long getTrigerTimestamp() {
        return trigerTimestamp;
    }

    public void setTrigerTimestamp(long trigerTimestamp) {
        this.trigerTimestamp = trigerTimestamp;
    }


    public synchronized int getState() {
        return state;
    }

    public synchronized void setState(int state) {
        this.state = state;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }


    public synchronized void cancel() {
        if (state == MESSAGE_STATE_WAITING) {
            future.cancel(false);
            this.setState(MESSAGE_STATE_CANCELED);
        } else if (state == MESSAGE_STATE_DOING) {
            this.setState(MESSAGE_STATE_CANCELED);
        }
    }

}
