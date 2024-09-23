package com.example.testaudioenglish.Model.ToeicModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserScoreModel {
    private Long idScore;
    private Long idCustomer;
    private Long idQuiz;
    private int score;
    private String timeFinish;
    private String dateFinish;
    private Long totalListening;
    private Long totalReading;
    private String username;
    private String title;

    public UserScoreModel() {
        // Default constructor for JSON deserialization
    }

    public UserScoreModel(Long idScore, Long idCustomer, String username, Long idQuiz, String title, int score, String timeFinish, String dateFinish, Long totalListening, Long totalReading) {
        this.idScore = idScore;
        this.idCustomer = idCustomer;
        this.idQuiz = idQuiz;
        this.score = score;
        this.timeFinish = timeFinish;
        this.dateFinish = dateFinish;
        this.totalListening = totalListening;
        this.totalReading = totalReading;
        this.username = username;
        this.title = title;
    }

    public Long getIdScore() {
        return idScore;
    }

    public void setIdScore(Long idScore) {
        this.idScore = idScore;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Long getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public Long getTotalListening() {
        return totalListening;
    }

    public void setTotalListening(Long totalListening) {
        this.totalListening = totalListening;
    }

    public Long getTotalReading() {
        return totalReading;
    }

    public void setTotalReading(Long totalReading) {
        this.totalReading = totalReading;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserScoreModel(Long idCustomer, Long idQuiz, int score, String timeFinish, String dateFinish, Long totalListening, Long totalReading) {
        this.idCustomer = idCustomer;
        this.idQuiz = idQuiz;
        this.score = score;
        this.timeFinish = timeFinish;
        this.dateFinish = dateFinish;
        this.totalListening = totalListening;
        this.totalReading = totalReading;
    }

    // Getters and Setters
    // ...


}
