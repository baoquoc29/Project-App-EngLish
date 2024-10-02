package com.example.testaudioenglish.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.ApiService.AccountService;
import com.example.testaudioenglish.ApiService.RetrofitClient;
import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Response.CustomerResponse;
import com.example.testaudioenglish.Response.IntegerResponse;
import com.example.testaudioenglish.Response.UserResponse;
import com.example.testaudioenglish.Response.UserScoreResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private AccountService accountService;

    public AccountRepository() {
        accountService = RetrofitClient.getClient().create(AccountService.class);
    }

    public LiveData<UserResponse> login(AccountCustomer accountCustomer) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        accountService.login(accountCustomer).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserScoreResponse> getHistory(Long idCustomer, int page, int size) {
        MutableLiveData<UserScoreResponse> data = new MutableLiveData<>();

        accountService.getHistoryExamByIdCustomer(idCustomer, page, size).enqueue(new Callback<UserScoreResponse>() {
            @Override
            public void onResponse(Call<UserScoreResponse> call, Response<UserScoreResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("API Response", "Data: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserScoreResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage(), t); // Thêm log chi tiết lỗi
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<IntegerResponse> getTotalExamByIdCustomer(Long idCustomer){
        MutableLiveData<IntegerResponse> data = new MutableLiveData<>();
        accountService.getTotalExamByIdCustomer(idCustomer).enqueue(new Callback<IntegerResponse>() {
            @Override
            public void onResponse(Call<IntegerResponse> call, Response<IntegerResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<IntegerResponse> call, Throwable t) {

            }
        });
        return data;
    }
    public LiveData<IntegerResponse> getMaxPointsByIdCustomer(Long idCustomer){
        MutableLiveData<IntegerResponse> data = new MutableLiveData<>();
        accountService.getMaxPointByIdCustomer(idCustomer).enqueue(new Callback<IntegerResponse>() {
            @Override
            public void onResponse(Call<IntegerResponse> call, Response<IntegerResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<IntegerResponse> call, Throwable t) {

            }
        });
        return data;
    }
    public LiveData<CustomerResponse> updateCustomerByIdCustomer(Long idCustomer,AccountCustomer accountCustomer){
        MutableLiveData<CustomerResponse> data = new MutableLiveData<>();
        accountService.updateCustomer(idCustomer,accountCustomer).enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
                else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {

            }
        });
        return data;
    }
    public LiveData<CustomerResponse> getCustomerByIdCustomer(Long idCustomer){
        MutableLiveData<CustomerResponse> data = new MutableLiveData<>();
        accountService.getInformationOfCustomer(idCustomer).enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {

            }
        });
        return data;
    }
    private void update_day_online(String name){
        accountService.update_total_day_online(name);
    }
}
