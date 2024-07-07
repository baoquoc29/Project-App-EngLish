package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> selectedItemId = new MutableLiveData<>();

    public LiveData<Integer> getSelectedItemId() {
        return selectedItemId;
    }

    public void selectItem(int itemId) {
        selectedItemId.setValue(itemId);
    }
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

}
