package com.bobomico.quartz.utils;

/**
 * @ClassName: com.bobomico.quartz.utils.mall-bobomico-B
 * @URL: https://github.com/huangyueranbbc/QuartzUtils/blob/QuartzUtils_v1.0.0/src/main/java/com/hyr/quartz/utils/HookPriority.java
 * @Author: DELL
 * @Date: 2019/4/30  12:28
 * @Description: 类似于状态机
 * @version:
 */
public enum HookPriority {
    MIN_PRIORITY(1),
    SCHEDULER_PRIORITY(20),
    PLUGIN_PRIORITY(40),
    NORM_PRIORITY(50),
    JOB_PRIORITY(60),
    MAX_PRIORITY(100);

    private int priority;

    HookPriority(int priority) {
        this.priority = priority;
    }

    public int value() {
        return priority;
    }
}
