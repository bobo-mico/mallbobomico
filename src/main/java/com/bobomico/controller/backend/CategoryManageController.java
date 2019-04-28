package com.bobomico.controller.backend;

import com.bobomico.common.ServerResponse;
import com.bobomico.service.ICategoryService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: com.ccfit.po.mybatismain
 * @Author: Lion
 * @Date: 2018/6/20  8:13
 * @Description: 后台 - 分类管理
 * @version: beta
 */
@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加商品分类
     * 角色 - Commodity Administrator 商品管理员
     * @param categoryName
     * @param parentId
     * @return
     */
    @PostMapping("add_category.do")
    @RequiresRoles(value={"商品管理员"}, logical=Logical.OR)
    public ServerResponse addCategory(
            String categoryName, @RequestParam(value="parentId", defaultValue="0") int parentId){
       return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 更新商品分类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse setCategoryName(Integer categoryId, String categoryName){
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    /**
     * 获取平级分类信息
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse getChildrenParallelCategory(
            @RequestParam(value="categoryId", defaultValue="0") Integer categoryId){
        // 查询子节点的category信息, 不递归
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 获取某个分类及其子类信息
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse getCategoryAndDeepChildrenCategory(
            @RequestParam(value="categoryId", defaultValue="0") Integer categoryId){
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}