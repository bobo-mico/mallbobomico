package com.bobomico.logger.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: com.bobomico.logger.common.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  5:37
 * @Description: todo 可替换成枚举
 * @version:
 */
public final class LogConstant {
    /**
     * 测试日志常量内容
     *
     * @return
     */
    public static Map<String, String> testConstant() {
        Map<String, String> map = new HashMap<>();
        map.put("测试：0", "测试：正常");
        map.put("测试：1", "测试：删除");
        map.put("测试：2", "测试：审核");
        return map;
    }
}
