package com.example.testaudioenglish.Response;

import com.example.testaudioenglish.Model.AccountCustomer;

public class CustomerResponse {
    private boolean status;
    private String message;
    private AccountCustomer data;

    public AccountCustomer getData() {
        return data;
    }

    public void setData(AccountCustomer data) {
        this.data = data;
    }

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

}
