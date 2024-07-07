package com.example.testaudioenglish.ApiService;

import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Model.EmailSendModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {
    @POST("/dev/api/v1/auth/signup")
    Call<Void> signUp(@Body AccountCustomer accountCustomer);
    @POST("dev/api/v1/auth/login")
    Call<Void> login(@Body AccountCustomer accountCustomer);
    @POST("dev/api/v1/auth/forgot")
    Call<Void> forgot(@Body EmailSendModel emailSendModel);
}
