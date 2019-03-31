package com.bobomico.pojo;

/**
 * @ClassName: com.bobomico.pojo.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  3:46
 * @Description:
 * @version:
 */
public class Question {
    private Integer id;
    // 前端用来排序的编号 其实可以不要 直接通过id就可以排序
    private Integer qaNo;
    private String Question;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
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
}
