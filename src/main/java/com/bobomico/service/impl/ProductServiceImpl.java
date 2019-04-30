package com.bobomico.service.impl;

import com.bobomico.common.Const;
import com.bobomico.common.ResponseCode;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.ProductDetailVo;
import com.bobomico.controller.vo.ProductListVo;
import com.bobomico.dao.MallCategoryMapper;
import com.bobomico.dao.MallProductMapper;
import com.bobomico.dao.po.MallCategory;
import com.bobomico.dao.po.MallProduct;
import com.bobomico.service.ICategoryService;
import com.bobomico.service.IProductService;
import com.bobomico.util.DateTimeUtil;
import com.bobomico.util.PropertiesUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: com.bobomico.service.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  0:09
 * @Description: 后台 - 服务层 商品管理
 * @version:
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private MallProductMapper productMapper;

    @Autowired
    private MallCategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 新增或更新商品 根据商品id
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(MallProduct product){
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                // todo guava
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            if(product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createBySuccess("更新产品失败");
            }else{
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createBySuccess("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    /**
     * 修改产品销售状态
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        // 非法参数
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MallProduct product = new MallProduct();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    /**
     * 管理商品详细信息
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        // 校验非法参数完全可以单独做一个函数
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MallProduct product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 装配商品详情vo
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(MallProduct product){
        // todo 有没有更好的办法呢?
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        // todo 可以用常量
        productDetailVo.setImageHost(
                PropertiesUtil.getStringProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        // 获取商品分类信息
        MallCategory category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        // 如果分类信息为空表示没有分类
        if(category == null){
            productDetailVo.setParentCategoryId(0); // 默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /**
     * 商品列表 分页 PageHelper
     * @param pageNum 页码
     * @param pageSize 页面容量
     * @return
     */
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize){
        // 起始页
        PageHelper.startPage(pageNum, pageSize);
        // 查询逻辑
        List<MallProduct> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(MallProduct productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        // 分页处理
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 装配vo assemble
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(MallProduct product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(
                PropertiesUtil.getStringProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /**
     * 商品查询 可根据不同条件查询
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return 返回列表
     */
    public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        // 支持模糊查询 todo 可改成guava
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        // 通过mybatis where来过滤查询条件
        List<MallProduct> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(MallProduct productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        // 分页
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 用户商品检索
     * @param productId
     * @return 如果商品不在售状态 则返回null
     */
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MallProduct product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 用户根据关键字检索商品详情 todo 排序功能有bug
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    public ServerResponse<PageInfo> getProductByKeywordCategory(
            String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList();
        if(categoryId != null){
            MallCategory category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                // 没有该分类 并且还没有关键字 这个时候返回一个空的结果集 不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        // 排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                // 满足pageHelper的排序格式
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }

        List<MallProduct> productList = productMapper.selectByNameAndCategoryIds(
                StringUtils.isBlank(keyword) ? null : keyword,
                categoryIdList.size()==0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(MallProduct product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
