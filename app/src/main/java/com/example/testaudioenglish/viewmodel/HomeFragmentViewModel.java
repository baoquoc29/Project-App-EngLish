package com.example.testaudioenglish.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeFragmentViewModel extends AndroidViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
    }

}
