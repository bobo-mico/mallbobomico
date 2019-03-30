package com.bobomico.dao;

import com.bobomico.dao.po.SysQuestionAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysQuestionAnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysQuestionAnswer record);

    int insertSelective(SysQuestionAnswer record);

    SysQuestionAnswer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysQuestionAnswer record);

    int updateByPrimaryKey(SysQuestionAnswer record);

    // 根据用户ID搜索所有问题
    List<SysQuestionAnswer> selectByUserId(Integer id);

    // 根据用户ID检查问题答案
    int checkAnswer(@Param("sys_user_id")Integer userId, @Param("question")String question, @Param("answer")String answer);
}