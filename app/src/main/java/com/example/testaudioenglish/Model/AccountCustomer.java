package com.example.testaudioenglish.Model;

import lombok.Data;

@Data
public class AccountCustomer {
    private Long id;
    private String username;
    private String password;
    private String membership;
    private String email;
    private String name;
    private String age;
    private String numberphone;

    public AccountCustomer(Long id, String username, String password, String membership, String email, String name, String age, String numberphone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.membership = membership;
        this.email = email;
        this.name = name;
        this.age = age;
        this.numberphone = numberphone;
    }
    public AccountCustomer(String username,String password){
        this.username = username;
        this.password = password;
    }


}
