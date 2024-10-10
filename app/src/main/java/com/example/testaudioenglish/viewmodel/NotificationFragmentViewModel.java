package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.NotificationResponse;

public class NotificationFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<NotificationResponse> data;
    private ToeicFullTestRepository repository;

    public NotificationFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
    }

    public MutableLiveData<NotificationResponse> getNotification(long id, String type) {
        if (data == null) {
            data = new MutableLiveData<>();
            loadNotification(id, type);
        }
        return data;
    }

    private void loadNotification(long id, String type) {
        repository.getNotificationsById(id, type).observeForever(response -> {
            if (response != null) {
                data.setValue(response);
            } else {
                data.setValue(null);
            }
        });
    }
}
