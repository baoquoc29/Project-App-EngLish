package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Repository.FlashCardRepository;

import java.util.ArrayList;
import java.util.List;

public class MemoryCardViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private LiveData<List<FlashCardEntity>> data;
    private MutableLiveData<Boolean> clickToClose = new MutableLiveData<>();
    private MutableLiveData<Boolean> clickToReset = new MutableLiveData<>();
    private MutableLiveData<Boolean> clickToAgain = new MutableLiveData<>();
    private MutableLiveData<Integer> countCheck = new MutableLiveData<>();
    public MemoryCardViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        data = new MutableLiveData<>(new ArrayList<>());
    }
    public LiveData<Integer> count(long id){
        return repository.countCheck(id);
    }
    public LiveData<Integer> countZero(long id){
        return repository.countCheckZero(id);
    }
    public MutableLiveData<Integer> getCountCheck() {
        return countCheck;
    }

    public void setCountCheck(MutableLiveData<Integer> countCheck) {
        this.countCheck = countCheck;
    }

    public MutableLiveData<Boolean> getClickToAgain() {
        return clickToAgain;
    }

    public void setClickToAgain(MutableLiveData<Boolean> clickToAgain) {
        this.clickToAgain = clickToAgain;
    }
    public void updateTickedStatus(long id, long idWord, boolean isTicked) {
        if (isTicked) {
            repository.updateTickerClickedFull(id, idWord);
        } else {
            repository.updateTickerClickedEmpty(id, idWord);
        }
    }

    public MutableLiveData<Boolean> getClickToReset() {
        return clickToReset;
    }

    public void setClickToReset(MutableLiveData<Boolean> clickToReset) {
        this.clickToReset = clickToReset;
    }
    public void clickToAgain(){
        clickToAgain.setValue(true);
    }
    public void clickToClose(){
        clickToClose.setValue(true);
    }
    public MutableLiveData<Boolean> getClickToClose() {
        return clickToClose;
    }

    public void setClickToClose(MutableLiveData<Boolean> clickToClose) {
        this.clickToClose = clickToClose;
    }
    public void updateCheck(long idTopic,long idWord){
        repository.updateCheck(idTopic,idWord);
    }

    public void onClickToReset(){
        clickToReset.setValue(true);
    }
    public void resetCheck(long id){
        repository.updateResetCheck(id);
    }
    public LiveData<List<FlashCardEntity>> getListByTopic(long id){
        return  repository.getAllFlashCardByTopic(id);
    }
    public LiveData<List<FlashCardEntity>> getListAgainByTopic(long id){
        return  repository.getAllFlashCardByTopicAndCheck(id);
    }

}
