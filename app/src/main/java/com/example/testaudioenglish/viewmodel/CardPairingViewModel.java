package com.example.testaudioenglish.viewmodel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Repository.FlashCardRepository;

import java.util.ArrayList;
import java.util.List;

public class CardPairingViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private MutableLiveData<Boolean> readyClicked = new MutableLiveData<>(false);
    private LiveData<List<FlashCardEntity>> list_flashCard;
    private MutableLiveData<String> timerText = new MutableLiveData<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int seconds = 0;
    private Runnable runnable;
    private int milliseconds = 0;
    private MutableLiveData<Boolean> visible = new MutableLiveData<>(false);
    public LiveData<Boolean> getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible.setValue(visible);
    }
    public CardPairingViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        list_flashCard = new MutableLiveData<>(new ArrayList<>());
        timerText.setValue("00:00");
    }
    public LiveData<String> getTimerText() {
        return timerText;
    }

    public void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                milliseconds += 100;
                int totalSeconds = milliseconds / 1000;
                int hundredths = (milliseconds % 1000) / 100;

                String time;
                    time = String.format("%02d.%d", totalSeconds, hundredths);
                timerText.postValue(time);
                handler.postDelayed(this, 100);
            }
        };
        handler.post(runnable);
    }



    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }
    public MutableLiveData<Boolean> getReadyClicked() {
        return readyClicked;
    }
    public void  onButtonClick(){
        readyClicked.setValue(true);

    }

    public void setReadyClicked(MutableLiveData<Boolean> readyClicked) {
        this.readyClicked = readyClicked;
    }
    public LiveData<List<FlashCardEntity>> getWordByIdTopic(long id) {
        return repository.getAllFlashCardByTopic(id);
    }
}
