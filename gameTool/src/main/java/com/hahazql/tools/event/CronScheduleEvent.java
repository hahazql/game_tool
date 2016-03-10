package com.hahazql.tools.event;/**
 * Created by zql on 15/12/25.
 */

import com.hahazql.tools.helper.LogMgr;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Created by zql on 15/12/25.
 *
 * @className CronScheduleEvent
 * @classUse 使用cron的定时触发事件
 */
public class CronScheduleEvent extends ScheduleEvent {
    private String cronStr;

    private Scheduler sched;
    private JobKey jobKey;

    private CronTrigger trigger;
    private long lastFireTime = 0;

    public CronScheduleEvent(String cronStr) {
        super(0);
        this.cronStr = cronStr;
    }


    @Override
    public synchronized void cancel() {
        try {
            sched.deleteJob(jobKey);
        } catch (SchedulerException e) {
            LogMgr.error(CronScheduleEvent.class, e.getMessage());
        }
        super.cancel();
    }


    public String getCronStr() {
        return cronStr;
    }


    public void setCronStr(String cronStr) {
        this.cronStr = cronStr;
    }


    public Scheduler getSched() {
        return sched;
    }


    public void setSched(Scheduler sched) {
        this.sched = sched;
    }


    public JobKey getJobKey() {
        return jobKey;
    }


    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }


    public CronTrigger getTrigger() {
        return trigger;
    }


    public void setTrigger(CronTrigger trigger) {
        this.trigger = trigger;
    }

    public long getNextExecuteTime() {
        return this.trigger.getNextFireTime().getTime();
    }


    @Override
    public long getTrigerTimestamp() {
        return this.trigger.getNextFireTime().getTime();
    }


    @Override
    public long getCreateTimestamp() {
        return this.trigger.getStartTime().getTime();
    }


    public long getLastFireTime() {
        return lastFireTime;
    }


    public void setLastFireTime(long lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

}
