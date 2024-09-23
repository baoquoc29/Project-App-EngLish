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

public class MultipleChoiceToeicViewModel extends AndroidViewModel {
    private ToeicFullTestRepository repository;
    private MutableLiveData<ListeningResponse> listeningData;
    private MutableLiveData<MultipleChoiceRespone> multipleChoiceData;
    private MutableLiveData<ReadingResponse> readingPartSixData;
    public MultipleChoiceToeicViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
    }

    public LiveData<ListeningResponse> getListeningData(long id, String part) {
        if (listeningData == null) {
            listeningData = new MutableLiveData<>();
            loadListeningData(id, part);
        }
        return listeningData;
    }
    public LiveData<ListeningResponse> getListeningAllData(long id_quiz) {
        if (listeningData == null) {
            listeningData = new MutableLiveData<>();
            loadListeningAllData(id_quiz);
        }
        return listeningData;
    }
    public LiveData<MultipleChoiceRespone> getListeningAllDataPart5(long id_quiz) {
        if (multipleChoiceData == null) {
            multipleChoiceData = new MutableLiveData<>();
            loadListeningAllDataPart5(id_quiz);
        }
        return multipleChoiceData;
    }
    public LiveData<ReadingResponse> getListeningAllDataPart6(long id_quiz,String part) {
        if (readingPartSixData == null) {
            readingPartSixData = new MutableLiveData<>();
            loadListeningAllDataPart6(id_quiz,part);
        }
        return readingPartSixData;
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
    private void loadListeningAllData(long id_quiz) {
        repository.getAllData(id_quiz).observeForever(response -> {
            if (response != null) {
                listeningData.setValue(response);
            } else {
                listeningData.setValue(null);
            }
        });
    }
    private void loadListeningAllDataPart5(long id_quiz) {
        repository.getAllDataPart5(id_quiz).observeForever(response -> {
            if (response != null) {
                multipleChoiceData.setValue(response);
            } else {
                multipleChoiceData.setValue(null);
            }
        });
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
}
