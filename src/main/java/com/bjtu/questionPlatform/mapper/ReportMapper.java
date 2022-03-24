package com.bjtu.questionPlatform.mapper;


import com.bjtu.questionPlatform.entity.*;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReportMapper {
    @Select("select keysid from keyword where reportId = #{reportId}")
    List<String> searchkeysidByReportId(String reportId);

    @Select("select * from report where reportId = #{reportId}")
    Report selectReportById(String reportId);


    @Select("select * from report where ID = #{ID}")
    @Options(flushCache=Options.FlushCachePolicy.TRUE,useCache=false)
    List<Report> selectReportByUserId(String ID);

    @Select("select * from KeyWord where reportId = #{reportId}")
    List<KeyWord> selectKeyWordByReportId(String reportId);

    @Select("select * from totalscore where reportId = #{reportId}")
    List<Grade> selectGradesByReportId(String reportId);

    @Select("select * from totalscore where reportId = #{reportId}")
    List<TotalScore> selectTotalScoreByReportId(String reportId);

    @Select("select * from score where reportId = #{reportId}")
    List<Score> selectScoreByReportId(String reportId);

    @Select("select * from judgement where judgementid = #{judgementid}")
    List<Judgement> selectJudgementByJudgementId(String judgementid);

    @Insert("insert into report (reportId,ID,reportPath,reportTime,reportName) "+
            "values (#{reportId},#{ID},#{reportPath},NOW(),#{reportName},#{reportName})")
    void upload(Report report);

    @Select("select ReportId from expertReport where ExpertName = #{ExpertName} and finish=#{finish}")
    List<String> selectReportIdByExpertName(String ExpertName,int finish);

    // 不用了
    @Select("select * from report where username = #{username}")
    List<Report> selectReportByUsername(String username);


    @Select("select * from report")
    List<Report> getAllReports();

    @Select("select reportId from report")
    List<String> getmaxReportID();

    @Select("select * from KeyWord")
    List<KeyWord> getAllKeyWords();


    @Insert("insert into report (reportId,ID,reportPath,reportTime,reportName,reportStatus) "+
            "values (#{reportId},#{ID},#{reportPath},NOW(),#{reportName},#{reportStatus})")
    void createReport(Report report);

    @Insert("insert into keyWord (keysId,reportId,keysContent,keysTime) "+
            "values (#{keysId},#{reportId},#{keysContent},NOW())")
    void createKey(KeyWord keyWord);

    @Update("update report set reportStatus=#{reportStatus} where reportId=#{reportId}")
    void modifyReportStatus(int reportStatus,String reportId);

    @Update("update expertReport set finish=#{finish} where reportId=#{reportId} and expertName=#{expertName}")
    void modifyFinishStatus(int finish,String reportId,String expertName);

    @Select("select * from expertReport where reportId=#{reportId} and finish=#{finish}")
    List<ExpertReport> getExpertReport(String reportId,int finish);

    @Update("update report set reportStatus= #{reportStatus} where reportId= #{reportId}")
    void updateStatus(Report report);

    @Select("select reportname from report where reportId=#{reportId}")
    String searchReportNameByID(String reportId);

    //将对应关键字连接到report
    @Update("update keyword set  reportId = #{reportID} where keyscontent = #{keyscontent}")
    void updatekeyword_reportid(String reportID,String keyscontent);

    //获取所有关键字内容
    @Select("select keyscontent from keyword")
    List<String> getallKeysContent();

    //更新keyword的reportID
    @Update("update keyword set  reportId = #{updateid} where reportId = #{reportId}")
    void update_Keyword(String reportId,String updateid);

    //删除关键字
    @Delete("delete from keyword where reportId = #{reportId}")
    void deleteReport_Keyword(String reportId);
    //删除报告
    @Delete("delete from report where reportId = #{reportId}")
    void deleteReport_Id(String reportId);

    //删除待分配专家报告
    @Delete("delete from expertreport where reportId = #{reportId}")
    void deleteReport_expertreport(String reportId);

    //删除专家待打分报告
    @Delete("delete from totalscore where reportId = #{reportId}")
    void deleteReport_totalscore(String reportId);




}
