package com.bobomico.service.impl;

import com.bobomico.service.IFileService;
import com.bobomico.util.FTPUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName: com.bobomico.service.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  0:16
 * @Description: 图片上传服务
 * @version:
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    public String upload(MultipartFile file, String path){
        String fileName = file.getOriginalFilename();
        // todo 可用stream或者guava优化代码
        // 扩展名
        // abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 防止文件重名覆盖
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        // 日志插值表达式
        log.info("开始上传文件:上传文件的文件名:{}   上传的路径:{}    新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){          // 是否存在
            fileDir.setWritable(true);  // 如果不存在赋予可写权限
            fileDir.mkdirs();           // 可创建多级目录
        }
        // 完整的路径+文件名
        File targetFile = new File(path,uploadFileName);

        try {
            // 文件已上传到项目文件中
            file.transferTo(targetFile);
            // 从项目文件夹中上传到ftp图片服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 删除项目文件夹中的上传文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }
}