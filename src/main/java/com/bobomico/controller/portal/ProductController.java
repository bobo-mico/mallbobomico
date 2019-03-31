package com.bobomico.controller.portal;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.ProductDetailVo;
import com.bobomico.service.IProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: com.bobomico.controller.portal.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  4:12
 * @Description: 客户端 - 商品
 *                  无需任何权限
 * @version:
 */
@RestController
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 用户根据商品ID获取商品详情
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    /**
     * 用户根据关键字搜索商品详情
     * @param keyword       关键字  可为空
     * @param categoryId    分类    可为空
     * @param pageNum       页码
     * @param pageSize      页面容量
     * @param orderBy       排序规则
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}