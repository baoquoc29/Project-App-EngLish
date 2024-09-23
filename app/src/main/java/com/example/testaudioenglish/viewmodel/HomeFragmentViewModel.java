package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Navigation.Event;
import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.DictationRespone;
import com.example.testaudioenglish.Response.TopicResponse;

public class HomeFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<String> username;
    private MutableLiveData<TopicResponse> list;
    private ToeicFullTestRepository repository;
    public MutableLiveData<Event<Boolean>> navigateToTopic = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> navigateToAnyPart = new MutableLiveData<>();
    public MutableLiveData<Boolean> navigateToDictationTopic = new MutableLiveData<>();
    public MutableLiveData<DictationRespone> listDictation;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
        username = new MutableLiveData<>();
        list = new MutableLiveData<>();
        listDictation = new MutableLiveData<>();

    }

    private void loadTopicTest() {
        repository.getAllTopics().observeForever(response -> {
            if (response != null) {
                list.setValue(response);
            } else {
                list.setValue(null);
            }
        });
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
    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public MutableLiveData<Boolean> getNavigateToDictationTopic() {
        return navigateToDictationTopic;
    }

    public void setNavigateToDictationTopic(MutableLiveData<Boolean> navigateToDictationTopic) {
        this.navigateToDictationTopic = navigateToDictationTopic;
    }

    public LiveData<TopicResponse> getAllDataTopic() {
        if (list.getValue() == null) {
            loadTopicTest();
        }
        return list;
    }
    public void navigateToDictationTopic(){
        navigateToDictationTopic.setValue(true);
    }
    public void navigateToTopic(){
        navigateToTopic.setValue(new Event<>(true));
    }
    public void navigateToTopicAnyPart(){
        navigateToTopic.setValue(new Event<>(true));
    }
}
