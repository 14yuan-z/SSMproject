package com.bjtu.questionPlatform.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @program: framework
 * @description: user entity
 * @author: ChenDiDi
 * @create: 2021-04-26 18:46
 * @version: 1.0
 **/

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Report implements Serializable {
    private String reportId;
    private String reportName;
    private String ID;
    private String keyWord;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp reportTime;
    private String username;
    private MultipartFile file;
    private String reportPath;
    private String jClassId;
    private String reportStatus;
}
