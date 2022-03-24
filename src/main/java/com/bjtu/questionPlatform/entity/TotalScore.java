package com.bjtu.questionPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TotalScore {
    String reportid;
    String expertname;
    String suggestion;
    String totalscore;

}
