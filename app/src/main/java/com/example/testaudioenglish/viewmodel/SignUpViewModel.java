package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.ApiService.AccountService;
import com.example.testaudioenglish.ApiService.RetrofitClient;
import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Navigation.Event;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpViewModel extends AndroidViewModel {

    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordAgain = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> age = new MutableLiveData<>();
    public MutableLiveData<String> numberPhone = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> navigateToLogin = new MutableLiveData<>();
    private AccountService accountService;


    public SignUpViewModel(@NonNull Application application) {
        super(application);
        accountService = RetrofitClient.getClient("http://192.168.0.102:9111").create(AccountService.class);
    }
    public void login(){
        navigateToLogin.setValue(new Event<>(true));
    }

    public void signUp() {
        String usernameValue = username.getValue();
        String passwordValue = password.getValue();
        String passwordAgainValue = passwordAgain.getValue();
        String emailValue = email.getValue();
        String nameValue = name.getValue();
        String ageValue = age.getValue();
        String numberPhoneValue = numberPhone.getValue();

        if (!validateInputs(usernameValue, passwordValue, passwordAgainValue, emailValue, numberPhoneValue)) {
            return;
        }

        AccountCustomer accountCustomer = new AccountCustomer(null, usernameValue, passwordValue, "user", emailValue, nameValue, ageValue, numberPhoneValue);
        isLoading.setValue(true);

        Call<Void> call = accountService.signUp(accountCustomer);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    message.setValue("User created successfully");
                } else {
                    message.setValue("Failed to create user");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.setValue(false);
                message.setValue("Error: " + t.getMessage());
            }
        });
    }

    private boolean validateInputs(String username, String password, String passwordAgain, String email, String numberPhone) {
        if (isNullOrEmpty(username) || isNullOrEmpty(password) || isNullOrEmpty(passwordAgain) || isNullOrEmpty(email) || isNullOrEmpty(numberPhone)) {
            message.setValue("All fields are required");
            return false;
        }

        if (!password.equals(passwordAgain)) {
            message.setValue("Passwords do not match");
            return false;
        }

        if (password.length() < 6) {
            message.setValue("Password must be at least 6 characters long");
            return false;
        }

        if (!isValidEmail(email)) {
            message.setValue("Invalid email format");
            return false;
        }

        if (numberPhone.length() != 10) {
            message.setValue("Phone number must be 10 digits long");
            return false;
        }

        return true;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
