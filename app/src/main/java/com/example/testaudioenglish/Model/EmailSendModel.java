package com.example.testaudioenglish.Model;

import lombok.Data;

@Data
public class EmailSendModel {
    private String email;
    public EmailSendModel(String email){
        this.email = email;
    }


}
