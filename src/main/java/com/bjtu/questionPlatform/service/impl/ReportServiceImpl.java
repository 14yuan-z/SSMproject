package com.bjtu.questionPlatform.service.impl;


import com.bjtu.questionPlatform.entity.*;

import com.bjtu.questionPlatform.mapper.ReportMapper;
import com.bjtu.questionPlatform.mapper.UserMapper;
import com.bjtu.questionPlatform.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public void upload(Report report) {

    }

    @Override
    public List<String> searchkeysidByReportId(String reportId) {
        return reportMapper.searchkeysidByReportId(reportId);
    }

    @Override
    public List<KeyWord> selectKeyWordByReportId(String reportId) {
        return reportMapper.selectKeyWordByReportId(reportId);
    }

    @Override
    public List<Grade> selectGradesByReportId(String reportId){
        return reportMapper.selectGradesByReportId(reportId);
    }
    @Override
    public List<TotalScore> selectTotalScoreByReportId(String reportId){
        return reportMapper.selectTotalScoreByReportId(reportId);
    }
    @Override
    public List<Score> selectScoreByReportId(String reportId){
        return reportMapper.selectScoreByReportId(reportId);
    }
    @Override
    public List<Judgement> selectJudgementByJudgementId(String judgementid){
        return reportMapper.selectJudgementByJudgementId(judgementid);
    }


    @Override
    public List<KeyWord> getAllKeyWords() {
        return reportMapper.getAllKeyWords();
    }

    @Override
    public void createKey(KeyWord keyWord) {
        reportMapper.createKey(keyWord);
    }

    @Override
    public List<Report> selectReportByUserId(String ID) {
        return reportMapper.selectReportByUserId(ID);
    }

    @Override
    public List<Report> selectReportByUsername(String username) {
        return reportMapper.selectReportByUsername(username);
    }

    @Override
    public List<Report> getAllReports() {
        return reportMapper.getAllReports();
    }

//    @Override
//    public int getmaxReportID() {
//        List<String> strings = reportMapper.getmaxReportID();
//        Math.
//    }

    @Override
    public void createReport(Report report) {
        reportMapper.createReport(report);
    }


    @Override
    public Report selectReportById(String reportId) {
        return reportMapper.selectReportById(reportId);
    }

    @Override
    public List<String> selectReportIdByExpertName(String ExpertName,int finish) {
        return reportMapper.selectReportIdByExpertName(ExpertName,finish);
    }

    @Override
    public void modifyReportStatus(int reportStatus, String reportId) {
        reportMapper.modifyReportStatus(reportStatus,reportId);
    }

    @Override
    public void modifyFinishStatus(int finish, String reportId,String expertName) {
        reportMapper.modifyFinishStatus(finish,reportId,expertName);
    }

    @Override
    public List<ExpertReport> getExpertReport(String reportId,int finish) {
        return reportMapper.getExpertReport(reportId,finish);
    }

    public void updateStatus(Report report){
        reportMapper.updateStatus(report);
    }


    @Override
    public String searchReportNameByID(String reportId) {
        return reportMapper.searchReportNameByID(reportId);
    }

    @Override
    public List<String> getallKeysContent() {
        return reportMapper.getallKeysContent();
    }

    @Override
    public void updatekeyword_reportid(String reportID,String keyscontent) {
        reportMapper.updatekeyword_reportid(reportID,keyscontent);
    }

    @Override
    public void deleteReport(String reportId, int status) {
        switch (status){
            case 1 :
            case 2 :
//                System.out.println("??????????????????");
//                reportMapper.deleteReport_expertreport();
                reportMapper.update_Keyword(reportId,"0");
//                reportMapper.deleteReport_Keyword(reportId);
                reportMapper.deleteReport_Id(reportId);
                break;
//            case 2 :
////                System.out.println("???????????????");
//                reportMapper.deleteReport_Keyword(reportId);
//                reportMapper.deleteReport_Id(reportId);
//                break;
            case 3 :
//                System.out.println("???????????????");
                reportMapper.update_Keyword(reportId,"0");
                reportMapper.deleteReport_expertreport(reportId);
                reportMapper.deleteReport_Id(reportId);

                break;
            case 4 :
//                System.out.println("???????????????");
                reportMapper.update_Keyword(reportId,"0");
                reportMapper.deleteReport_expertreport(reportId);
                reportMapper.deleteReport_totalscore(reportId);
                reportMapper.deleteReport_Id(reportId);
                break;

        }

//        reportMapper.deleteReport1(reportId);
    }



}
