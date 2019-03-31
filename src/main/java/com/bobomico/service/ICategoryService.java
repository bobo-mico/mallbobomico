package com.bobomico.service;

import com.bobomico.common.ServerResponse;
import com.bobomico.dao.po.MallCategory;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: com.ccfit.po.mybatismain
 * @Author: Lion
 * @Date: 2018/6/20  8:13
 * @Description: 后台 - 商品分类管理
 * @version: beta
 */
public interface ICategoryService {

    // 添加商品分类
    ServerResponse addCategory(String categoryName, Integer parentId);

    // 更新商品分类名称
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    // 添加子级商品分类
    ServerResponse<List<MallCategory>> getChildrenParallelCategory(Integer categoryId);

    // 检索分类及子分类
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
