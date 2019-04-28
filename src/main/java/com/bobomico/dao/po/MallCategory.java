package com.bobomico.dao.po;

import lombok.*;

import java.util.Date;

/**
 * of 只包含（白名单
 * exclude 排除（黑名单
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MallCategory {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}