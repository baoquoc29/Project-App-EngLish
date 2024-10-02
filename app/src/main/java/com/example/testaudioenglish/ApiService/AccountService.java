package com.example.testaudioenglish.ApiService;

import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Model.EmailSendModel;
import com.example.testaudioenglish.Response.CustomerResponse;
import com.example.testaudioenglish.Response.IntegerResponse;
import com.example.testaudioenglish.Response.StringResponse;
import com.example.testaudioenglish.Response.UserResponse;
import com.example.testaudioenglish.Response.UserScoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {

    @POST("/dev/api/v1/auth/accounts/signup")
    Call<Void> signUp(@Body AccountCustomer accountCustomer);

    @POST("/dev/api/v1/auth/accounts/login")
    Call<UserResponse> login(@Body AccountCustomer accountCustomer);

    @POST("/dev/api/v1/auth/accounts/forgot")
    Call<Void> forgot(@Body EmailSendModel emailSendModel);

    @GET("/dev/api/v1/get_max/id/{id_customer}")
    Call<IntegerResponse> getMaxPointByIdCustomer(@Path("id_customer") long idCustomer);

    @GET("/dev/api/v1/get_total/id/{id_customer}")
    Call<IntegerResponse> getTotalExamByIdCustomer(@Path("id_customer") long idCustomer);

    @GET("/dev/api/v1/get_history/id/{id_customer}")
    Call<UserScoreResponse> getHistoryExamByIdCustomer(
            @Path("id_customer") long idCustomer,
            @Query("page") int page,
            @Query("size") int size);

    @PUT("/dev/api/v1/auth/accounts/update/customer/id/{id_customer}")
    Call<CustomerResponse> updateCustomer(@Path("id_customer") long idCustomer ,@Body AccountCustomer accountCustomer);

    @GET("/dev/api/v1/auth/accounts/get_customer/customer/id/{id_customer}")
    Call<CustomerResponse> getInformationOfCustomer(@Path("id_customer") long idCustomer);

    @POST("/dev/api/v1/auth/accounts/update_day_online/account/username/{name}")
    Call<Void> update_total_day_online(@Path("name") String name);

    @GET("/dev/api/v1/auth/accounts/get_total_day_online/account/username/{name}")
    Call<IntegerResponse> get_total_day_online(@Path("name") String name);

    @GET("/dev/api/v1/auth/accounts/get_check_day/account/username/{name}")
    Call<StringResponse> get_check_day(@Path("name") String name);

    @POST("/dev/api/v1/auth/accounts/update_check_day/account/username/{user_name}")
    Call<Void> update_check_day(@Path("user_name") String name, @Body String date);
}
