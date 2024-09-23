package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.ListeningResponse;

public class ExamEachPartViewModel extends AndroidViewModel {
    private ToeicFullTestRepository repository;
    private MutableLiveData<ListeningResponse> listeningData;
    private MutableLiveData<Boolean> navigationToSheets = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationToBack = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationToForward = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> navigationPauseAudio = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationPlayAudio = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getNavigationToCheck() {
        return navigationToCheck;
    }

    public void setNavigationToCheck(MutableLiveData<Boolean> navigationToCheck) {
        this.navigationToCheck = navigationToCheck;
    }

    private MutableLiveData<Boolean> navigationToSpeed = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> navigationToCheck = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> getNavigationPauseAudio() {
        return navigationPauseAudio;
    }

    public void setNavigationPauseAudio(MutableLiveData<Boolean> navigationPauseAudio) {
        this.navigationPauseAudio = navigationPauseAudio;
    }

    public MutableLiveData<Boolean> getNavigationPlayAudio() {
        return navigationPlayAudio;
    }

    public void setNavigationPlayAudio(MutableLiveData<Boolean> navigationPlayAudio) {
        this.navigationPlayAudio = navigationPlayAudio;
    }

    public MutableLiveData<Boolean> getNavigationToSpeed() {
        return navigationToSpeed;
    }

    public void setNavigationToSpeed(MutableLiveData<Boolean> navigationToSpeed) {
        this.navigationToSpeed = navigationToSpeed;
    }

    public MutableLiveData<Boolean> getNavigationToBack() {
        return navigationToBack;
    }

    public void setNavigationToBack(MutableLiveData<Boolean> navigationToBack) {
        this.navigationToBack = navigationToBack;
    }

    public MutableLiveData<Boolean> getNavigationToForward() {
        return navigationToForward;
    }

    public void setNavigationToForward(MutableLiveData<Boolean> navigationToForward) {
        this.navigationToForward = navigationToForward;
    }

    public ExamEachPartViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
    }

    public MutableLiveData<Boolean> getNavigationToSheets() {
        return navigationToSheets;
    }

    public void setNavigationToSheets(MutableLiveData<Boolean> navigationToSheets) {
        this.navigationToSheets = navigationToSheets;
    }

    public LiveData<ListeningResponse> getListeningData(long id, String part) {
        if (listeningData == null) {
            listeningData = new MutableLiveData<>();
            loadListeningData(id, part);
        }
        return listeningData;
    }
    public void clickedToSheets(){
        navigationToSheets.setValue(true);
    }
    public void clickedToForward(){
        navigationToForward.setValue(true);
    }
    public void clickedToCheck(){
        navigationToCheck.setValue(true);
    }
    public void clickedToUnCheck(){
        navigationToCheck.setValue(false);
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
    private void loadListeningData(long id, String part) {
        repository.getListeningData(id, part).observeForever(response -> {
            if (response != null) {
                listeningData.setValue(response);
            } else {
                listeningData.setValue(null);
            }
        });
    }
}
