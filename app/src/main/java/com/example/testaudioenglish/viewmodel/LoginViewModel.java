package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.ApiService.AccountService;
import com.example.testaudioenglish.ApiService.RetrofitClient;
import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Model.EmailSendModel;
import com.example.testaudioenglish.Navigation.Event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isLoadingForgot = new MutableLiveData<>(false);
    private AccountService accountService;
    public MutableLiveData<Event<Boolean>> navigateToSignUp = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> navigateToLogin = new MutableLiveData<>();
    public LoginViewModel(@NonNull Application application) {
        super(application);
        accountService = RetrofitClient.getClient("http://192.168.0.102:9111").create(AccountService.class);
    }
    public void fogotPass(){
        isLoadingForgot.setValue(true);

    }
    public void servicePass(EmailSendModel email){
        Call<Void> forgotPassService = accountService.forgot(email);
        forgotPassService.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    isLoadingForgot.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoadingForgot.setValue(false);
            }
        });
    }
    public void signUp(){
        navigateToSignUp.setValue(new Event<>(true));
    }
    public void login(){
    String usernameValue = username.getValue();
    String passwordValue = password.getValue();
    if(!validCheckInput(usernameValue,passwordValue)){
        return;
    }
        AccountCustomer accountCustomer = new AccountCustomer(usernameValue,passwordValue);
        isLoading.setValue(true);
        Call<Void> service = accountService.login(accountCustomer);
        service.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.setValue(false);
                if(response.isSuccessful()){
                    message.setValue("Đăng nhập thành công");
                    navigateToLogin.setValue(new Event<>(true));
                }
                else {
                    message.setValue("Thông tin tài khoản không chính xác");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.setValue(false);
                message.setValue("Error: " + t.getMessage());
            }
        });
    }
    private boolean validCheckInput(String username,String password){
        if(isNullOrEmpty(username) || isNullOrEmpty(password)){
            message.setValue("Không được để trống");
            return false;
        }
        return true;
    }
    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
