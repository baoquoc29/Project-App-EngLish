package com.example.testaudioenglish.Model.ToeicModel;

import lombok.Data;

public class ImageListeningModel {
    private Long idAnswer;
    private Long idContent;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answerCorrect;
    private String question;

    public String getYourAnswer() {
        return yourAnswer;
    }

    private String image;
    private String audio;
    private String partListening;
    private Long idQuiz;
    private String title;

    private String yourAnswer;
    public Long getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Long getIdContent() {
        return idContent;
    }

    public void setIdContent(Long idContent) {
        this.idContent = idContent;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(String answerCorrect) {
        this.answerCorrect = answerCorrect;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPartListening() {
        return partListening;
    }

    public void setPartListening(String partListening) {
        this.partListening = partListening;
    }

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
}
