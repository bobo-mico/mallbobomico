package com.bobomico.controller.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: com.bobomico.controller.vo.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  7:05
 * @Description: CartProductVo的集合
 * @version:
 */
public class CartVo {
    // 商品集合
    private List<CartProductVo> cartProductVoList;

    // 购物车总金额
    private BigDecimal cartTotalPrice;

    // 是否全选
    private Boolean allChecked;

    // 主图地址
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
