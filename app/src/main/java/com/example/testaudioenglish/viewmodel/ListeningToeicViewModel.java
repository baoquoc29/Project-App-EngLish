package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.ListeningResponse;

public class ListeningToeicViewModel extends AndroidViewModel {
    private ToeicFullTestRepository repository;
    private MutableLiveData<ListeningResponse> listeningData;

    public ListeningToeicViewModel(@NonNull Application application) {
        super(application);

        repository = new ToeicFullTestRepository();
    }
    private void loadListeningAllData(long id_quiz) {
        repository.getAllData(id_quiz).observeForever(response -> {
            if (response != null) {
                listeningData.setValue(response);
            } else {
                listeningData.setValue(null);
            }
        });
    }
    public LiveData<ListeningResponse> getListeningAllData(long id_quiz) {
        if (listeningData == null) {
            listeningData = new MutableLiveData<>();
            loadListeningAllData(id_quiz);
        }
        return listeningData;
    }
}
