package com.example.testaudioenglish.Model;

public class DictationQuestionsModel {
    private Long id;
    private Long id_topic;
    private String audio;
    private String pronunciation;
    private String nameTopic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_topic() {
        return id_topic;
    }

    public void setId_topic(Long id_topic) {
        this.id_topic = id_topic;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }
}
