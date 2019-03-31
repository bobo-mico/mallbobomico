package com.bobomico.service.impl;

import com.bobomico.common.ServerResponse;
import com.bobomico.dao.MallCategoryMapper;
import com.bobomico.dao.po.MallCategory;
import com.bobomico.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: com.bobomico.service.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  11:08
 * @Description: 商品分类管理
 * @version:
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private MallCategoryMapper categoryMapper;

    /**
     * 添加商品分类
     * @param categoryName 分类名
     * @param parentId     分类的父节点
     * @return
     */
    public ServerResponse addCategory(String categoryName, Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){  // 如果父节点为空或者分类名为空报错 又是校验 没意思
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        MallCategory category = new MallCategory();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true); // 分类状态 1 可用 2 废弃

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 更新商品分类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        MallCategory category = new MallCategory();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    /**
     * 获取商品子分类
     * @param categoryId
     * @return
     */
    public ServerResponse<List<MallCategory>> getChildrenParallelCategory(Integer categoryId){
        List<MallCategory> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查询本级分类及子分类
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<MallCategory> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);


        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(MallCategory categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归查询子分类
     * @param categorySet
     * @param categoryId
     */
    private void findChildCategory(Set<MallCategory> categorySet, Integer categoryId){ // categoryId = 100006
        MallCategory category = categoryMapper.selectByPrimaryKey(categoryId);  // select * from mmall_category where id = 100006
        if(category != null){   // true
            categorySet.add(category);  // 家用电器 冰箱 双开门冰箱
        }
        // categoryList 是一个临时的中转站 记录了某分类下的所有子级分类
        List<MallCategory> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(MallCategory categoryItem : categoryList){  // 100006
            findChildCategory(categorySet, categoryItem.getId());
        }
    }
}