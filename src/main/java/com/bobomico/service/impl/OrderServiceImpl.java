package com.bobomico.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.bobomico.common.Const;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.OrderItemVo;
import com.bobomico.controller.vo.OrderProductVo;
import com.bobomico.controller.vo.OrderVo;
import com.bobomico.controller.vo.ShippingVo;
import com.bobomico.convert.Converter;
import com.bobomico.dao.*;
import com.bobomico.dao.po.*;
import com.bobomico.service.IOrderService;
import com.bobomico.util.BigDecimalUtil;
import com.bobomico.util.DateTimeUtil;
import com.bobomico.util.FTPUtil;
import com.bobomico.util.PropertiesUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName: com.bobomico.service.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/4  16:24
 * @Description:
 * @version:
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {


    private static AlipayTradeService tradeService;

    static {
        /**
         * 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         * Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("alipay/zfbinfo.properties");

        /**
         * 使用Configs提供的默认参数
         * AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Autowired
    private MallOrderMapper orderMapper;

    @Autowired
    private MallOrderItemMapper orderItemMapper;

    @Autowired
    private MallPayInfMapper payInfoMapper;

    @Autowired
    private MallCartMapper cartMapper;

    @Autowired
    private MallProductMapper productMapper;

    @Autowired
    private MallShippingMapper shippingMapper;

    /**
     * 创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse createOrder(Integer userId, Integer shippingId){
        // 获取已勾选的购物车数据
        List<MallCart> cartList = cartMapper.selectCheckedCartByUserId(userId);

        // 获取购物车中的商品集合
        ServerResponse serverResponse = this.getCartOrderItem(userId, cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        // 获取购物车中的商品集合 同上
        List<MallOrderItem> orderItemList = (List<MallOrderItem>)serverResponse.getData();
        // 计算订单的总价
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        // 生成订单并返回订单对象
        MallOrder order = this.assembleOrder(userId, shippingId, payment);
        if(order == null){
            return ServerResponse.createByErrorMessage("生成订单错误");
        }

        // 继续装配订单明细对象 主要是追加该明细属于哪个订单 todo 可以用流
        for(MallOrderItem orderItem : orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        // 批量插入订单
        orderItemMapper.batchInsert(orderItemList);

        // 生成成功 同步关联数据
        // 减少库存 通过订单明细的集合 遍历商品并更新库存
        this.reduceProductStock(orderItemList);
        // 清空购物车 通过购物车id
        this.cleanCart(cartList);

        // 返回给前端数据 包含了订单及订单明细
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    /**
     * 订单装配
     * @param userId 谁的订单
     * @param shippingId 地址信息
     * @param payment 支付价格 快照
     * @return 半成品
     */
    private MallOrder assembleOrder(Integer userId, Integer shippingId, BigDecimal payment){
        MallOrder order = new MallOrder();
        // 生成订单号(算法可以继续重构
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);

        order.setSysUserId(userId);
        order.setShippingId(shippingId);
        // 发货时间等
        // 付款时间等
        int rowCount = orderMapper.insert(order);
        if(rowCount > 0){
            return order;
        }
        return null;
    }

    /**
     * 订单号生成器
     * @return
     */
    private long generateOrderNo(){
        long currentTime =System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 同步购物车
     * @param cartList
     */
    private void cleanCart(List<MallCart> cartList){
        for(MallCart cart : cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    /**
     * 同步商品库存
     * @param orderItemList
     */
    private void reduceProductStock(List<MallOrderItem> orderItemList){
        for(MallOrderItem orderItem : orderItemList){
            MallProduct product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * 获取订单明细对象
     * @param userId
     * @param cartList
     * @return 购物车中的商品集合
     */
    private ServerResponse getCartOrderItem(Integer userId, List<MallCart> cartList){
        List<MallOrderItem> orderItemList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        // 校验购物车数据 包括单品的状态和数量
        for(MallCart cartItem : cartList){
            MallOrderItem orderItem = new MallOrderItem();

            // 查询购物车中的商品详情(店家
            MallProduct product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            // 检查该商品是否还存在 todo 我认为在每次进入购物车时都应该自动检查商品是否还存在
            if(product == null){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "已被店家删除");
            }
            // 检查该商品的销售状态
            if(Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }

            // 校验库存
            // 从购物车中检查该商品的数量 > 商品的库存
            if(cartItem.getQuantity() > product.getStock()){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }

            // 装配商品
            orderItem.setSysUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());  // 单品价格快照
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));   // 商品总价快照
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }

    /**
     * 计算订单中的商品总价
     * @param orderItemList
     * @return
     */
    private BigDecimal getOrderTotalPrice(List<MallOrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        // 所有商品都有总价 把商品的总价相加就是订单总价
        for(MallOrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add( payment.doubleValue(), orderItem.getTotalPrice().doubleValue() );
        }
        return payment;
    }

    /**
     * 装配 订单
     * @param orderList
     * @param userId
     * @return 订单集合
     */
    private List<OrderVo> assembleOrderVoList(List<MallOrder> orderList, Integer userId){
        List<OrderVo> orderVoList = Lists.newArrayList();
        for(MallOrder order : orderList){
            List<MallOrderItem>  orderItemList;
            if(userId == null){
                // todo 管理员查询的时候 不需要传userId
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            }else{
                orderItemList = orderItemMapper.getByOrderNoUserId(order.getOrderNo(),userId);
            }
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    /**
     * 装配 订单详情
     * @param order         订单信息
     * @param orderItemList 订单详情信息
     * @return vo
     */
    private OrderVo assembleOrderVo(MallOrder order, List<MallOrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        // 订单号
        orderVo.setOrderNo(order.getOrderNo());
        // 订单总价
        orderVo.setPayment(order.getPayment());
        // 支付类型
        orderVo.setPaymentType(order.getPaymentType());
        // 支付类型的文字描述
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        // 运费
        orderVo.setPostage(order.getPostage());
        // 订单状态 默认为待支付
        orderVo.setStatus(order.getStatus());
        // 订单状态的文字描述
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());
        // 收件地址id
        orderVo.setShippingId(order.getShippingId());
        // 收件地址详情
        MallShipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            // 收件人姓名
            orderVo.setReceiverName(shipping.getReceiverName());
            // 地址详情
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        // 订单支付时间
        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        // 订单发货时间
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        // 订单结束时间
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        // 订单创建时间
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        // 订单关闭时间
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));
        // 主图地址
        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        // 订单商品详情 由订单详情vo组合的集合
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        // 订单商品详情
        for(MallOrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        // 设置订单商品详情
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /**
     * 装配 收件地址
     * @param shipping
     * @return
     */
    private ShippingVo assembleShippingVo(MallShipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo = Converter.assemble(shipping, shippingVo);
        return shippingVo;
    }

    /**
     * 装配 订单商品 单个
     * @param orderItem 订单详情集合
     * @return vo
     */
    private OrderItemVo assembleOrderItemVo(MallOrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo = Converter.assemble(orderItem, orderItemVo);
        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    /**
     * 支付
     * @param orderNo
     * @param userId
     * @param path
     * @return
     */
    public ServerResponse pay(Long orderNo, Integer userId, String path){
        // 返回给前端
        Map<String ,String> resultMap = Maps.newHashMap();
        // 根据用户ID和订单ID查询订单
        MallOrder order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        // 封装前端需要的需要
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("波波米克商城扫码支付,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<MallOrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
        for(MallOrderItem orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(
                    orderItem.getProductId().toString(),
                    orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                // 细节细节细节
                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path, qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常",e);
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                resultMap.put("qrUrl",qrUrl);
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    /**
     * 简单打印应答
     * @param response
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    /**
     * 回调之后的业务逻辑
     * @param params
     * @return
     */
    public ServerResponse aliCallback(Map<String,String> params){
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        MallOrder order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("非波波&米可商城的订单,回调忽略");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        if(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        MallPayInf payInfo = new MallPayInf();
        payInfo.setSysUserId(order.getSysUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    /**
     * 前台轮询查询支付状态
     * @param userId
     * @param orderNo
     * @return
     */
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo){
        MallOrder order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     *
     * @param userId
     * @param orderNo
     * @return
     */
    public ServerResponse<String> cancel(Integer userId,Long orderNo){
        MallOrder order  = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("该用户此订单不存在");
        }
        if(order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMessage("已付款,无法取消订单");
        }
        MallOrder updateOrder = new MallOrder();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());

        int row = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if(row > 0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     *
     * @param userId
     * @return
     */
    public ServerResponse getOrderCartProduct(Integer userId){
        OrderProductVo orderProductVo = new OrderProductVo();
        // 从购物车中获取数据
        List<MallCart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse serverResponse =  this.getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<MallOrderItem> orderItemList =( List<MallOrderItem> ) serverResponse.getData();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");
        for(MallOrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return ServerResponse.createBySuccess(orderProductVo);
    }

    /**
     *
     * @param userId
     * @param orderNo
     * @return
     */
    public ServerResponse<OrderVo> getOrderDetail(Integer userId ,Long orderNo){
        MallOrder order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order != null){
            List<MallOrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return  ServerResponse.createByErrorMessage("没有找到该订单");
    }

    /**
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<MallOrder> orderList = orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList = assembleOrderVoList(orderList,userId);
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderVoList);
        return ServerResponse.createBySuccess(pageResult);
    }
}