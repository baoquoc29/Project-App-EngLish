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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testaudioenglish.Adapter.SheetsAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.databinding.FragmentShortConversationBinding;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionListeningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionListeningFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private float playbackSpeed = 1.0f;

    private RadioButton radioPart2A,radioPart2B,radioPart2C;
    private RadioGroup choicesRadioGroupPart2;
    private MediaPlayer mediaPlayer;
    private ImageView buttonBack,buttonForward,buttonPause,buttonPlay;
    private TextView currentTime, remainingTime,speedTime;
    private int currentIndex = 0;
    private SeekBar seekBar;
    private TextView item;
    private List<ImageListeningModel> list = new ArrayList<>();
    private List<CheckListAnswer> checkAnswer = new ArrayList<>();
    private TextView correctAnswer;
    private TextView questionPart2;
    private SheetsAdapter adapter;
    private String mParam1;
    private String mParam2;
    private Long idTopic;
    private static final String ID_TOPIC  ="IdTopic";
    private ExamAnyFragmentViewModel shortConversationViewModel;
    public QuestionListeningFragment() {
        // Required empty public constructor
    }

    public static QuestionListeningFragment newInstance(String param1, String param2,Long idTopic) {
        QuestionListeningFragment fragment = new QuestionListeningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putLong(ID_TOPIC,idTopic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            idTopic = getArguments().getLong(ID_TOPIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentShortConversationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_short_conversation, container, false);
        shortConversationViewModel = new ViewModelProvider(this).get(ExamAnyFragmentViewModel.class);
        binding.setViewModel(shortConversationViewModel);
        binding.setLifecycleOwner(this);
        mapping(binding);
        loadData();
        clickToDialogSheets();
        clickToNextQuestion();
        clickToBackQuestion();
        setToClose();
        return binding.getRoot();
    }
    public void setToClose(){
        shortConversationViewModel.getNavigationtoClose().observe(getViewLifecycleOwner(),clicked->{
            if(clicked){
                getActivity().onBackPressed();
            }
        });
    }
    public void clickToDialogSheets(){
        shortConversationViewModel.getNavigationToSheets().observe(getViewLifecycleOwner(),clicked->{
            if(clicked){
                showCustomSheetsDialog(list);
            }
        });
    }
    public void setDataForCheckAnswer(){
        for(int i = 0;i<list.size();i++){
            checkAnswer.add(new CheckListAnswer(i,"",false,list.get(i).getAnswerCorrect()));
        }
    }
    public void clickToNextQuestion(){
        shortConversationViewModel.getNavigationToForward().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                // Kiểm tra để không vượt quá số lượng câu hỏi
                if (currentIndex < list.size() - 1) {
                    currentIndex++;
                    buttonBack.setVisibility(View.VISIBLE);
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        unCheckAnswer();
                        correctAnswer.setVisibility(View.GONE);
                        questionPart2.setText("Câu " +(currentIndex + 1) + ":");
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
            choicesRadioGroupPart2.clearCheck();
            if (answer != null) {
                switch (answer) {
                    case "A":
                        radioPart2A.setChecked(true);
                        break;
                    case "B":
                        radioPart2B.setChecked(true);
                        break;
                    case "C":
                        radioPart2C.setChecked(true);
                        break;
                }
            }
        }




    public void clickToBackQuestion() {
        shortConversationViewModel.getNavigationToBack().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                // Kiểm tra để đảm bảo currentIndex không xuống dưới 0
                if (currentIndex > 0) {
                    currentIndex = currentIndex - 1; // Giảm index xuống 1
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        unCheckAnswer();
                        correctAnswer.setVisibility(View.GONE);
                        questionPart2.setText("Câu " +(currentIndex + 1) + ":");
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
            radioPart2A.setText("A");
            radioPart2B.setText("B");
            radioPart2C.setText("C");

    }
    public void checkAnswerClicked(){
            questionPart2.setText("Câu " + (currentIndex + 1) + ": " + list.get(currentIndex).getQuestion());
            radioPart2A.setText("A: " + list.get(currentIndex).getOptionA());
            radioPart2B.setText("B: " + list.get(currentIndex).getOptionB());
            radioPart2C.setText("C: " + list.get(currentIndex).getOptionC());

    }
    public void clickToCheck(ImageListeningModel currentAnswer){
        shortConversationViewModel.getNavigationToCheck().observe(getViewLifecycleOwner(),clicked->{
            if(clicked){
                CheckListAnswer answer = checkAnswer.get(currentIndex);
                answer.setCorrect(true);
                    checkAnswer(currentIndex,choicesRadioGroupPart2);

                    questionPart2.setText("Câu " + (currentIndex + 1) + ":"  + list.get(currentIndex).getQuestion());
                    radioPart2A.setText("A: " + list.get(currentIndex).getOptionA());
                    radioPart2B.setText("B: " + list.get(currentIndex).getOptionB());
                    radioPart2C.setText("C: " + list.get(currentIndex).getOptionC());

                shortConversationViewModel.clickedToUnCheck();
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
                    correctAnswer.setVisibility(View.GONE);
                    questionPart2.setText("Câu " +(currentIndex + 1) + ":");
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

    public void mapping(FragmentShortConversationBinding binding){

        choicesRadioGroupPart2 = binding.choicesRadioGroupPart2;
        radioPart2A = binding.radioPart2A;
        radioPart2B = binding.radioPart2B;
        radioPart2C = binding.radioPart2C;
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
        correctAnswer = binding.correctAnswer;
        questionPart2 = binding.questionPart2;
        if(currentIndex == 0){
            buttonBack.setVisibility(View.GONE);
        }
        else{
            buttonBack.setVisibility(View.VISIBLE);
        }

    }
    public void updateRadioButtonsForNewQuestion(ImageListeningModel currentAnswer) {
            radioPart2A.setText("A: " + currentAnswer.getOptionA());
            radioPart2B.setText("B: " + currentAnswer.getOptionB());
            radioPart2C.setText("C: " + currentAnswer.getOptionC());

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
        shortConversationViewModel.getNavigationPlayAudio().observe(getViewLifecycleOwner(),clicked->{
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
        shortConversationViewModel.getNavigationPauseAudio().observe(getViewLifecycleOwner(),clicked->{
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
                choicesRadioGroupPart2.clearCheck();
            }
            ImageListeningModel currentQuestion = list.get(index);
            playAudio(currentQuestion.getAudio());
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
        shortConversationViewModel.getListeningData(idTopic,"Part 2").observe(getViewLifecycleOwner(), new Observer<ListeningResponse>() {
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
        // Create and configure the BottomSheetDialog
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