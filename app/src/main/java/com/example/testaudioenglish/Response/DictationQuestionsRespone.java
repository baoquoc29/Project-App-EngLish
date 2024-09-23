package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.DictationQuestionsModel;
import com.example.testaudioenglish.Model.TopicDictationModel;

import java.util.List;

public class DictationQuestionsRespone {
    private boolean status;
    private String message;
    private List<DictationQuestionsModel> data;

    public List<DictationQuestionsModel> getData() {
        return data;
    }

    public void setData(List<DictationQuestionsModel> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
