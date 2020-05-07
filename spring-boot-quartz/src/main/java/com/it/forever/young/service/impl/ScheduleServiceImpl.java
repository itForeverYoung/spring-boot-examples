package com.it.forever.young.service.impl;

import com.it.forever.young.constant.JobStatus;
import com.it.forever.young.entity.ScheduleJobEntity;
import com.it.forever.young.service.ScheduleService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Objects;

/**
 * @author zhangjikai
 * @date 2020/3/23 21:00
 **/
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Override
    public boolean addJob(Class clazz, String cronExpression) {
        ScheduleJobEntity jobEntity = new ScheduleJobEntity();
        jobEntity.setClazz(clazz);
        jobEntity.setJobName(clazz.getName());
        jobEntity.setTriggerName(clazz.getName());
        jobEntity.setCronExpression(cronExpression);
        return addJob(jobEntity);
    }

    @Override
    public boolean addJob(ScheduleJobEntity jobEntity) {
        if (Objects.isNull(jobEntity)) {
            return false;
        }
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(jobEntity.getCronExpression());
        } catch (ParseException e) {
            LOGGER.error("parse cron expression failed");
            return false;
        }
        JobDetail jobDetail = JobBuilder.newJob(jobEntity.getClazz()).withIdentity(jobEntity.getJobName(), jobEntity.getJobGroup()).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        if (Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY == jobEntity.getStrategyWithMisFire()) {
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW == jobEntity.getStrategyWithMisFire()) {
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else {
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobEntity.getTriggerName(), jobEntity.getTriggerGroup()).withSchedule(cronScheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOGGER.error("trigger {} and cron {} schedule {} failed in group {}, ex is {}",
                    jobEntity.getTriggerName(), jobEntity.getCronExpression(),
                    jobEntity.getJobName(), jobEntity.getJobGroup(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean pauseJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseJob(new JobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            LOGGER.error("pause job {} in {} failed, ex is {}", jobName, jobGroup, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean resumeJob(String jobName, String jobGroup) {
        try {
            scheduler.resumeJob(new JobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            LOGGER.error("resume job {} in {} failed, ex is {}", jobName, jobGroup, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroup) {
        try {
            return scheduler.deleteJob(new JobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            LOGGER.error("delete job {} in {} failed, ex is {}", jobName, jobGroup, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkExists(String jobName, String jobGroup) {
        try {
            return scheduler.checkExists(new JobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            LOGGER.error("check exists job {} in {} failed, ex is {}", jobName, jobGroup, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateJobStatus(String jobName, String jobGroup, String status) {
        if (JobStatus.start.name().equals(status)) {
            return resumeJob(jobName, jobGroup);
        }
        if (JobStatus.stop.name().equals(status)) {
            return pauseJob(jobName, jobGroup);
        }
        return false;
    }

    @Override
    public boolean updateTriggerCronExpression(String triggerName, String triggerGroup, String cron, int strategyWithMisFire) {
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(cron);
        } catch (ParseException e) {
            LOGGER.error("update trigger {} in {} failed, because parse {} to CronExpression failed, ex is {}", triggerName, triggerGroup, cron, e.getMessage());
            return false;
        }
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        if (Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY == strategyWithMisFire) {
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW == strategyWithMisFire) {
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else {
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }
        // update trigger cron expression
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup).withSchedule(cronScheduleBuilder).build();
        try {
            scheduler.rescheduleJob(new TriggerKey(triggerName, triggerGroup), cronTrigger);
        } catch (SchedulerException e) {
            LOGGER.error("update trigger {} in {} failed, because this trigger not found, ex is {}", triggerName, triggerGroup, e.getMessage());
            return false;
        }
        return true;
    }

}
