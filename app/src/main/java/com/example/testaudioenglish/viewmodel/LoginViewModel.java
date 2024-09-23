package com.example.testaudioenglish.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.ApiService.AccountService;
import com.example.testaudioenglish.ApiService.RetrofitClient;
import com.example.testaudioenglish.Model.AccountCustomer;
import com.example.testaudioenglish.Model.EmailSendModel;
import com.example.testaudioenglish.Navigation.Event;
import com.example.testaudioenglish.Response.IntegerResponse;
import com.example.testaudioenglish.Response.StringResponse;
import com.example.testaudioenglish.Response.UserResponse;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isLoadingForgot = new MutableLiveData<>(false);
    private AccountService accountService;
    public MutableLiveData<Event<Boolean>> navigateToSignUp = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> navigateToLogin = new MutableLiveData<>();
    public MutableLiveData<Integer> total_day_online = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        accountService = RetrofitClient.getClient().create(AccountService.class);
        sharedPreferences = application.getApplicationContext().getSharedPreferences("DateTime", Context.MODE_PRIVATE);
    }

    public void forgotPass() {
        isLoadingForgot.setValue(true);
    }
    public void get_total_day_online(String name, Long idUser, Long idCustomer) {
        accountService.get_total_day_online(name).enqueue(new Callback<IntegerResponse>() {
            @Override
            public void onResponse(Call<IntegerResponse> call, Response<IntegerResponse> response) {
                if (response.isSuccessful()) {
                    int totalDay = response.body().getData();
                    total_day_online.setValue(totalDay);

                    saveToSharedPreferences(idUser, idCustomer, totalDay);
                }
            }

            @Override
            public void onFailure(Call<IntegerResponse> call, Throwable t) {
                // Xử lý khi thất bại
            }
        });
    }
    public void servicePass(EmailSendModel email) {
        Call<Void> forgotPassService = accountService.forgot(email);
        forgotPassService.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    isLoadingForgot.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoadingForgot.setValue(false);
            }
        });
    }
    public boolean checkDayLogin(){
        long currentDate = getStartOfDayInMillis(System.currentTimeMillis());
        long lastUpdateDate = sharedPreferences.getLong("lastUpdateDate", 0);
        return currentDate == lastUpdateDate;
    }
    public void update_day_online(String name) {
        long currentDate = getStartOfDayInMillis(System.currentTimeMillis());
        long lastUpdateDate = sharedPreferences.getLong("lastUpdateDate", 0);

        if (currentDate != lastUpdateDate) {
            accountService.update_total_day_online(name).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật ngày sau khi thành công
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("lastUpdateDate", currentDate);
                        editor.apply();

                        Log.d("UpdateDayOnline", "Ngày đã được cập nhật thành công.");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("UpdateDayOnline", "Cập nhật thất bại", t);
                }
            });
        }
    }


    // Phương thức lấy thời gian bắt đầu của ngày (0:00 AM) để so sánh
    private long getStartOfDayInMillis(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public void signUp() {
        navigateToSignUp.setValue(new Event<>(true));
    }
    public void getCheckDay(String username){
        accountService.get_check_day(username).enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                if(response.isSuccessful()){
                    saveDateSharedPreferences(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {

            }
        });
    }
    public void login() {
        String usernameValue = username.getValue();
        String passwordValue = password.getValue();
        if (!validCheckInput(usernameValue, passwordValue)) {
            return;
        }
        AccountCustomer accountCustomer = new AccountCustomer(usernameValue, passwordValue);
        isLoading.setValue(true);
        Call<UserResponse> service = accountService.login(accountCustomer);
        service.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    update_day_online(usernameValue);
                    if(!checkDayLogin()){
                        String day = get_check_day();
                        updateCheckDayOnline(usernameValue,day);
                    }
                    message.setValue("Đăng nhập thành công");
                    Long idUser = response.body().getData().getId();
                    Long idCustomer = response.body().getData().getIdCustomer();
                    get_total_day_online(usernameValue, idUser, idCustomer);
                    getCheckDay(usernameValue);
                    navigateToLogin.setValue(new Event<>(true));
                } else {
                    message.setValue("Thông tin tài khoản không chính xác");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                isLoading.setValue(false);
                message.setValue("Error: " + t.getMessage());
            }
        });
    }

    private boolean validCheckInput(String username, String password) {
        if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
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

    private void saveToSharedPreferences(Long idUser, Long idCustomer, int totalDay) {
        Context context = getApplication().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("idUser", idUser);
        editor.putLong("idCustomer", idCustomer);
        editor.putInt("totalDay", totalDay);

        // Commit the changes
        editor.apply();
    }
    public void updateCheckDayOnline(String name, String date) {
        // Loại bỏ dấu ngoặc kép trong chuỗi nếu có
        String cleanedDate = date.replace("\"", "");
        Log.d("UpdateCheckDayOnline", "Updating check day. Name: " + name + ", Date: " + cleanedDate);

        accountService.update_check_day(name, cleanedDate).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UpdateCheckDayOnline", "Check day updated successfully");
                    // Handle any additional logic here if needed
                } else {
                    Log.e("UpdateCheckDayOnline", "Update failed. Response code: " + response.code());
                    // Handle error response here if needed
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UpdateCheckDayOnline", "Update failed", t);
                // Handle network or other errors here
            }
        });
    }


    public String get_check_day(){
        Calendar calendar = Calendar.getInstance();

        // Lấy tên của ngày trong tuần
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return getDayName(dayOfWeek);
    }
    private static String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Unknown";
        }
    }
    private void saveDateSharedPreferences(String date) {
        Context context = getApplication().getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("CheckDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("date", date);

        // Commit the changes
        editor.apply();
    }
}
