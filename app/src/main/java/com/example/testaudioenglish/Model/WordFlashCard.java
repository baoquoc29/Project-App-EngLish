package com.example.testaudioenglish.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WordFlashCard {
    private String engVer;
    private String vietVer;
    public WordFlashCard(String engVer, String vietVer){
        this.engVer = engVer;
        this.vietVer = vietVer;
    }

    public String getEngVer() {
        return engVer;
    }

    public void setEngVer(String engVer) {
        this.engVer = engVer;
    }

    public String getVietVer() {
        return vietVer;
    }

    public void setVietVer(String vietVer) {
        this.vietVer = vietVer;
    }
}
