package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;

import java.util.List;

public class UserScoreResponse {
    private boolean status;
    private String message;
    private List<UserScoreModel> data;

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

    public List<UserScoreModel> getData() {
        return data;
    }

    public void setData(List<UserScoreModel> data) {
        this.data = data;
    }
}
