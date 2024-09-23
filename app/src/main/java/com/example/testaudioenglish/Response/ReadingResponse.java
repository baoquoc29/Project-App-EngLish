package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;

import java.util.List;

public class ReadingResponse {
    private boolean status;
    private String message;
    private List<ReadingToeicModel> data;

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

    public List<ReadingToeicModel> getData() {
        return data;
    }

    public void setData(List<ReadingToeicModel> data) {
        this.data = data;
    }
}
