package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;

import java.util.List;


public class ListeningResponse {
    private boolean status;
    private String message;
    private List<ImageListeningModel> data;

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

    public List<ImageListeningModel> getData() {
        return data;
    }

    public void setData(List<ImageListeningModel> data) {
        this.data = data;
    }
}
