package com.example.testaudioenglish.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testaudioenglish.Adapter.SheetsAdapter;
import com.example.testaudioenglish.Custom.CustomPasswordTransformationMethod;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.DictationQuestionsModel;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.DictationQuestionsRespone;
import com.example.testaudioenglish.databinding.ActivityDictationQuestionBinding;
import com.example.testaudioenglish.databinding.ActivityDictationTopicBinding;
import com.example.testaudioenglish.databinding.FragmentPictureListeningBinding;
import com.example.testaudioenglish.viewmodel.DictationViewModel;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictationQuestionActivity extends AppCompatActivity {

    private float playbackSpeed = 1.0f;
    private MediaPlayer mediaPlayer;
    private ImageView buttonBack,buttonForward,buttonPause,buttonPlay;
    private TextView currentTime, remainingTime,speedTime;
    private SeekBar seekBar;
    private DictationViewModel dictationViewModel;
    private List<DictationQuestionsModel> list = new ArrayList<>();
    private long idTopic;
    private int currentIndex = 0;
    private TextView questionIndex;
    private TextView conversationTitle;
    private TextView textInputField;
    private TextInputEditText correctAnswer;
    private TextInputLayout ground1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityDictationQuestionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dictation_question);
        dictationViewModel = new ViewModelProvider(this).get(DictationViewModel.class);
        binding.setViewModel(dictationViewModel);
        binding.setLifecycleOwner(this);
        Intent intent = getIntent();
        mapping(binding);
        idTopic = intent.getLongExtra("idTopic",-1);
        setupToolbar();
        loadData();
        clickToNextQuestion();
        clickToBackQuestion();
        clickToCheckQuestion();
        clickToSkipQuestion();
    }
    public void clickToCheckQuestion(){
        dictationViewModel.getNavigationToCheck().observe(this,clicked->{
            if(clicked){
                ground1.setVisibility(View.VISIBLE);
                correctAnswer.setText(list.get(currentIndex).getPronunciation());

                if(list.get(currentIndex).getPronunciation().replaceAll("[?!.,]", "").equalsIgnoreCase(textInputField.getText().toString().replaceAll("[?.!,]",""))){
                    Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                    setEndIcon();
                }
                else{
                    Toast.makeText(this, "Incorrect, Try a gain", Toast.LENGTH_SHORT).show();
                    correctAnswer.setText(list.get(currentIndex).getPronunciation());
                    correctAnswer.setTransformationMethod(new CustomPasswordTransformationMethod(textInputField.getText().toString()));
                }
            }
        });
    }
    public void clickToSkipQuestion(){
        dictationViewModel.getNavigationToSkip().observe(this,clicked->{
            if(clicked){
                ground1.setVisibility(View.VISIBLE);
                correctAnswer.setText(list.get(currentIndex).getPronunciation());
                correctAnswer.setTransformationMethod(new CustomPasswordTransformationMethod(list.get(currentIndex).getPronunciation()));
                setEndIcon();
            }
        });
    }
    public void setEndIcon(){
        try {
            // Lấy đối tượng endIcon từ TextInputLayout
            View endIconView = ground1.findViewById(com.google.android.material.R.id.text_input_end_icon);
            if (endIconView != null) {
                endIconView.performClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void clickToNextQuestion(){
        dictationViewModel.getNavigationToForward().observe(this, clicked -> {
            if (clicked) {
                // Kiểm tra để không vượt quá số lượng câu hỏi
                if (currentIndex < list.size() - 1) {
                    currentIndex++;
                    ground1.setVisibility(View.INVISIBLE);
                    showQuestionDictation();
                    setEndIcon();
                } else {
                    Toast.makeText(this, "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void clickToBackQuestion(){
        dictationViewModel.getNavigationToBack().observe(this, clicked -> {
            if (clicked) {
                // Kiểm tra để không vượt quá số lượng câu hỏi
                if (currentIndex < list.size() - 1) {
                    currentIndex--;
                    ground1.setVisibility(View.INVISIBLE);
                    showQuestionDictation();
                    setEndIcon();
                } else {
                    Toast.makeText(this, "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void mapping(ActivityDictationQuestionBinding binding){
        buttonBack = binding.backButton;
        buttonForward = binding.forwardButton;
        buttonPause = binding.pauseButton;
        buttonPlay = binding.playButton;
        questionIndex = binding.questionIndex;
        currentTime = binding.currentTime;
        remainingTime = binding.remainingTime;
        speedTime = binding.speed;
        correctAnswer = binding.correctAnswer;
        seekBar = binding.sourceListening;
        conversationTitle = binding.conversationTitle;
        textInputField = binding.textInputField;
        ground1 = binding.ground1;
        seekBar.setEnabled(false);
        correctAnswer.setEnabled(false);
        setVisibleBackButton();
    }
    public void setTitle(String text){
        conversationTitle.setText("Conversation: " + text);
    }
    public void setVisibleBackButton(){
        if(currentIndex == 0){
            buttonBack.setVisibility(View.INVISIBLE);
        }
        else if(currentIndex == list.size() -1){
            buttonForward.setVisibility(View.INVISIBLE);
        }
        else{
            buttonBack.setVisibility(View.VISIBLE);
            buttonForward.setVisibility(View.VISIBLE);
        }
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
        }
    }
    private void clickToButtonPlayAudio(){
        dictationViewModel.getNavigationPlayAudio().observe(this, clicked->{
            if(clicked){
                buttonPause.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.GONE);
                mediaPlayer.start();
                PlaybackParams params = new PlaybackParams();
                params.setSpeed(playbackSpeed);
                mediaPlayer.setPlaybackParams(params);
            }
        });
    }
    public void clickToPauseAudio(){
        dictationViewModel.getNavigationPauseAudio().observe(this, clicked->{
            if(clicked){
                buttonPause.setVisibility(View.GONE);
                buttonPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });
    }
    private void loadData(){
        dictationViewModel.getQuestionDictationTopic(idTopic).observe(this, new Observer<DictationQuestionsRespone>() {
            @Override
            public void onChanged(DictationQuestionsRespone dictationQuestionsRespone) {
                if(dictationQuestionsRespone != null){
                    list.clear();
                    list.addAll(dictationQuestionsRespone.getData());
                    setTitle(list.get(0).getNameTopic());
                    showQuestionDictation();
                }
            }
        });
    }
    private void showQuestionDictation(){
        DictationQuestionsModel model = list.get(currentIndex);
        questionIndex.setText((currentIndex + 1) + "/" + list.size());
        playAudio(model.getAudio());
        textInputField.setText("");
        clickToButtonPlayAudio();
        clickToPauseAudio();
        setVisibleBackButton();
        speedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpeedSelectionDialog();
            }
        });
    }
    private void playAudio(String audioUrl) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            } else {
                mediaPlayer.reset();
            }

            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    buttonPlay.setVisibility(View.VISIBLE); // Show the play button when ready
                    buttonPause.setVisibility(View.GONE); // Hide the pause button
                    updateSeekBarAndTime(); // Update the seek bar without starting playback
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    buttonPause.setVisibility(View.GONE);
                    buttonPlay.setVisibility(View.VISIBLE);
                    seekBar.setProgress(0);
                    currentTime.setText("0:00");
                    remainingTime.setText("0:00");
                }
            });

        } catch (IOException e) {
            Log.e("MediaPlayerError", "Error playing audio", e);
        }
    }

    private Handler handler = new Handler();

    private void updateSeekBarAndTime() {
        if (mediaPlayer != null) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
            remainingTime.setText(formatTime(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));

            handler.postDelayed(this::updateSeekBarAndTime, 1000);
        }
    }
    private void changePlaybackSpeed(float speed) {
        if (mediaPlayer != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            playbackSpeed = speed;
            if (mediaPlayer.isPlaying()) {
                PlaybackParams params = mediaPlayer.getPlaybackParams();
                params.setSpeed(playbackSpeed);
                mediaPlayer.setPlaybackParams(params);
            }
            speedTime.setText(String.format("%.1fx", playbackSpeed));
        }
    }

    public void setSpeedOptionListener(int idAudioSpeed, View view, BottomSheetDialog bottomSheetDialog, float speed) {
        View speedOption = view.findViewById(idAudioSpeed);

        if (speedOption != null) {
            speedOption.setOnClickListener(v -> {
                changePlaybackSpeed(speed);
                bottomSheetDialog.dismiss();
            });
        } else {
            Log.e("SpeedOptionError", "Invalid view ID for audio speed option: " + idAudioSpeed);
        }
    }

    private void showSpeedSelectionDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_audio_speed, null);
        bottomSheetDialog.setContentView(dialogView);
        setSpeedOptionListener(R.id.audio_speed_text_08,dialogView,bottomSheetDialog,0.8f);
        setSpeedOptionListener(R.id.audio_speed_text_09,dialogView,bottomSheetDialog,0.9f);
        setSpeedOptionListener(R.id.audio_speed_text_10,dialogView,bottomSheetDialog,1.0f);
        setSpeedOptionListener(R.id.audio_speed_text_11,dialogView,bottomSheetDialog,1.1f);
        setSpeedOptionListener(R.id.audio_speed_text_12,dialogView,bottomSheetDialog,1.2f);
        setSpeedOptionListener(R.id.audio_speed_text_13,dialogView,bottomSheetDialog,1.3f);
        setSpeedOptionListener(R.id.audio_speed_text_14,dialogView,bottomSheetDialog,1.4f);
        setSpeedOptionListener(R.id.audio_speed_text_15,dialogView,bottomSheetDialog,1.5f);
        bottomSheetDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}