package com.bjtu.questionPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Expert {
    String expertName;
    String keysId;
    String expertType;
    String expertUnit;
    String expertInformation;
    String mail;
    String phone;
    String code;
}
