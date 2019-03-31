package com.bobomico.dao;

import com.bobomico.dao.po.MallProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallProduct record);

    int insertSelective(MallProduct record);

    MallProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallProduct record);

    int updateByPrimaryKey(MallProduct record);

    // 分页查询 商品列表
    List<MallProduct> selectList();

    // 根据商品名或分类信息查询商品
    List<MallProduct> selectByNameAndProductId(
            @Param("productName")String productName, @Param("productId") Integer productId);

    // 根据分类id查询商品
    List<MallProduct> selectByNameAndCategoryIds(
            @Param("productName")String productName,@Param("categoryIdList")List<Integer> categoryIdList);
}