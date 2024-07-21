package com.example.testaudioenglish.Model;

public class MultipleChoiceModel {
    private long id;
    private String englishWord;
    private String vietWord;
    private long idTopic;
    private String answerB;
    private String answerC;
    private String answerD;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getVietWord() {
        return vietWord;
    }

    public void setVietWord(String vietWord) {
        this.vietWord = vietWord;
    }

    public long getIdTopic() {
        return idTopic;
    }

    public MultipleChoiceModel(long id, String englishWord, String vietWord, long idTopic, String answerB, String answerC, String answerD) {
        this.id = id;
        this.englishWord = englishWord;
        this.vietWord = vietWord;
        this.idTopic = idTopic;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
    }

    public void setIdTopic(long idTopic) {
        this.idTopic = idTopic;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }
}
