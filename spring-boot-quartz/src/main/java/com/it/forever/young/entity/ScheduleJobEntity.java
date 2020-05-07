package com.it.forever.young.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author zhangjikai
 * @date 2020/3/23 21:01
 **/
@Data
public class ScheduleJobEntity {

    private Class clazz;
    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private String cronExpression;
    private Map<String, Object> dataMap;
    private int strategyWithMisFire;

}
