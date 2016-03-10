package com.hahazql.tools.Quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TaskEventJob implements Job {
    private MessageCarrier carrier;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        setCarrier((MessageCarrier) dataMap.get("carrier"));
        carrier.run();
    }

    public MessageCarrier getCarrier() {
        return carrier;
    }

    public void setCarrier(MessageCarrier carrier) {
        this.carrier = carrier;
    }


}
