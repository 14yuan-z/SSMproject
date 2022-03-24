package com.bjtu.questionPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Judgement {
    String judgementid;
    String judgementcontent;
    String managerid;
    String judgementproportion;
    String judgementname;
    String jClassId;
}

