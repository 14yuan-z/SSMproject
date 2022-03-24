package com.bjtu.questionPlatform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjtu.questionPlatform.entity.*;

import com.bjtu.questionPlatform.service.JudgementService;
import com.bjtu.questionPlatform.service.ReportService;
import com.bjtu.questionPlatform.service.UserService;
import com.bjtu.questionPlatform.utils.resultUtils.ResponseResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import com.bjtu.questionPlatform.entity.Report;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.IOException;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

import com.bjtu.questionPlatform.mapper.ReportMapper;

/**
 * @program: questionPlatform_back_end
 * @description: file controller
 * @author: CodingLiOOT
 * @create: 2021-04-13 19:33
 * @version: 1.0
 **/
@Configuration
class BaseInterceptor extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/static/");
        super.addResourceHandlers(registry);
    }
}

@Slf4j
@RestController
@RequestMapping(value = "/api/file")
public class FileController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;
    @Autowired
    private JudgementService judgementService;


    private final static String rootPath = System.getProperty("user.dir") + "/src/main/resources/static/";

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/upload")
    public void uploadFile(Report report) {
        MultipartFile file = report.getFile();
        System.out.println("文件上传后端测试");

        File fileDir = new File(rootPath);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }
        try {
            if (file != null) {
//                System.out.println("文件不空");
                String oldName = file.getOriginalFilename();
//                System.out.println(oldName);
                String newName = UUID.randomUUID().toString() + (oldName != null ? oldName.substring(oldName.lastIndexOf(".")) : null);
//                System.out.println(newName);
                file.transferTo(new File(rootPath, newName));

                // 为了获取关键词id
                List<KeyWord> keyWords = reportService.getAllKeyWords();
                List<String> keyscontent = reportService.getallKeysContent();
                List<String> maxketID_ = keyWords.stream().map(KeyWord::getKeysId).collect(Collectors.toList());
                int total = Collections.max(maxketID_.stream().map(Integer::parseInt).collect(Collectors.toList()))+1;
//                Optional<KeyWord> maxkeyID = keyWords.stream().max(Comparator.comparing(KeyWord::getKeysId));
                // 为了获取报告id
                List<Report> reports = reportService.getAllReports();
                List<String> maxID = reports.stream().map(Report::getReportId).collect(Collectors.toList());
                int reportId = Collections.max(maxID.stream().map(Integer::parseInt).collect(Collectors.toList()))+1;
//                System.out.println("关键字ID"+total);
//                System.out.println("报告ID"+reportId);
                String k = report.getKeyWord();
                JSONArray jsonArray = JSON.parseArray(k);

                Report r = new Report();
                User u = userService.selectUserByUserName(report.getUsername());
                r.setReportPath(newName);
                r.setID(u.getID());
                r.setReportId(reportId + "");
                r.setReportName(oldName);
                r.setReportStatus("1");
                reportService.createReport(r);



                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    KeyWord key = new KeyWord();
                    key.setKeysContent(jsonObject.getString("word"));
                    key.setKeysId(total + "");
                    key.setReportId(reportId + "");
                    reportService.createKey(key);
                    total++;
//                    //如果keyword在之前就存在，将keyword的reportID连接到当前报告
//                    if (keyscontent.contains(jsonObject.getString("word")))
//                    {
////                        System.out.println("更新旧的keyword");
//                        reportService.updatekeyword_reportid(r.getReportId(),jsonObject.getString("word"));
//                    }
//                    //此时为新的keyword
//                    else {
////                        System.out.println("创建新的keyword");
//                        key.setKeysContent(jsonObject.getString("word"));
//                        key.setKeysId(total + "");
//                        key.setReportId(reportId + "");
//                        reportService.createKey(key);
//                        total++;
//                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @CacheEvict

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getList")
    public HashMap<String, Object> getList(@RequestBody User user) throws InterruptedException {
        List<HashMap<String, Object>> reports = new ArrayList<>();
        List<HashMap<String, Object>> keyWords = new ArrayList<>();
        User u = userService.selectUserByUserName(user.getUsername());
//        List<Report> reportlist1 = reportMapper.selectReportByUserId(u.getID());
        List<Report> reportlist = reportService.selectReportByUserId(u.getID());
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
            item.put("createTime", reportlist.get(i).getReportTime().toString());
//            System.out.println(reportlist.get(i).getReportTime());
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
            reports.add(item);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("reports", reports);
        data.put("keyWords", keyWords);
        return data;

    }

    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/getReport")
    public HashMap<String, Object> getReport(@RequestBody Report report) {
        List<HashMap<String, Object>> keyWord = new ArrayList<>();
        List<HashMap<String, Object>> grades = new ArrayList<>();
        List<HashMap<String, Object>> judgement = new ArrayList<>();
        List<KeyWord> w = reportService.selectKeyWordByReportId(report.getReportId());
        for (int i = 0; i < w.size(); i++) {
            HashMap<String, Object> word = new HashMap<>();
            //System.out.println(w.get(i).getKeysContent());
            word.put("word", w.get(i).getKeysContent());
            keyWord.add(word);
        }

        List<Grade> g = reportService.selectGradesByReportId(report.getReportId());
        for (int i = 0; i < g.size(); i++) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("expertName", g.get(i).getExpertName());
            item.put("totalScore", g.get(i).getTotalScore());
            item.put("suggestion", g.get(i).getSuggestion());
            grades.add(item);
        }

        List<Score> s = reportService.selectScoreByReportId(report.getReportId());
        for (int i = 0; i < s.size(); i++) {
            List<Judgement> j = reportService.selectJudgementByJudgementId(s.get(i).getJudgementid());
            HashMap<String, Object> item = new HashMap<>();
            item.put("expertName", s.get(i).getExpertname());
            item.put("judgementName", j.get(0).getJudgementname());
            item.put("judgementContent", j.get(0).getJudgementcontent());
            item.put("score", s.get(i).getScore());
            judgement.add(item);

        }

        Report rpt = reportService.selectReportById(report.getReportId());
//        System.out.println("id"+report.getReportId());
//        System.out.println("获取路径地址"+rpt.getReportPath());
        String url = "http://localhost:8090/static/" + rpt.getReportPath();

        HashMap<String, Object> data = new HashMap<>();
        data.put("keyWord", keyWord);
        data.put("grades", grades);
        data.put("judgement", judgement);

        Report r = reportService.selectReportById(report.getReportId());
        data.put("reportStatus", r.getReportStatus());

        data.put("file", url);

        String Status = rpt.getReportStatus();
        data.put("reportStatus", Status);

        return data;
    }

    //    删除报告
    @CrossOrigin
    @ResponseResultBody
    @PostMapping(value = "/deleteReport")
    public void deleteReport(@RequestBody HashMap<String, String> report) {
        System.out.println(report);
        switch (report.get("status")) {
            case "待分配指标类":
//                //首先获取到报告关键字对应的keysid以便于删除专家
//                List<String> keysid = reportService.searchkeysidByReportId(report.get("reportId"));
//                for (String keyid : keysid){
//                    judgementService.deleteExpertBykeysid(keyid);
//                }
                reportService.deleteReport(report.get("reportId"),1);
//                System.out.println("删除后获取的报告数"+reportService.selectReportByUserId("030edf9b-dd73-4247-8ccf-34e2ff590c86").size());
                    break;
            case "待分配专家":
//                System.out.println("待分配专家");
                reportService.deleteReport(report.get("reportId"), 2);
                break;
            case "专家待打分":
//                System.out.println("专家待打分");
                reportService.deleteReport(report.get("reportId"), 3);
                break;
            case "已完成打分":
//                System.out.println("已完成打分");
                reportService.deleteReport(report.get("reportId"), 4);
                break;
        }

    }


}
