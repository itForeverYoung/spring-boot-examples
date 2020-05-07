package com.it.forever.young.service;

import com.it.forever.young.entity.ScheduleJobEntity;

/**
 * @author zhangjikai
 * @date 2020/3/23 21:00
 **/
public interface ScheduleService {

    /**
     * add a job
     * @param clazz job class
     * @param cronExpression executed cron expression
     * @return true if add succeed
     */
    boolean addJob(Class clazz, String cronExpression);

    /**
     * add a job
     * @param scheduleJobEntity
     * @return true if add succeed
     */
    boolean addJob(ScheduleJobEntity scheduleJobEntity);

    /**
     * pause a job by jobName and job group
     * @param jobName job name
     * @param jobGroup job group
     * @return true if pause succeed
     */
    boolean pauseJob(String jobName, String jobGroup);

    /**
     * resume a job by jobName and job group
     * @param jobName job name
     * @param jobGroup job group
     * @return true if resume succeed
     */
    boolean resumeJob(String jobName, String jobGroup);

    /**
     * delete a job by jobName and job group
     * @param jobName job name
     * @param jobGroup job group
     * @return true if delete succeed
     */
    boolean deleteJob(String jobName, String jobGroup);

    /**
     * check if the job is exists
     * @param jobName job name
     * @param jobGroup job group
     * @return true if exists
     */
    boolean checkExists(String jobName, String jobGroup);

    /**
     * stop or start a job by jobName and job group
     * @param jobName job name
     * @param jobGroup job group
     * @param status if stop, call pauseJob; if start call resumeJob
     * @return true if update succeed
     */
    boolean updateJobStatus(String jobName, String jobGroup, String status);

    /**
     * update cron expression by triggerName and triggerGroup
     * @param triggerName trigger name
     * @param triggerGroup trigger group
     * @param cron new cron expression
     * @param strategyWithMisFire
     * @return true if update succeed
     */
    boolean updateTriggerCronExpression(String triggerName, String triggerGroup, String cron, int strategyWithMisFire);

}
