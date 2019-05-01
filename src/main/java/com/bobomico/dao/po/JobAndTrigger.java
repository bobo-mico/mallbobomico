package com.bobomico.dao.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @ClassName: com.bobomico.dao.po.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  5:58
 * @Description: Quartz可视化
 * @version:
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobAndTrigger {
    private String JOB_NAME;
    private String JOB_GROUP;
    private String JOB_CLASS_NAME;
    private String TRIGGER_NAME;
    private String TRIGGER_GROUP;
    private BigInteger REPEAT_INTERVAL;
    private BigInteger TIMES_TRIGGERED;
    private String CRON_EXPRESSION;
    private String TIME_ZONE_ID;
}