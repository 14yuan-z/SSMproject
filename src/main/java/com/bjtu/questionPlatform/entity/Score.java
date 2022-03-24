package com.bjtu.questionPlatform.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Score {
    String expertname;
    String judgementid;
    String reportId;
    String ID;  //上传报告的用户id
    String score;
    String suggestion;  // 专家建议
    String totalScore;  // 加权后总分
    String judgeWithScore;  // 前端传来的打分json

}
