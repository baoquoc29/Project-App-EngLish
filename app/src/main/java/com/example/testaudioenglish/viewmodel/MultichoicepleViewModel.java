package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Repository.FlashCardRepository;

import java.util.List;

public class MultichoicepleViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private MutableLiveData<List<FlashCardEntity>> list = new MutableLiveData<>();
    public MultichoicepleViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
    }
    public LiveData<List<FlashCardEntity>> getList(long idTopic){
        return repository.getAllFlashCardByTopic(idTopic);
    }
    public LiveData<List<String>> getListAnswer(String engver){
        return repository.listAnswer(engver);
    }
}
