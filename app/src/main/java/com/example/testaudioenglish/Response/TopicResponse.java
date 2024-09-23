package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.ToeicModel.TopicToeicModel;

import java.util.List;

public class TopicResponse {
    private boolean status;
    private String message;
    private List<TopicToeicModel> data;

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

    public List<TopicToeicModel> getData() {
        return data;
    }

    public void setData(List<TopicToeicModel> data) {
        this.data = data;
    }
}
