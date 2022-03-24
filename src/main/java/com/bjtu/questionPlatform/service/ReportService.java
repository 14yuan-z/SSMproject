package com.bjtu.questionPlatform.service;

import com.bjtu.questionPlatform.entity.*;

import java.util.List;

public interface ReportService {

    void upload(Report report);

    List<KeyWord> selectKeyWordByReportId(String reportId);

    List<Grade> selectGradesByReportId(String reportId);

    List<TotalScore> selectTotalScoreByReportId(String reportId);

    List<Score> selectScoreByReportId(String reportId);

    List<Judgement> selectJudgementByJudgementId(String judgementid);

    List<KeyWord> getAllKeyWords();

    void createKey(KeyWord keyWord);


    List<Report> selectReportByUserId(String ID);

    List<Report> selectReportByUsername(String username);

    //根据ReportID获取keysid
    List<String> searchkeysidByReportId(String reportId);

    List<Report> getAllReports();
//    int getmaxReportID();

    void createReport(Report report);


    Report selectReportById(String reportId);

    List<String> selectReportIdByExpertName(String ExpertName, int finish);

    void modifyReportStatus(int reportStatus, String reportId);

    void modifyFinishStatus(int finish, String reportId, String expertName);

    List<ExpertReport> getExpertReport(String reportId, int finish);

    void updateStatus(Report report);

    //根据id查询报告名称
    String searchReportNameByID(String reportId);

    //获取所有keyscontent
    List<String> getallKeysContent();

    //删除报告
    void deleteReport(String reportId, int status);

    //将对应关键字内容的keyword的连接到report
    void updatekeyword_reportid(String reportID, String keyscontent);


}
