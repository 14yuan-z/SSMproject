package com.bjtu.questionPlatform.mapper;

import com.bjtu.questionPlatform.entity.JudgeClass;
import com.bjtu.questionPlatform.entity.Score;
import com.bjtu.questionPlatform.entity.TotalScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ScoreMapper {
    @Insert("insert into score (expertName,judgementId,reportId,ID,score) "+
            "values (#{expertname},#{judgementid},#{reportId},#{ID},#{score})")
    void createScore(Score score);

    @Insert("insert into totalscore (expertName,reportId,suggestion,totalScore) "+
            "values (#{expertname},#{reportId},#{suggestion},#{totalScore})")
    void createTotalScore(Score score);

    @Select("select totalscore from totalscore where expertname = #{expertname} and reportid=#{reportid}")
    String getTotalScore(TotalScore score);
}
