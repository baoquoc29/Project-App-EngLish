package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;
import com.example.testaudioenglish.Response.ReadingResponse;

public class ExamAnyFragmentViewModel extends AndroidViewModel {
    private ToeicFullTestRepository repository;
    private MutableLiveData<ListeningResponse> listeningData;
    private MutableLiveData<MultipleChoiceRespone> listPart5;
    private MutableLiveData<Boolean> navigationToSheets = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationToBack = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationToForward = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> navigationPauseAudio = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> navigationPlayAudio = new MutableLiveData<>(false);
    private MutableLiveData<ReadingResponse> readingPartSixData;
    public LiveData<ReadingResponse> getReadingDataPart6(long id_quiz,String part) {
        if (readingPartSixData == null) {
            readingPartSixData = new MutableLiveData<>();
            loadListeningAllDataPart6(id_quiz,part);
        }
        return readingPartSixData;
    }
    private void loadListeningAllDataPart6(long id_quiz,String part) {
        repository.getAllDataPart6(id_quiz,part).observeForever(response -> {
            if (response != null) {
                readingPartSixData.setValue(response);
            } else {
                readingPartSixData.setValue(null);
            }
        });
    }
    public MutableLiveData<Boolean> getNavigationtoClose() {
        return navigationtoClose;
    }

    public void setNavigationtoClose(MutableLiveData<Boolean> navigationtoClose) {
        this.navigationtoClose = navigationtoClose;
    }

    private MutableLiveData<Boolean> navigationtoClose = new MutableLiveData<>(false);

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

    public ExamAnyFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
    }

    public MutableLiveData<Boolean> getNavigationToSheets() {
        return navigationToSheets;
    }

    public void setNavigationToSheets(MutableLiveData<Boolean> navigationToSheets) {
        this.navigationToSheets = navigationToSheets;
    }
    public void clickToClose(){
        navigationtoClose.setValue(true);
    }
    public LiveData<ListeningResponse> getListeningData(long id, String part) {
        if (listeningData == null) {
            listeningData = new MutableLiveData<>();
            loadListeningData(id, part);
        }
        return listeningData;
    }
    public LiveData<MultipleChoiceRespone> getListeningAllDataPart5(long id_quiz) {
        if (listPart5 == null) {
            listPart5 = new MutableLiveData<>();
            loadListeningAllDataPart5(id_quiz);
        }
        return listPart5;
    }
    private void loadListeningAllDataPart5(long id_quiz) {
        repository.getAllDataPart5(id_quiz).observeForever(response -> {
            if (response != null) {
                listPart5.setValue(response);
            } else {
                listPart5.setValue(null);
            }
        });
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
