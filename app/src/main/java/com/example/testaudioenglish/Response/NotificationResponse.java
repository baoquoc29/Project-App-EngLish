package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.NotificationModel;

import java.util.List;

public class NotificationResponse {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationModel> getData() {
        return data;
    }

    public void setData(List<NotificationModel> data) {
        this.data = data;
    }

    private List<NotificationModel> data;
}
