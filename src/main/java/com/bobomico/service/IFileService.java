package com.bobomico.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: com.bobomico.service.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  0:14
 * @Description: 函数式接口
 * @version:
 */
@FunctionalInterface
public interface IFileService {
    String upload(MultipartFile file, String path);
}
