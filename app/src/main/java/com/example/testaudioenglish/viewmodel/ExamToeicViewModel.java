package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;
import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;

import java.util.Collections;
import java.util.List;

public class ExamToeicViewModel extends AndroidViewModel {
        private ToeicFullTestRepository repository;
        private MutableLiveData<Integer> selectedItemId = new MutableLiveData<>();

        public LiveData<Integer> getSelectedItemId() {
            return selectedItemId;
        }
        private MutableLiveData<List<MultipleChoiceRespone>> multipleChoiceData = new MutableLiveData<>();
    private MutableLiveData<List<ReadingToeicModel>> readingPartSixData = new MutableLiveData<>();
        public void selectItem(int itemId) {
            selectedItemId.setValue(itemId);
        }
        public ExamToeicViewModel(@NonNull Application application) {
            super(application);
            repository = new ToeicFullTestRepository();
        }

    public MutableLiveData<List<MultipleChoiceRespone>> getListeningAllDataPart5(long id_quiz) {
        if (multipleChoiceData == null) {
            multipleChoiceData = new MutableLiveData<>();
            loadListeningAllDataPart5(id_quiz);
        }
        return multipleChoiceData;
    }
    public MutableLiveData<List<ReadingToeicModel>> getListeningAllDataPart6(long id_quiz, String part) {
        if (readingPartSixData == null) {
            readingPartSixData = new MutableLiveData<>();
            loadListeningAllDataPart6(id_quiz,part);
        }
        return readingPartSixData;
    }

    private void loadListeningAllDataPart5(long id_quiz) {
        repository.getAllDataPart5(id_quiz).observeForever(response -> {
            if (response != null) {
                multipleChoiceData.setValue(Collections.singletonList(response));
            } else {
                multipleChoiceData.setValue(null);
            }
        });
    }
    private void loadListeningAllDataPart6(long id_quiz,String part) {
        repository.getAllDataPart6(id_quiz,part).observeForever(response -> {
            if (response != null) {
                readingPartSixData.setValue(response.getData());
            } else {
                readingPartSixData.setValue(null);
            }
        });
    }
    public void setUpToCompeleteExam(UserScoreModel userScoreModel){
           repository.completeExam(userScoreModel);
    }

    }

