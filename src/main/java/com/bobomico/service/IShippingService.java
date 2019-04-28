package com.bobomico.service;

import com.bobomico.common.ServerResponse;
import com.bobomico.dao.po.MallShipping;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: com.bobomico.service.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/4  6:23
 * @Description: 服务接口 - 用户地址管理
 * @version:
 */
public interface IShippingService {
    // 新增地址
    ServerResponse add(Integer userId, MallShipping shipping);
    // 删除地址
    ServerResponse<String> del(Integer userId,Integer shippingId);
    // 更新地址
    ServerResponse<String> update(Integer userId, MallShipping shipping);
    // 根据用户id和地址id查询地址
    ServerResponse<MallShipping> select(Integer userId, Integer shippingId);
    // 根据用户id查询所有地址
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
