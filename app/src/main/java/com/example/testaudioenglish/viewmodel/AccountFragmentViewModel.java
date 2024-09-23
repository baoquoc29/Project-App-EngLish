package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.AccountRepository;
import com.example.testaudioenglish.Response.IntegerResponse;
import com.example.testaudioenglish.Response.UserScoreResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccountFragmentViewModel extends AndroidViewModel {
    private AccountRepository repository;
    private MutableLiveData<UserScoreResponse> listHistory;
    private MutableLiveData<IntegerResponse> getMaxPointsByIdCustomer;
    private MutableLiveData<IntegerResponse> getTotalExamByIdCustomer;
    private MutableLiveData<String> textTotalExam = new MutableLiveData<>();
    private MutableLiveData<String> textMaxPoints = new MutableLiveData<>();
    private MutableLiveData<String> dateNow = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<Boolean> clickToHistory = new MutableLiveData<>();
    private MutableLiveData<Boolean> clickToPayment = new MutableLiveData<>();
    private MutableLiveData<Boolean> clickToChangeInfor = new MutableLiveData<>();
    private MutableLiveData<Boolean> clickToLogOut = new MutableLiveData<>();
    public AccountFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository();
    }

    public MutableLiveData<Boolean> getClickToHistory() {
        return clickToHistory;
    }

    public void setClickToHistory(MutableLiveData<Boolean> clickToHistory) {
        this.clickToHistory = clickToHistory;
    }

    public MutableLiveData<Boolean> getClickToPayment() {
        return clickToPayment;
    }

    public void setClickToPayment(MutableLiveData<Boolean> clickToPayment) {
        this.clickToPayment = clickToPayment;
    }

    public MutableLiveData<Boolean> getClickToChangeInfor() {
        return clickToChangeInfor;
    }

    public void setClickToChangeInfor(MutableLiveData<Boolean> clickToChangeInfor) {
        this.clickToChangeInfor = clickToChangeInfor;
    }

    public MutableLiveData<Boolean> getClickToLogOut() {
        return clickToLogOut;
    }

    public void setClickToLogOut(MutableLiveData<Boolean> clickToLogOut) {
        this.clickToLogOut = clickToLogOut;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getTextTotalExam() {
        return textTotalExam;
    }

    public MutableLiveData<String> getDateNow() {
        return dateNow;
    }

    public void setDateNow(MutableLiveData<String> dateNow) {
        this.dateNow = dateNow;
    }

    public LiveData<String> getTextMaxPoints() {
        return textMaxPoints;
    }
    public void setDateNow(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDate.now().format(formatter);

        dateNow.setValue("Ngày hoạt động gần nhất : " + formattedDate);
    }
    public void setUsername(String text){
        username.setValue(text);
    }
    public void getMaxPoints(Long idCustomer) {
        repository.getMaxPointsByIdCustomer(idCustomer).observeForever(integerResponse -> {
            if (integerResponse != null && integerResponse.getData() != null) {
                textMaxPoints.setValue(integerResponse.getData().toString());
            }
        });
    }
    public void clickToChangeInfor(){
        clickToChangeInfor.setValue(true);
    }

    public void getTotalExam(Long idCustomer) {
        repository.getTotalExamByIdCustomer(idCustomer).observeForever(integerResponse -> {
            if (integerResponse != null && integerResponse.getData() != null) {
                textTotalExam.setValue(integerResponse.getData().toString());
            }
        });
    }

    public void clickToHistoryExam(){
        clickToHistory.setValue(true);
    }
    public void clickToLogout(){
        clickToLogOut.setValue(true);
    }
}
