package com.bjtu.questionPlatform.service;

import com.bjtu.questionPlatform.entity.Score;
import com.bjtu.questionPlatform.entity.TotalScore;

public interface ScoreService {
    void createScore(Score score);

    void createTotalScore(Score score);

    String getTotalScore(TotalScore score);
}
