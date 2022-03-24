package com.bjtu.questionPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    String reportID;
    String expertName;
    int totalScore;
    String suggestion;
}

