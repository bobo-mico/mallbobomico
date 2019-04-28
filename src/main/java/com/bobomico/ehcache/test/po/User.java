package com.bobomico.ehcache.test.po;

import lombok.*;

/**
 * @ClassName: com.bobomico.ehcache.test.po.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/29  2:44
 * @Description: 测试
 * @version:
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    public Integer id;
    public String name;
    public String password;
}
