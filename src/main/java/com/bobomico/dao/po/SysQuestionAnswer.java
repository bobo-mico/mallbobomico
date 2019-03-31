package com.bobomico.dao.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

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

    public SysQuestionAnswer(Integer id, Integer qaNo, Integer sysUserId, String question, String answer, Date createTime, Date updateTime) {
        this.id = id;
        this.qaNo = qaNo;
        this.sysUserId = sysUserId;
        this.question = question;
        this.answer = answer;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SysQuestionAnswer() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQaNo() {
        return qaNo;
    }

    public void setQaNo(Integer qaNo) {
        this.qaNo = qaNo;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}