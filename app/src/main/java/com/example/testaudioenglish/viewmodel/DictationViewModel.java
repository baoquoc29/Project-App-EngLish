package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Model.DictationQuestionsModel;
import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.DictationQuestionsRespone;
import com.example.testaudioenglish.Response.DictationRespone;

public class DictationViewModel extends AndroidViewModel {
    private ToeicFullTestRepository repository;
    public MutableLiveData<DictationRespone> listDictation;
    public MutableLiveData<DictationQuestionsRespone> listQuestionDictation;
    private MutableLiveData<Boolean> navigationToBack = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationToForward = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> getNavigationToCheck() {
        return navigationToCheck;
    }
    private MutableLiveData<Boolean> navigationToSpeed = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> navigationToCheck = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> navigationToSkip = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationPauseAudio = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationPlayAudio = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getNavigationToBack() {
        return navigationToBack;
    }

    public MutableLiveData<Boolean> getNavigationToForward() {
        return navigationToForward;
    }

    public MutableLiveData<Boolean> getNavigationToSpeed() {
        return navigationToSpeed;
    }

    public MutableLiveData<Boolean> getNavigationToSkip() {
        return navigationToSkip;
    }

    public MutableLiveData<Boolean> getNavigationPauseAudio() {
        return navigationPauseAudio;
    }

    public MutableLiveData<Boolean> getNavigationPlayAudio() {
        return navigationPlayAudio;
    }

    public void clickedToForward(){
        navigationToForward.setValue(true);
    }
    public void clickedToCheck(){
        navigationToCheck.setValue(true);
    }
    public void clickedToBack(){
        navigationToBack.setValue(true);
    }
    public void clickedPlayAudio(){
        navigationPlayAudio.setValue(true);
    }
    public void clickedPauseAudio(){
        navigationPauseAudio.setValue(true);
    }
    public void clickedSpeedAudio(){
        navigationToSpeed.setValue(true);
    }
    public void clickedtoSkip(){
        navigationToSkip.setValue(true);
    }
    public DictationViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
        listDictation = new MutableLiveData<>();
        listQuestionDictation = new MutableLiveData<>();
    }
    private void loadQuestionDictation(Long id) {
        repository.getAllQuestionDictation(id).observeForever(response -> {
            if (response != null) {
                listQuestionDictation.setValue(response);
            } else {
                listQuestionDictation.setValue(null);
            }
        });
    }
    public LiveData<DictationQuestionsRespone> getQuestionDictationTopic(Long id) {
        if (listQuestionDictation.getValue() == null) {
            loadQuestionDictation(id);
        }
        return listQuestionDictation;
    }
    private void loadTopicDictation() {
        repository.getDictationTopic().observeForever(response -> {
            if (response != null) {
                listDictation.setValue(response);
            } else {
                listDictation.setValue(null);
            }
        });
    }
    public LiveData<DictationRespone> getAllDictationTopic() {
        if (listDictation.getValue() == null) {
            loadTopicDictation();
        }
        return listDictation;
    }
}
