package com.bobomico.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.bobomico.common.Const;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.BaseController;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.IOrderService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName: com.bobomico.controller.portal.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/4  16:18
 * @Description:
 * @version:
 */
@RestController
@RequestMapping("/order/")
public class OrderController extends BaseController {

    private static  final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     * 支付接口
     * @param orderNo
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("pay.do")
    @RequiresAuthentication
    public ServerResponse pay(
            Long orderNo, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        String path = session.getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, sul.getSysUserId(), path);
    }

    /**
     * alipay回调接口
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback.do")
    @RequiresAuthentication
    public Object alipayCallback(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){
                valueStr = (i == values.length -1) ? valueStr + values[i] : valueStr + values[i]+",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        // 非常重要 验证回调的正确性 是不是支付宝发的 并且还要避免重复通知

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }

        //todo 验证各种数据

        // 后续业务逻辑
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    /**
     * 前台轮询支付宝订单状态
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(
            Long orderNo, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(sul.getSysUserId(), orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    /**
     * 创建订单
     * @param shippingId
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("create.do")
    @RequiresAuthentication
    public ServerResponse create(
            Integer shippingId, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iOrderService.createOrder(sul.getSysUserId(), shippingId);
    }

    /**
     *
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("cancel.do")
    @RequiresAuthentication
    public ServerResponse cancel(
            Long orderNo, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iOrderService.cancel(sul.getSysUserId(), orderNo);
    }

    /**
     *
     * @param session
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    @RequiresAuthentication
    public ServerResponse getOrderCartProduct(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iOrderService.getOrderCartProduct(sul.getSysUserId());
    }

    /**
     *
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("detail.do")
    @RequiresAuthentication
    public ServerResponse detail(Long orderNo, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iOrderService.getOrderDetail(sul.getSysUserId(), orderNo);
    }

    /**
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @RequiresAuthentication
    public ServerResponse list(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value="pageNum",defaultValue="1")int pageNum, @RequestParam(value="pageSize",defaultValue="10")int pageSize){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iOrderService.getOrderList(sul.getSysUserId(), pageNum, pageSize);
    }
}