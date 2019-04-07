package com.bobomico.service.impl;

import com.bobomico.common.Const;
import com.bobomico.common.ResponseCode;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.CartProductVo;
import com.bobomico.controller.vo.CartVo;
import com.bobomico.dao.MallCartMapper;
import com.bobomico.dao.MallProductMapper;
import com.bobomico.dao.po.MallCart;
import com.bobomico.dao.po.MallProduct;
import com.bobomico.service.ICartService;
import com.bobomico.util.BigDecimalUtil;
import com.bobomico.util.PropertiesUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: com.bobomico.service.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  7:10
 * @Description: 购物车 - 服务层
 * @version:
 */
@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    @Autowired
    private MallCartMapper cartMapper;

    @Autowired
    private MallProductMapper productMapper;

    /**
     * 向购物车添加商品
     * @param userId
     * @param productId
     * @param count
     * @return CarVo
     */
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count){
        // 参数检查
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        // 先查看商品是否已经在该购物车
        MallCart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if(cart == null){
            // 这个产品不在这个购物车 需要新new一个商品
            MallCart cartItem = new MallCart();
            cartItem.setProductId(productId);           // 商品id
            cartItem.setQuantity(count);                // 数量
            cartItem.setChecked(Const.Cart.CHECKED);    // 放入购车的商品 默认为选中状态
            cartItem.setSysUserId(userId);              // 用户id
            cartMapper.insert(cartItem);                // 放入购物车
        }else{
            // 如果商品已存在 追加数量
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);   // 更新购物车
        }
        return this.list(userId);   // 返回更新后的购物车
    }

    /**
     * 查看购物车
     * @param userId
     * @return vo
     */
    public ServerResponse<CartVo> list (Integer userId){
        // 获取购物车详细信息
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 更新购物车
     * @param userId
     * @param productId
     * @param count
     * @return 更新后的购物车列表
     */
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MallCart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);    // 如果购物车存在同类产品 那么就修改数量
        }
        cartMapper.updateByPrimaryKey(cart);
        return this.list(userId);
    }

    /**
     * 删除购物车商品
     * @param userId
     * @param productIds
     * @return 删除后的购物车列表
     */
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId, productList);
        return this.list(userId);
    }

    /**
     *
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo> selectOrUnSelect (Integer userId, Integer productId, Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    /**
     * 核心方法 装配购物车vo
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        // 数据库是用一张表来做购物车的 所以一个客户可能拥有多条购物车记录
        List<MallCart> cartList = cartMapper.selectCartByUserId(userId);

        /* 购物车商品vo
           逆向工程生成的购物车对象并没有跟商品一对多的设计
           因此需要一个vo包装类来模拟购物车中包含了多个商品的状态 就像hibernate那样处理一对多的关系
           这也是我们要返回给前端的关键数据  */
        List<CartProductVo> cartProductVoList = Lists.newArrayList();   // Guava泛型推断

        // 购物车总金额
        BigDecimal cartTotalPrice = new BigDecimal("0");

        // 如果用户的购物车不是空的
        if(CollectionUtils.isNotEmpty(cartList)){
            // 遍历用户已经存在的购物车信息 并装配到购物车商品vo中
            for(MallCart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());

                // 数据库的购物车表能查出的商品信息是有限的 我们需要继续查询数据库
                MallProduct product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    // 判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        // 库存充足 ERP来做这件事会更专业
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);   // 库存充足 无须修改
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);      // 库存不足 需修改
                        // 准备去数据库更新购物车中某商品的有效库存
                        MallCart cartForQuantity = new MallCart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        // 数据库同步更新购物车商品数量
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    // 计算总价
                    cartProductVo.setProductTotalPrice(
                            BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }

                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    // 如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(
                            cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
