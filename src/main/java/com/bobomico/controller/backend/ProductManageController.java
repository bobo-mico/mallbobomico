package com.bobomico.controller.backend;

import com.bobomico.common.ServerResponse;
import com.bobomico.dao.po.MallProduct;
import com.bobomico.service.IFileService;
import com.bobomico.service.IProductService;
import com.bobomico.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @ClassName: com.bobomico.controller.backend.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  0:02
 * @Description: 后台 - 控制器 商品管理
 * @version:
 */
@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 保存或更新商品
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse productSave(MallProduct product){
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 修改产品销售状态
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse setSaleStatus(Integer productId,Integer status){
        return iProductService.setSaleStatus(productId,status);
    }

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse getDetail(Integer productId){
        return iProductService.manageProductDetail(productId);
    }

    /**
     * 商品列表 分页 PageHelper
     * @param pageNum 页码
     * @param pageSize 页面容量
     * @return
     */
    @RequestMapping("list.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse getList(
            @RequestParam(value="pageNum", defaultValue="1") int pageNum,
            @RequestParam(value="pageSize", defaultValue="10") int pageSize){
        return iProductService.getProductList(pageNum,pageSize);
    }

    /**
     * 商品搜索
     * @param productName 可根据商品名
     * @param productId   可根据商品id
     * @param pageNum     如果返回多行 可分页展示
     * @param pageSize    页面容量
     * @return
     */
    @RequestMapping("search.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse productSearch(String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    /**
     * 图片上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @RequiresRoles({"商品管理员"})
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        // todo guava
        String url = PropertiesUtil.getStringProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 富文本中的图片上传
     *  富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
     *  {
     *      "success": true/false,
     *      "msg": "error message", # optional
     *      "file_path": "[real file path]"
     *  }
     * @param file 不是必须的
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @RequiresRoles({"商品管理员"})
    public Map richtextImgUpload(
            @RequestParam(value="upload_file",required=false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getStringProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}