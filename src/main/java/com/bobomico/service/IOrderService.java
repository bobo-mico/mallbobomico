package com.bobomico.service;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @ClassName: com.bobomico.service.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/4  16:21
 * @Description:
 * @version:
 */
public interface IOrderService {
    // 客户端
    // 支付
    ServerResponse pay(Long orderNo, Integer userId, String path);
    // 回调之后的业务方法
    ServerResponse aliCallback(Map<String,String> params);
    // 前台轮询查询支付状态
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
    // 创建订单
    ServerResponse createOrder(Integer userId, Integer shippingId);
    //
    ServerResponse<String> cancel(Integer userId, Long orderNo);
    //
    ServerResponse getOrderCartProduct(Integer userId);
    //
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    //
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    // 后台
    //
    // ServerResponse<PageInfo> manageList(int pageNum,int pageSize);
    // // ServerResponse<OrderVo> manageDetail(Long orderNo);
    // ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
    // ServerResponse<String> manageSendGoods(Long orderNo);
}
