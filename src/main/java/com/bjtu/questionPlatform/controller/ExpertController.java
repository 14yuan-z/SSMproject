package com.bjtu.questionPlatform.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjtu.questionPlatform.entity.*;
import com.bjtu.questionPlatform.service.*;


import com.bjtu.questionPlatform.entity.Expert;
import com.bjtu.questionPlatform.entity.User;
import com.bjtu.questionPlatform.service.ExpertService;
import com.bjtu.questionPlatform.service.MailService;
import com.bjtu.questionPlatform.service.UserService;
import com.bjtu.questionPlatform.utils.InviteCodeUtils.InviteCodeUtils;

import com.bjtu.questionPlatform.utils.resultUtils.ResponseResultBody;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/Expert")
public class ExpertController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private JudgementService judgementService;
    @Autowired

    private ScoreService scoreService;
    @Autowired
    private ExpertService expertService;


    //专家登录
    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/expertLogin")
    public HashMap<String, Object> expertLogin(@RequestBody Expert expert) {
        expertService.expertLogin(expert.getExpertName(), expert.getCode());
        HashMap<String, Object> data = new HashMap<>();
        data.put("expert", expertService.selectExpertByExpertName(expert.getExpertName()));
        return data;
    }


    // 返回报告详情和打分指标
    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getOneReport")
    public HashMap<String, Object> getReportJudge(@RequestBody Report report) {
        List<HashMap<String, Object>> judgements = new ArrayList<>();
        HashMap<String, Object> jClass = new HashMap<>();

        Report r = reportService.selectReportById(report.getReportId());
        // 获取报告pdf内容
        String url = "http://localhost:8090/static/" + r.getReportPath();
        // 获取该报告的jclass
        JudgeClass judgeClass = judgementService.getjClass(r.getJClassId());
        List<Judgement> j = judgementService.getJudgementByJClassId(r.getJClassId());

        for (int i = 0; i < j.size(); i++) {
            HashMap<String, Object> judgement = new HashMap<>();
            judgement.put("judgeId", j.get(i).getJudgementid());
            judgement.put("judgeName", j.get(i).getJudgementname());
            judgement.put("judgeContent", j.get(i).getJudgementcontent());
            judgement.put("judgeProportion", j.get(i).getJudgementproportion());
            judgements.add(judgement);
        }
        jClass.put("judgements", judgements);
        jClass.put("jClassId", r.getJClassId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("reportPdf", url);
        data.put("jClass", jClass);
        data.put("reportStatus", r.getReportStatus());
        return data;
    }

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/sendScores")
    public void sendScores(@RequestBody Score score) {
        Report report = reportService.selectReportById(score.getReportId());
//        System.out.println(score.getReportId());
        // 插入每个score和judgeId
        String judgeWithScore = score.getJudgeWithScore();
        System.out.println("judgeWithScore" + judgeWithScore);
        JSONArray jsonArray = JSON.parseArray(judgeWithScore);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Score s = new Score();
            // 插入judgement相关数据
            s.setExpertname(score.getExpertname());
            s.setReportId(score.getReportId());
            s.setJudgementid(jsonObject.getString("judgeId"));
            s.setScore(jsonObject.getString("score"));
            s.setID(report.getID());
//            scoreService.createScore(s);
        }

        Score totalScore = new Score();
        totalScore.setTotalScore(score.getTotalScore());
        totalScore.setSuggestion(score.getSuggestion());
        totalScore.setReportId(score.getReportId());
        totalScore.setExpertname(score.getExpertname());
        scoreService.createTotalScore(totalScore);
        // 专家打完分把打分状态改为1
//        System.out.println("haha" + score.getReportId());
//        System.out.println("haha" + score.getExpertname());
        reportService.modifyFinishStatus(1, score.getReportId(), score.getExpertname());
        // 判断是否可以把报告状态改为打分完成
        // 获取这个报告id和完成状态为0的列表
        List<ExpertReport> list = reportService.getExpertReport(score.getReportId(), 0);
        if (list.size() == 0) {
            reportService.modifyReportStatus(4, score.getReportId());
        }
    }

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getScoreDetails")
    public HashMap<String, Object> getScoreDetails(@RequestBody Score score) {
//        System.out.println(score);


        List<HashMap<String, Object>> judgements = new ArrayList<>();
        HashMap<String, Object> jClass = new HashMap<>();

        Report r = reportService.selectReportById(score.getReportId());

        //获取打分情况
        List<TotalScore> sc = reportService.selectTotalScoreByReportId(r.getReportId());
        String totalScore = sc.get(0).getTotalscore();
        String suggestion = sc.get(0).getSuggestion();

        // 获取报告pdf内容
        String url = "http://localhost:8090/static/" + r.getReportPath();
        // 获取该报告的jclass
        JudgeClass judgeClass = judgementService.getjClass(r.getJClassId());
        List<Judgement> j = judgementService.getJudgementByJClassId(r.getJClassId());
        List<HashMap<String, Object>> keyWord = new ArrayList<>();

        //获取关键词
        List<KeyWord> w = reportService.selectKeyWordByReportId(score.getReportId());
        for (int i = 0; i < w.size(); i++) {
            HashMap<String, Object> word = new HashMap<>();
            word.put("word", w.get(i).getKeysContent());
            keyWord.add(word);
        }


        //获取指标详情
        for (int i = 0; i < j.size(); i++) {
            HashMap<String, Object> judgement = new HashMap<>();
//            System.out.println("ReportID" + score.getReportId());
//            System.out.println("judgeName" + score.getExpertname());
//            System.out.println("judgeId" + j.get(i).getJudgementid());
//            Score s = expertService.selectScore(score.getReportId(), j.get(i).getJudgementid(), score.getExpertname());
            judgement.put("judgeId", j.get(i).getJudgementid());
            judgement.put("judgeName", j.get(i).getJudgementname());
            judgement.put("judgeContent", j.get(i).getJudgementcontent());
            judgement.put("judgeProportion", j.get(i).getJudgementproportion());

//            judgement.put("score", s.getScore());
            judgements.add(judgement);
        }
        jClass.put("judgement", judgements);
        jClass.put("jClassId", r.getJClassId());
        jClass.put("jClassName", judgeClass.getJClassName());
        jClass.put("managerId", judgeClass.getManagerId());
//        System.out.println("jClass:"+jClass);


        HashMap<String, Object> data = new HashMap<>();
        data.put("reportPdf", url);
        data.put("jClass", jClass);
        data.put("totalScore", totalScore);
        data.put("suggestion", suggestion);
        data.put("keyWords", keyWord);
        data.put("reportStatus", r.getReportStatus());
//        System.out.println("data:"+data);


        return data;
    }


    private MailService mailService;

    @Autowired
    private InviteCodeUtils inviteCodeUtils;


//    @CrossOrigin
//    @ResponseResultBody
//    @PostMapping(value = "/sendCode")
//    public void sendVerifyCode(@RequestBody String expertName) {
//        String expertCode =inviteCodeUtils.setCode(expertName); // 根据专家姓名发送6位邀请码，有效时间6小时
//        expertService.invite(expertName,"123.com",expertCode);
//    }


    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getRatingReportList")
    public HashMap<String, Object> getReportList(@RequestBody Expert expert) {
        List<HashMap<String, Object>> reports = new ArrayList<>();
        List<HashMap<String, Object>> keyWords = new ArrayList<>();

//        System.out.println(expert.getExpertName());
        List<String> reportIds = reportService.selectReportIdByExpertName(expert.getExpertName(), 0);
        return getStringObjectHashMap(reports, keyWords, reportIds);
    }

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getRatedReportList")
    public HashMap<String, Object> getRatedReportList(@RequestBody Expert expert) {
        List<HashMap<String, Object>> reports = new ArrayList<>();
        List<HashMap<String, Object>> keyWords = new ArrayList<>();

//        System.out.println(expert.getExpertName());
        List<String> reportIds = reportService.selectReportIdByExpertName(expert.getExpertName(), 1);
        return getStringObjectHashMap(reports, keyWords, reportIds);
    }

    private HashMap<String, Object> getStringObjectHashMap(List<HashMap<String, Object>> reports, List<HashMap<String, Object>> keyWords, List<String> reportIds) {
        for (int i = 0; i < reportIds.size(); i++) {
            String ReportId;
            ReportId = reportIds.get(i);
            Report report = reportService.selectReportById(ReportId);
            HashMap<String, Object> item = new HashMap<>();
            item.put("reportId", ReportId);
            String n = report.getReportName();
            int dot = n.lastIndexOf('.');
            if ((dot > -1) && (dot < (n.length()))) {
                n = n.substring(0, dot);
            }
            item.put("reportName", n);
            item.put("createTime", report.getReportTime().toLocalDateTime().toString().replace("T", ","));
            item.put("reportStatus", report.getReportStatus());


            List<KeyWord> w = reportService.selectKeyWordByReportId(ReportId);
            List<HashMap<String, Object>> keyWordsOfTheReport = new ArrayList<>();
            for (int j = 0; j < w.size(); j++) {
                HashMap<String, Object> wordOfTheReport = new HashMap<>();
                wordOfTheReport.put("word", w.get(j).getKeysContent());
                keyWordsOfTheReport.add(wordOfTheReport);

                HashMap<String, Object> word = new HashMap<>();
                word.put("reportId", ReportId);
                word.put("word", w.get(j).getKeysContent());
                keyWords.add(word);

            }
            item.put("keyWord", keyWordsOfTheReport);
            reports.add(item);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("reports", reports);
        data.put("keyWords", keyWords);
        return data;
    }


    //    报告导出为模板
    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/exportReport")
    public HashMap<String, Object> exportReport(@RequestBody TotalScore report) throws FileNotFoundException, UnsupportedEncodingException {
//        System.out.println(report);
        int totalscore_ = Integer.parseInt(scoreService.getTotalScore(report));
//        System.out.println(totalscore_);
        String grade = "";
        String ibs_ = "";   //传给ibs系统评分的结果
        String remark = "";  //用于存储模板评语
        //根据分数判断等级
        if (totalscore_ < 60) {
            grade = "不合格";
            ibs_ = "不合格";
            remark = "内容缺乏严谨性，需要改正。";
        } else {
            ibs_ = "合格";
            if (totalscore_ < 70) {
                grade = "中";
                remark = "整体满足合格的需求，但仍有很大的进步空间。";
            } else if (totalscore_ < 85) {
                grade = "良";
                remark = "整体比较合理，格式规范。";
            } else {
                grade = "优";
                remark = "本报告结构完整，格式规范，主题突出，层次清楚，逻辑性强，文笔流畅";

            }
        }
        String word_name = UUID.randomUUID().toString()+".doc";
        System.out.println(word_name);


        String path = "./"+word_name;

        PrintWriter out = new PrintWriter("./src/main/resources/static/"+word_name, "GBK");
        out.println("报告名称："+reportService.searchReportNameByID(report.getReportid()));
        out.println("评分专家："+report.getExpertname());
        out.println("得分情况："+grade);
        out.println("评语:"+remark);
        out.close();
        String url = "http://localhost:8090/static/" + word_name;

        File file = new File(path);
        String fileName=file.getName();
        System.out.println(fileName);

        HashMap<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("score",ibs_);
        return data;
    }


}