package com.bobomico.statemachine;

/**
 * @ClassName: com.bobomico.statemachine.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/6  21:50
 * @Description:
 * @version:
 */
public enum States {
    UNPAID,                 // 待支付
    WAITING_FOR_RECEIVE,    // 待收货
    DONE                    // 已完成
}
