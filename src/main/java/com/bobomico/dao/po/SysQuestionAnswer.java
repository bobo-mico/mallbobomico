package com.bobomico.dao.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysQuestionAnswer {

    private Integer id;

    // 前端用来排序的编号 其实可以不要 直接通过id就可以排序
    private Integer qaNo;

    @JsonIgnore
    @JSONField(serialize=false)
    private Integer sysUserId;

    private String question;

    // @JsonIgnore // 忽略注解对进出都有效
    @JSONField(serialize=false)
    private String answer;

    @JsonIgnore
    @JSONField(serialize=false)
    private Date createTime;

    @JsonIgnore
    @JSONField(serialize=false)
    private Date updateTime;
}