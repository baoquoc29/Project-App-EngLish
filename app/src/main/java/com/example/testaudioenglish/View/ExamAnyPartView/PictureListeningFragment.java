package com.example.testaudioenglish.View.ExamAnyPartView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Adapter.SheetsAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.databinding.FragmentPictureListeningBinding;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PictureListeningFragment extends Fragment implements OnItemClickListener {
    private ExamAnyFragmentViewModel examAnyFragmentViewModel;
    private float playbackSpeed = 1.0f;
    private RadioButton radioButtonA,radioButtonB,radioButtonC,radioButtonD;

    private RadioButton radioPart2A,radioPart2B,radioPart2C;
    private RadioGroup choicesRadioGroupPart2;
    private MediaPlayer mediaPlayer;
    private ImageView listeningSectionImage;
    private ImageView buttonBack,buttonForward,buttonPause,buttonPlay;
    private Button check;
    private TextView currentTime, remainingTime,speedTime;
    private int currentIndex = 0;
    private SeekBar seekBar;
    private TextView item;
    private List<ImageListeningModel> list = new ArrayList<>();
    private List<CheckListAnswer> checkAnswer = new ArrayList<>();
    private boolean isCorrect = false;
    private RadioGroup radioGroup;
    private TextView correctAnswer;
    private TextView questionPart2;
    private LinearLayout layoutPart1,layoutPart2;
    private SheetsAdapter adapter;
    private String tagPart;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PictureListeningFragment() {
        // Required empty public constructor
    }

    public static PictureListeningFragment newInstance(String param1, String param2) {
        PictureListeningFragment fragment = new PictureListeningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPictureListeningBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_picture_listening, container, false);
        examAnyFragmentViewModel = new ViewModelProvider(this).get(ExamAnyFragmentViewModel.class);
        binding.setViewModel(examAnyFragmentViewModel);
        binding.setLifecycleOwner(this);
        mapping(binding);
        loadData();
        clickToDialogSheets();
        clickToNextQuestion();
        clickToBackQuestion();
        setToClose();
        return binding.getRoot();
    }
    public void clickToDialogSheets(){
        examAnyFragmentViewModel.getNavigationToSheets().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                showCustomSheetsDialog(list);
            }
        });
    }
    public void setToClose(){
        examAnyFragmentViewModel.getNavigationtoClose().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                getActivity().onBackPressed();
            }
        });
    }
    public void setDataForCheckAnswer(){
        for(int i = 0;i<list.size();i++){
            checkAnswer.add(new CheckListAnswer(i,"",false,list.get(i).getAnswerCorrect()));
        }
    }
    public void clickToNextQuestion(){
        examAnyFragmentViewModel.getNavigationToForward().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                // Kiểm tra để không vượt quá số lượng câu hỏi
                if (currentIndex < list.size() - 1) {
                    currentIndex++;
                    buttonBack.setVisibility(View.VISIBLE);
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        unCheckAnswer();
                        correctAnswer.setVisibility(View.GONE);
                    }
                    else{
                        checkAnswerClicked();
                        correctAnswer.setVisibility(View.VISIBLE);
                        setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer());
                        correctAnswer.setText("Đáp án đúng là: " + checkAnswer.get(currentIndex).getCorrectAnswer());
                    }
                    showQuestion(currentIndex);
                } else {
                    Toast.makeText(getContext(), "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setRadioButtonChecked(String answer) {
        if(layoutPart1.getVisibility() == View.VISIBLE){
            radioGroup.clearCheck();

            // Check the appropriate RadioButton based on the answer
            if (answer != null) {
                switch (answer) {
                    case "A":
                        radioButtonA.setChecked(true);
                        break;
                    case "B":
                        radioButtonB.setChecked(true);
                        break;
                    case "C":
                        radioButtonC.setChecked(true);
                        break;
                    case "D":
                        radioButtonD.setChecked(true);
                        break;
                }
            }
        }
    }



    public void clickToBackQuestion() {
        examAnyFragmentViewModel.getNavigationToBack().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                // Kiểm tra để đảm bảo currentIndex không xuống dưới 0
                if (currentIndex > 0) {
                    currentIndex = currentIndex - 1; // Giảm index xuống 1
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        unCheckAnswer();

                        correctAnswer.setVisibility(View.GONE);
                    }
                    else{

                        checkAnswerClicked();
                        setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer());
                        correctAnswer.setVisibility(View.VISIBLE);
                        correctAnswer.setText("Đáp án đúng là: " + checkAnswer.get(currentIndex).getCorrectAnswer());
                    }

                    showQuestion(currentIndex);
                    if (currentIndex == 0) {
                        buttonBack.setVisibility(View.GONE);
                    } else {
                        buttonBack.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "You are at the first question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void unCheckAnswer(){
            radioButtonA.setText("A");
            radioButtonB.setText("B");
            radioButtonC.setText("C");
            radioButtonD.setText("D");
    }
    private void setToRadioText(){
        radioButtonA.setText("A: " + list.get(currentIndex).getOptionA());
        radioButtonB.setText("B: " + list.get(currentIndex).getOptionB());
        radioButtonC.setText("C: " + list.get(currentIndex).getOptionC());
        radioButtonD.setText("D: " +list.get(currentIndex).getOptionD());
    }

    public void checkAnswerClicked(){
            setToRadioText();
    }
    public void clickToCheck(ImageListeningModel currentAnswer){
        examAnyFragmentViewModel.getNavigationToCheck().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                CheckListAnswer answer = checkAnswer.get(currentIndex);
               
                answer.setCorrect(true);
                    checkAnswer(currentIndex,radioGroup);
                    setToRadioText();
                examAnyFragmentViewModel.clickedToUnCheck();
            }
        });
    }
    public void showCustomSheetsDialog(List<ImageListeningModel> questions) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.custom_dialog_sheets, null);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyleviewSheets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        adapter = new SheetsAdapter(checkAnswer, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showQuestion(position);
                currentIndex = position;
                dialog.dismiss();
                item.setText(currentIndex + 1 + " / " + list.size());
                if(!checkAnswer.get(currentIndex).isCorrect()){
                    unCheckAnswer();
                    radioGroup.clearCheck();
                    correctAnswer.setVisibility(View.GONE);
                }
                else{
                    checkAnswerClicked();
                    setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer());
                    correctAnswer.setVisibility(View.VISIBLE);
                    correctAnswer.setText("Đáp án đúng là: " + checkAnswer.get(currentIndex).getCorrectAnswer());
                }
            }
        });
        recyclerView.setAdapter(adapter);


        dialog.show();
    }

    public void mapping(FragmentPictureListeningBinding binding){
        radioButtonA = binding.radioA;
        radioButtonB = binding.radioB;
        radioButtonC = binding.radioC;
        radioButtonD = binding.radioD;
        listeningSectionImage = binding.listeningSectionImage;
        buttonBack = binding.buttonBack;
        buttonForward = binding.buttonForward;
        buttonPause = binding.pauseButton;
        buttonPlay = binding.playButton;
        currentTime = binding.currentTime;
        remainingTime = binding.remainingTime;
        speedTime = binding.speed;
        seekBar = binding.sourceListening;
        seekBar.setEnabled(false);
        item = binding.item;
        radioGroup = binding.choicesRadioGroup;
        correctAnswer = binding.correctAnswer;
        layoutPart1 = binding.layoutPart1;
        if(currentIndex == 0){
            buttonBack.setVisibility(View.GONE);
        }
        else{
            buttonBack.setVisibility(View.VISIBLE);
        }
    }
    public void updateRadioButtonsForNewQuestion(ImageListeningModel currentAnswer) {
            setToRadioText();
    }

    private void checkAnswer(int id,RadioGroup radioGroupCheck) {

        int selectedId = radioGroupCheck.getCheckedRadioButtonId();
        if (selectedId != -1) {
            ImageListeningModel currentQuestion = list.get(id);
            updateRadioButtonsForNewQuestion(currentQuestion);
            RadioButton selectedRadioButton = getActivity().findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            String correctRadioButtonAnswer = currentQuestion.getAnswerCorrect();
            String result = selectedAnswer.split(":")[0];
            checkAnswer.get(currentIndex).setSelectedAnswer(result);
            if (selectedAnswer.startsWith(correctRadioButtonAnswer)) {
                Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), "Incorrect. Try again.", Toast.LENGTH_SHORT).show();
                correctAnswer.setVisibility(View.VISIBLE);
                correctAnswer.setText("Đáp án đúng là: " + checkAnswer.get(currentIndex).getCorrectAnswer());
            }

        } else {
            Toast.makeText(getContext(), "Please select an answer.", Toast.LENGTH_SHORT).show();
        }
    }




    private void clickToButtonPlayAudio(){
        examAnyFragmentViewModel.getNavigationPlayAudio().observe(getViewLifecycleOwner(), clicked->{
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
        examAnyFragmentViewModel.getNavigationPauseAudio().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                buttonPause.setVisibility(View.GONE);
                buttonPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });
    }
    private void showQuestion(int index) {
        if (index >= 0 && index < list.size()) {
            if(checkAnswer.get(currentIndex).getSelectedAnswer() == null){
                radioGroup.clearCheck();
            }
            ImageListeningModel currentQuestion = list.get(index);
            playAudio(currentQuestion.getAudio());
            if(layoutPart1.getVisibility() == View.VISIBLE){
                Glide.with(this)
                        .load(currentQuestion.getImage())
                        .into(listeningSectionImage);
            }
            item.setText(currentIndex + 1 + " / " + list.size());

            clickToButtonPlayAudio();
            clickToPauseAudio();
            clickToCheck(currentQuestion);
            speedTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSpeedSelectionDialog();
                }
            });
        }
    }


    private void loadData() {
        examAnyFragmentViewModel.getListeningData(1,"Part 1").observe(getViewLifecycleOwner(), new Observer<ListeningResponse>() {
            @Override
            public void onChanged(ListeningResponse listeningResponse) {
                if (listeningResponse == null || listeningResponse.getData() == null) {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    return;
                }
                list.clear();
                list.addAll(listeningResponse.getData());

                if (!list.isEmpty()) {
                    setDataForCheckAnswer();
                    showQuestion(currentIndex);
                } else {
                    Log.d("Bug404", "No audio data available.");
                }
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
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

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null); // Stop updating the seek bar
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
    @Override
    public void onItemClick(int position) {

    }
}