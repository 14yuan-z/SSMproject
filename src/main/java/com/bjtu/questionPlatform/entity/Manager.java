package com.bjtu.questionPlatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    private String managerId;
    private String password;
    private String mail;
    private String phone;

}
