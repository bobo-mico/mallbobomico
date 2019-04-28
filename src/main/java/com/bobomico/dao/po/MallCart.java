package com.bobomico.dao.po;

import lombok.*;

import java.util.Date;

/**
 * Data - getter setter equals hashcode canEqual
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MallCart {
    private Integer id;

    private Integer sysUserId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;
}