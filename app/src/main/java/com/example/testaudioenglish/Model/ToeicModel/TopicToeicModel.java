package com.example.testaudioenglish.Model.ToeicModel;

import java.util.Date;

public class TopicToeicModel {
    private Long idQuiz;
    private String title;
    private String description;
    private String difficulty;
    private int totalComplete;

    public Long getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getTotalComplete() {
        return totalComplete;
    }

    public void setTotalComplete(int totalComplete) {
        this.totalComplete = totalComplete;
    }

    public TopicToeicModel(Long idQuiz, String title, String description, String difficulty, int totalComplete) {
        this.idQuiz = idQuiz;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.totalComplete = totalComplete;
    }
}
