package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Model.TopicDictationModel;

import java.util.List;

public class DictationRespone {
    private boolean status;
    private String message;
    private List<TopicDictationModel> data;

    public List<TopicDictationModel> getData() {
        return data;
    }

    public void setData(List<TopicDictationModel> data) {
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
