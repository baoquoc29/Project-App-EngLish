package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.ToeicModel.MultichoiceToeicModel;

import java.util.List;

public class MultipleChoiceRespone {
    private boolean status;
    private String message;
    private List<MultichoiceToeicModel> data;

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

    public List<MultichoiceToeicModel> getData() {
        return data;
    }

    public void setData(List<MultichoiceToeicModel> data) {
        this.data = data;
    }
}
