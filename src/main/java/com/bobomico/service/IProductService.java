package com.bobomico.service;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.ProductDetailVo;
import com.bobomico.dao.po.MallProduct;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: com.bobomico.service.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  0:05
 * @Description: 后台 - 商品管理
 * @version:
 */
public interface IProductService {

    // 保存或更新商品
    ServerResponse saveOrUpdateProduct(MallProduct product);

    // 修改产品销售状态
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    // 管理商品详细信息
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    // 获取商品列表 - 分页
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    // 查询商品
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    // 客户端查看商品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    // 客户端搜索分类
    ServerResponse<PageInfo> getProductByKeywordCategory(
            String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

}