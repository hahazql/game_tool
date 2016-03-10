package com.hahazql.tools.Quartz;


import com.hahazql.tools.Quartz.job.MessageCarrier;
import com.hahazql.tools.Quartz.job.TaskEventJob;
import com.hahazql.tools.common.Global;
import com.hahazql.tools.event.CronScheduleEvent;
import com.hahazql.tools.event.EventListenerAdapter;
import com.hahazql.tools.event.ScheduleEvent;
import com.hahazql.tools.helper.LogMgr;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzL {

    public static Scheduler scheduler;
    public static SchedulerFactory sf = new StdSchedulerFactory();
    public static volatile boolean isStart = false;

    public static void startQuartzService() {
        try {
            if (scheduler == null)
                scheduler = sf.getScheduler();
            scheduler.start();
            isStart = true;
        } catch (SchedulerException e) {
            LogMgr.error(QuartzL.class, e.getMessage());
        }

    }

    // 触发定时事件
    public static void schedule(CronScheduleEvent event,
                                EventListenerAdapter adapter) {
        if (!isStart)
            startQuartzService();
        LogMgr.debug(QuartzL.class,
                "schedule cron:" + event.getCronStr() + " event:"
                        + event.getClass());
        event.setState(ScheduleEvent.MESSAGE_STATE_WAITING);
        MessageCarrier carrier = new MessageCarrier(Global.timeService, event, adapter);
        try {
            JobDetail job = JobBuilder.newJob(TaskEventJob.class).build();
            job.getJobDataMap().put("carrier", carrier);
            event.setJobKey(job.getKey());
            event.setSched(scheduler);
            CronTrigger trigger = newTrigger()
                    .withSchedule(cronSchedule(event.getCronStr())).build();
            LogMgr.debug(QuartzL.class, "trigger>>>>>" + trigger);
            LogMgr.debug(QuartzL.class, "job>>>>>" + job);
            scheduler.scheduleJob(job, trigger);
            event.setTrigger(trigger);
        } catch (Exception e) {
            e.printStackTrace();
            LogMgr.error(QuartzL.class,
                    "error create schedule cron:" + event.getCronStr()
                            + " evnet:" + event.getClass());
        }

    }

    public static void stopQuartzService() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            // PrintUtil.error(QuartzL.class,e.getMessage(),e.getStackTrace());
            LogMgr.error(QuartzL.class, e.getMessage());
        }
    }

}
