package com.bobomico.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * @ClassName: com.bobomico.pojo.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  2:53
 * @Description: 用户权限 + 用户登录信息
 * @version:
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActiveUser implements Serializable {
    private Long id;
    private String name;
}
