package com.bobomico.pubfound;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ClassName: com.bobomico.datasource.mall-bobomico-B
 * @URL: https://blog.csdn.net/xiongyouqiang/article/details/80487944
 * @Author: DELL
 * @Date: 2019/5/1  7:20
 * @Description: 自定义多数据源配置类
 * @version:
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal();

    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}