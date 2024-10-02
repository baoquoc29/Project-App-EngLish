package com.example.testaudioenglish.Response;

import java.util.List;

public class TranslateRequest {
    private List<String> texts;

    private String tl;

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
        this.tl = tl;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    private String sl;

    public TranslateRequest(List<String> texts, String tl, String sl) {
        this.texts = texts;
        this.tl = tl;
        this.sl = sl;
    }
}
