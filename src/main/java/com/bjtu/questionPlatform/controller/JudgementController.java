package com.bjtu.questionPlatform.controller;


import com.bjtu.questionPlatform.entity.*;
import com.bjtu.questionPlatform.service.JudgementService;

import com.bjtu.questionPlatform.service.ReportService;

import com.bjtu.questionPlatform.utils.resultUtils.ResponseResultBody;


import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: questionPlatform_back_end
 * @description: judgement controller
 * @author: nancy_wdi
 * @create: 2021-05-04 11:13
 * @version: 1.0
 **/

@Slf4j
@RestController
@RequestMapping(value = "/api/judgement")
public class JudgementController {
    @Autowired
    private JudgementService judgementService;

    @Autowired
    private ReportService reportService;


    @CrossOrigin
    @ResponseResultBody

    @PostMapping(value = "/allocateJudgement")
    public void allocateJudgement(@RequestBody HashMap<String,String> P) {

        Report report = new Report();
        report.setReportId(P.get("reportId"));
        report.setJClassId(P.get("jClassId"));
//        System.out.println(report);
//        System.out.println("获取指标类"+report);
//        System.out.println(report.getReportId());
//        System.out.println(report.getJClassId());
//        //report.setJClassId(1+"");
        judgementService.allocateJudge(report);
        report.setReportStatus("2");
        reportService.updateStatus(report);

    }

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getOneJudgement")
    public HashMap<String, Object> getOneJudgement(@RequestBody HashMap<String, String> judgement) {
        String jClassId = judgement.get("jClassId");
//        System.out.println("前端获取指标类"+ jClassId);

        List<HashMap<String, Object>> jd = new ArrayList<>();


        List<Judgement> j = judgementService.selectJudgementByJClassId(jClassId);

        for (int i = 0; i < j.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();

            item.put("judgementId", j.get(i).getJudgementid());
            item.put("judgementName", j.get(i).getJudgementname());
            item.put("judgementContent", j.get(i).getJudgementcontent());
            item.put("judgementProportion", j.get(i).getJudgementproportion());
            item.put("managerId", j.get(i).getManagerid());

            jd.add(item);

        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("judgement", jd);


        System.out.println("单一指标类" + data);
        return data;
    }


    @PostMapping(value = "/newJudgement")
    public Pair getList(@RequestBody JudgeClass j) {
//        System.out.println(j);
        // 插入指标类基本信息
        List<JudgeClass> js = judgementService.getAllJClasses();
        int num = js.size() + 1;
        JudgeClass jc = new JudgeClass();
        jc.setJClassId(num + "");
        jc.setJClassName(j.getJClassName());
        jc.setManagerId(j.getManagerId());
        judgementService.createJClass(jc);

        // 插入每个judgement
        String judgement = j.getJudgement();
//        System.out.println("judgement" + judgement);
        JSONArray jsonArray = JSON.parseArray(judgement);

        List<Judgement> jgts = judgementService.getAllJudgements();
        int total = jgts.size() + 1;

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Judgement judge = new Judgement();
            // 插入judgement相关数据
            judge.setJudgementcontent(jsonObject.getString("judgementcontent"));
            judge.setJClassId(num + "");
            judge.setJudgementname(jsonObject.getString("judgementname"));
            judge.setJudgementproportion(jsonObject.getString("judgementproportion"));
            judge.setManagerid(j.getManagerId());
            judge.setJudgementid(total + "");
            judgementService.createJudgement(judge);
            total++;
        }

        return Pair.of("Status", 1);

    }


    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getJClassList")
    public HashMap<String, Object> getJClassList() {
        List<HashMap<String, Object>> JClass = new ArrayList<>();
        System.out.println("获取所有指标类");
        List<JudgeClass> jc = judgementService.getAllJudgeClass();
        for (int i = 0; i < jc.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("jClassId", jc.get(i).getJClassId());
            item.put("jClassName", jc.get(i).getJClassName());
            item.put("jClassTime", jc.get(i).getJClassTime().toLocalDateTime().toString().replace("T", ","));
            item.put("managerid", jc.get(i).getManagerId());
            JClass.add(item);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("jClass", JClass);
//        System.out.println(data);
        return data;

    }


    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getAllReportList")
    public HashMap<String, Object> getAllReportList() {
        List<HashMap<String, Object>> reports = new ArrayList<>();
        List<HashMap<String, Object>> keyWords = new ArrayList<>();

        System.out.println("获取所有报告");

        List<Report> reportlist = reportService.getAllReports();
        for (int i = 0; i < reportlist.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            String ReportId;
            ReportId = reportlist.get(i).getReportId();
            item.put("reportId", ReportId);
            String Status = reportlist.get(i).getReportStatus();
            item.put("reportStatus", Status);
            String n = reportlist.get(i).getReportName();
            int dot = n.lastIndexOf('.');
            if ((dot > -1) && (dot < (n.length()))) {
                n = n.substring(0, dot);
            }
            item.put("reportName", n);
//            System.out.println(reportlist.get(i).getReportTime().toLocalDateTime().toString().replace("T", ","));
            item.put("createTime", reportlist.get(i).getReportTime().toLocalDateTime().toString().replace("T", ","));
            List<KeyWord> w = reportService.selectKeyWordByReportId(reportlist.get(i).getReportId());
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
            item.put("jClassName", judgementService.getjClassName(reportlist.get(i).getJClassId()));
            reports.add(item);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("reports", reports);
        data.put("keyWords", keyWords);
//        System.out.println(data);
        return data;


    }


    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getExpertList")
    public HashMap<String, Object> getExpertList() {
        List<HashMap<String, Object>> experts = new ArrayList<>();

//        System.out.println("获取所有专家");

        List<Expert> e = judgementService.getAllExperts();
        for (int i = 0; i < e.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("expertName", e.get(i).getExpertName());
            item.put("keysId", e.get(i).getKeysId());
            item.put("expertType", e.get(i).getExpertType());
            item.put("expertUnit", e.get(i).getExpertUnit());
            item.put("expertInformation", e.get(i).getExpertInformation());
            experts.add(item);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("experts", experts);
        return data;

    }

}
