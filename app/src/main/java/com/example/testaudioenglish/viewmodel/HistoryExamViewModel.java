package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.AccountRepository;
import com.example.testaudioenglish.Response.UserScoreResponse;

public class HistoryExamViewModel extends AndroidViewModel {
    private AccountRepository repository;
    private MutableLiveData<UserScoreResponse> listHistory;
    public HistoryExamViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository();
    }
    private void loadHistoryExam(long idCustomer,int page,int size) {
        repository.getHistory(idCustomer,page,size).observeForever(response -> {
            if (response != null) {
                listHistory.setValue(response);
            } else {
                listHistory.setValue(null);
            }
        });
    }

    public LiveData<UserScoreResponse> getHistoryExam(long idCustomer,int page,int size) {
        if (listHistory == null) {
            listHistory = new MutableLiveData<>();
            loadHistoryExam(idCustomer,page,size);
        }
        return listHistory;
    }
}
