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

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Adapter.SheetsAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.databinding.FragmentShortConversation2Binding;
import com.example.testaudioenglish.databinding.FragmentShortConversationBinding;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShortConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShortConversationFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_PART = "tagPart";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String tagPart;
    private Long idTopic;
    private static final String ID_TOPIC  ="IdTopic";
    public ShortConversationFragment() {
        // Required empty public constructor
    }

    public static ShortConversationFragment newInstance(String param1, String param2,String tagPart,Long idTopic) {
        ShortConversationFragment fragment = new ShortConversationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(TAG_PART,tagPart);
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
            tagPart = getArguments().getString(TAG_PART);
            idTopic = getArguments().getLong(ID_TOPIC);
        }
    }
    private ExamAnyFragmentViewModel shortConversationViewModel;
    private int currentIndex = 0;
    private RadioButton[] radioButtons = new RadioButton[12];
    private List<ImageListeningModel> list = new ArrayList<>();
    private List<CheckListAnswer> checkAnswer = new ArrayList<>();
    private ImageView buttonBack,buttonForward,buttonPause,buttonPlay;
    private TextView currentTime, remainingTime,speedTime;
    private SeekBar seekBar;
    private TextView questionPart2_question1,questionPart2_question3,questionPart2_question2;
    private TextView item;
    private SheetsAdapter adapter;
    private MediaPlayer mediaPlayer;
    private RadioGroup radioGroup1,radioGroup2,radioGroup3;
    private TextView correctAnswer1,correctAnswer2,correctAnswer3;
    private float playbackSpeed = 1.0f;
    private ImageView imageShortConversation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentShortConversation2Binding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_short_conversation2, container, false);
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
    public void clickToDialogSheets(){
        shortConversationViewModel.getNavigationToSheets().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                showCustomSheetsDialog(checkAnswer);
            }
        });
    }
    public void setToClose(){
        shortConversationViewModel.getNavigationtoClose().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                getActivity().onBackPressed();
            }
        });
    }
    private void mapping(FragmentShortConversation2Binding binding){
            radioButtons[0] = binding.radioPart2AQuestion1;
            radioButtons[1] = binding.radioPart2BQuestion1;
            radioButtons[2] = binding.radioPart2CQuestion1;
            radioButtons[3] = binding.radioPart2DQuestion1;
            radioButtons[4] = binding.radioPart2AQuestion2;
            radioButtons[5] = binding.radioPart2BQuestion2;
            radioButtons[6] = binding.radioPart2CQuestion2;
            radioButtons[7] = binding.radioPart2DQuestion2;
            radioButtons[8] = binding.radioPart2AQuestion3;
            radioButtons[9] = binding.radioPart2BQuestion3;
            radioButtons[10] = binding.radioPart2CQuestion3;
            radioButtons[11] = binding.radioPart2DQuestion3;
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
        radioGroup1 = binding.choicesRadioGroupPart2Question1;
        radioGroup2 = binding.choicesRadioGroupPart2Question2;
        radioGroup3 = binding.choicesRadioGroupPart2Question3;
        correctAnswer1 = binding.correctAnswer1;
        correctAnswer2 = binding.correctAnswer2;
        correctAnswer3 = binding.correctAnswer3;
        questionPart2_question1 = binding.questionPart2Question1;
        questionPart2_question2= binding.questionPart2Question2;
        questionPart2_question3 = binding.questionPart2Question3;
        imageShortConversation = binding.imageShortConversation;
    }
    public void setDataForCheckAnswer(){
        for(int i = 0;i<list.size();i++){
            checkAnswer.add(new CheckListAnswer(i,"",false,list.get(i).getAnswerCorrect()));
        }
    }
    private void loadData() {
        shortConversationViewModel.getListeningData(idTopic,tagPart).observe(getViewLifecycleOwner(), new Observer<ListeningResponse>() {
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
    public void clickToNextQuestion(){
        shortConversationViewModel.getNavigationToForward().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                if (currentIndex < list.size() - 1) {
                    currentIndex = currentIndex + 3;
                    buttonBack.setVisibility(View.VISIBLE);
                    setCheckedRadioButton();
                    showQuestion(currentIndex);
                } else {
                    Toast.makeText(getContext(), "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setCheckedRadioButton(){
        if(!checkAnswer.get(currentIndex).isCorrect()){
            setUnVisibleCorrectAnswer();
        }
        else{
            setVisibleCorrectAnswer();
            setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex + 1).getSelectedAnswer(),checkAnswer.get(currentIndex+2).getSelectedAnswer());
        }
    }
    public void clickToBackQuestion(){
        shortConversationViewModel.getNavigationToBack().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                // Kiểm tra để không vượt quá số lượng câu hỏi
                if (currentIndex < list.size() - 1) {
                    currentIndex = currentIndex - 3;
                    buttonBack.setVisibility(View.VISIBLE);
                    setCheckedRadioButton();
                    showQuestion(currentIndex);
                } else {
                    Toast.makeText(getContext(), "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setVisibleCorrectAnswer(){
        correctAnswer1.setVisibility(View.VISIBLE);
        correctAnswer2.setVisibility(View.VISIBLE);
        correctAnswer3.setVisibility(View.VISIBLE);
    }
    public void setUnVisibleCorrectAnswer(){
        correctAnswer1.setVisibility(View.GONE);
        correctAnswer2.setVisibility(View.GONE);
        correctAnswer3.setVisibility(View.GONE);
    }
    private void showQuestion(int index) {
        if (index >= 0 && index < list.size()) {
            if(currentIndex == 0){
                buttonBack.setVisibility(View.GONE);
            }
            else{
                buttonBack.setVisibility(View.VISIBLE);
            }
            if(checkAnswer.get(currentIndex).getSelectedAnswer() == null){
                radioGroup1.clearCheck();
                radioGroup2.clearCheck();
                radioGroup3.clearCheck();
            }
            setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex + 1).getSelectedAnswer(),checkAnswer.get(currentIndex+2).getSelectedAnswer());
            ImageListeningModel currentQuestion = list.get(index);
            if(!currentQuestion.getImage().startsWith("Image")){
                imageShortConversation.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load (currentQuestion.getImage())
                        .into(imageShortConversation);
            }
            else{
                imageShortConversation.setVisibility(View.GONE);
            }
            item.setText(currentIndex + 1 + " / " + list.size());
            questionPart2_question1.setText("Câu " +(currentIndex + 1) + ":" + list.get(currentIndex).getQuestion());
            questionPart2_question2.setText("Câu " +(currentIndex + 2) + ":" + list.get(currentIndex + 1).getQuestion());
            questionPart2_question3.setText("Câu " +(currentIndex + 3) + ":" + list.get(currentIndex + 2).getQuestion());

            radioButtons[0].setText("A: " + list.get(currentIndex).getOptionA());
            radioButtons[1].setText("B: " + list.get(currentIndex).getOptionB());
            radioButtons[2].setText("C: " + list.get(currentIndex).getOptionC());
            radioButtons[3].setText("D: " + list.get(currentIndex).getOptionD());

            radioButtons[4].setText("A: " + list.get(currentIndex + 1).getOptionA());
            radioButtons[5].setText("B: " + list.get(currentIndex + 1).getOptionB());
            radioButtons[6].setText("C: " + list.get(currentIndex + 1).getOptionC());
            radioButtons[7].setText("D: " + list.get(currentIndex + 1).getOptionD());

            radioButtons[8].setText("A: " + list.get(currentIndex + 2).getOptionA());
            radioButtons[9].setText("B: " + list.get(currentIndex + 2).getOptionB());
            radioButtons[10].setText("C: " + list.get(currentIndex + 2).getOptionC());
            radioButtons[11].setText("D: " + list.get(currentIndex + 2).getOptionD());
            playAudio(currentQuestion.getAudio());
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
    public void clickToCheck(ImageListeningModel currentAnswer) {
        shortConversationViewModel.getNavigationToCheck().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                if(radioGroup1.getCheckedRadioButtonId() != -1 && radioGroup2.getCheckedRadioButtonId() != - 1 && radioGroup3.getCheckedRadioButtonId() != -1){
                    CheckListAnswer answer = checkAnswer.get(currentIndex);
                    answer.setCorrect(true);
                    CheckListAnswer answer1 = checkAnswer.get(currentIndex + 1);
                    answer1.setCorrect(true);
                    CheckListAnswer answer2 = checkAnswer.get(currentIndex + 2);
                    answer2.setCorrect(true);
                    setVisibleCorrectAnswer();
                    checkAnswerForGroup(currentIndex, radioGroup1, correctAnswer1);
                    checkAnswerForGroup(currentIndex + 1, radioGroup2, correctAnswer2);
                    checkAnswerForGroup(currentIndex + 2, radioGroup3, correctAnswer3);
                    shortConversationViewModel.clickedToUnCheck();
                }
                else{
                    Toast.makeText(getContext(), "Vui lòng chọn tất cả đáp án", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAnswerForGroup(int id, RadioGroup radioGroup, TextView correctAnswerView) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            ImageListeningModel currentQuestion = list.get(id);
            RadioButton selectedRadioButton = getActivity().findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString().split(":")[0];
            checkAnswer.get(id).setSelectedAnswer(selectedAnswer);
            correctAnswerView.setText("Đáp án đúng là: " + currentQuestion.getAnswerCorrect());
        }
//        } else {
//            Toast.makeText(getContext(), "Please select an answer.", Toast.LENGTH_SHORT).show();
//        }
    }


    public void clickToPauseAudio(){
        shortConversationViewModel.getNavigationPauseAudio().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                buttonPause.setVisibility(View.GONE);
                buttonPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });
    }
    private void clickToButtonPlayAudio(){
        shortConversationViewModel.getNavigationPlayAudio().observe(getViewLifecycleOwner(), clicked->{
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
    public void showCustomSheetsDialog(List<CheckListAnswer> questions) {

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
        adapter = new SheetsAdapter(questions, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                int groupIndex = position / 3;

                currentIndex = groupIndex * 3;

                showQuestion(currentIndex);

                setCheckedRadioButton();

                dialog.dismiss();

                item.setText((currentIndex + 1) + " / " + list.size());
            }
        });

        recyclerView.setAdapter(adapter);


        dialog.show();
    }
    public void setRadioButtonChecked(String answer1, String answer2, String answer3) {
        // Clear selections
        radioGroup1.clearCheck();
        radioGroup2.clearCheck();
        radioGroup3.clearCheck();

        // Set checked radio button for radioGroup1
        if (answer1 != null) {
            switch (answer1) {
                case "A":
                    radioButtons[0].setChecked(true);
                    break;
                case "B":
                    radioButtons[1].setChecked(true);
                    break;
                case "C":
                    radioButtons[2].setChecked(true);
                    break;
                case "D":
                    radioButtons[3].setChecked(true);
                    break;
            }
        }

        // Set checked radio button for radioGroup2
        if (answer2 != null) {
            switch (answer2) {
                case "A":
                    radioButtons[4].setChecked(true);
                    break;
                case "B":
                    radioButtons[5].setChecked(true);
                    break;
                case "C":
                    radioButtons[6].setChecked(true);
                    break;
                case "D":
                    radioButtons[7].setChecked(true);
                    break;
            }
        }

        // Set checked radio button for radioGroup3
        if (answer3 != null) {
            switch (answer3) {
                case "A":
                    radioButtons[8].setChecked(true);
                    break;
                case "B":
                    radioButtons[9].setChecked(true);
                    break;
                case "C":
                    radioButtons[10].setChecked(true);
                    break;
                case "D":
                    radioButtons[11].setChecked(true);
                    break;
            }
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