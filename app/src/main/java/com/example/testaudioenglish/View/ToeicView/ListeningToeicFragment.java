package com.example.testaudioenglish.View.ToeicView;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Activity.ExamToeicActivity;
import com.example.testaudioenglish.InterfaceAdapter.OnPartCompleteListener;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.databinding.FragmentListeningToeicBinding;
import com.example.testaudioenglish.viewmodel.ListeningToeicViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ListeningToeicFragment extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnPartCompleteListener onPartCompleteListener;
    private ListeningToeicViewModel listeningToeicViewModel;

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    private List<ImageListeningModel> list = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private RadioGroup radioGroup;
    private RadioButton radioA, radioB, radioC, radioD;
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private boolean setCheckCorrect;
    private ImageView image;
    private TextView numberQuestion;
    private LinearLayout layoutPartThirdAndFour1, layoutPartFirst;
    private TextView numberQuestion1, numberQuestion2, numberQuestion3;
    private RadioButton radioButtonA1, radioButtonB1, radioButtonC1, radioButtonD1;
    private RadioButton radioButtonA2, radioButtonB2, radioButtonC2, radioButtonD2;
    private RadioButton radioButtonA3, radioButtonB3, radioButtonC3, radioButtonD3;
    private ImageView imageListeningThirdAndFour;
    private RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private int indexCorrect = 0;
    private Button got;
    private boolean setCheckCorrect1;
    private boolean setCheckCorrect2;
    private boolean setCheckCorrect3;
    private Dialog dialog;
    private int index;
    private AppCompatButton buttonStart;
    private LinearLayout layoutReady;
    private int correctAnswerPart1 = 0;
    private int correctAnswerPart2 = 0;
    private int correctAnswerPart3 = 0;
    private int correctAnswerPart4 = 0;
    private Button check;
    public ListeningToeicFragment() {
        // Required empty public constructor
    }

    public static ListeningToeicFragment newInstance(String param1, String param2) {
        ListeningToeicFragment fragment = new ListeningToeicFragment();
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
        FragmentListeningToeicBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listening_toeic, container, false);
        listeningToeicViewModel = new ViewModelProvider(this).get(ListeningToeicViewModel.class);
        binding.setViewModel(listeningToeicViewModel);
        binding.setLifecycleOwner(this);
        initializeUIComponents(binding);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutReady.setVisibility(View.GONE);
                layoutPartFirst.setVisibility(View.VISIBLE);
                loadData();
                setUpFinish();
                setAnswerClickListeners();
            }
        });

        return binding.getRoot();
    }
    public void setOnPartCompleteListener(OnPartCompleteListener listener) {
        this.onPartCompleteListener = listener;
    }
    public void setUpFinish(){
       if( ((ExamToeicActivity) getActivity()).isCheckFinish()){
           finishExam();
           ((ExamToeicActivity) getActivity()).updatedCorrect(correctAnswers);
       }
    }
    public long getIdTopic(){
      return   ((ExamToeicActivity) getActivity()).getIdTopic();
    }
    private void loadData() {
        ((ExamToeicActivity) getActivity()).setVisibleBottomNavigation();
        ((ExamToeicActivity) getActivity()).setTimer();
        showLoadingDialog();
            listeningToeicViewModel.getListeningAllData(getIdTopic()).observe(getViewLifecycleOwner(), new Observer<ListeningResponse>() {
                @Override
                public void onChanged(ListeningResponse listeningResponse) {
                    if (listeningResponse == null || listeningResponse.getData() == null) {
                        Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    list.clear();
                    list.addAll(listeningResponse.getData());

                    if (!list.isEmpty()) {
                        showQuestion();

                    } else {
                        Log.d("Bug404", "No audio data available.");
                    }
                    dismissLoadingDialog();
                }
            });
    }
    private void initializeUIComponents(FragmentListeningToeicBinding binding) {
        radioA = binding.radioA;
        radioB = binding.radioB;
        radioC = binding.radioC;
        radioD = binding.radioD;
        radioButtonA1 = binding.radioA1;
        radioButtonB1 = binding.radioB1;
        radioButtonC1 = binding.radioC1;
        radioButtonD1 = binding.radioD1;
        layoutReady = binding.layoutReady;
        buttonStart = binding.buttonStart;
        radioButtonA2 = binding.radioA2;
        radioButtonB2 = binding.radioB2;
        radioButtonC2 = binding.radioC2;
        radioButtonD2 = binding.radioD2;

        radioButtonA3 = binding.radioA3;
        radioButtonB3 = binding.radioB3;
        radioButtonC3 = binding.radioC3;
        radioButtonD3 = binding.radioD3;
        layoutPartThirdAndFour1 = binding.layoutPartThirdAndFour1;
        layoutPartFirst = binding.layoutPartFirst;
        numberQuestion1 = binding.numberQuestion1;
        numberQuestion2 = binding.numberQuestion2;
        numberQuestion3 = binding.numberQuestion3;
        imageListeningThirdAndFour = binding.imageListeningThirdAndFour;
        numberQuestion = binding.numberQuestion;
        image = binding.image;
        radioGroup = binding.radioGroup;
        radioGroup1 = binding.radioGroup1;
        radioGroup2 = binding.radioGroup2;
        radioGroup3 = binding.radioGroup3;
    }
    private void setRadioButtonDefault() {
        resetRadioButtonGroup(radioButtonA1, radioButtonB1, radioButtonC1, radioButtonD1);
        resetRadioButtonGroup(radioButtonA2, radioButtonB2, radioButtonC2, radioButtonD2);
        resetRadioButtonGroup(radioButtonA3, radioButtonB3, radioButtonC3, radioButtonD3);
    }

    private void resetRadioButtonGroup(RadioButton... radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            radioButton.setChecked(false);
        }
    }

    public OnPartCompleteListener getOnPartCompleteListener() {
        return onPartCompleteListener;
    }

    private void showQuestion() {

        radioA.setChecked(false);
        radioB.setChecked(false);
        radioC.setChecked(false);
        radioD.setChecked(false);


        if (currentIndex >= list.size()) {
            finishExam();
            return;
        }


        ImageListeningModel currentQuestion = list.get(currentIndex);


        if (currentQuestion.getQuestion().equals("Not support")) {
            numberQuestion.setText("Câu: " + (currentIndex + 1));
        } else {
            numberQuestion.setText("Câu: " + (currentIndex + 1) + " " + currentQuestion.getQuestion());
        }


         image.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load (currentQuestion.getImage())
                    .into(image);

        if (currentIndex > 30 && currentIndex < 100) {
            setRadioButtonDefault();
            updatePartThreeAndFourUI(currentQuestion);
        } else if (currentIndex > 5 && currentIndex < 31) {
            radioD.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }
        playAudio(currentQuestion.getAudio());
        Toast.makeText(getContext(), "" + correctAnswerPart4, Toast.LENGTH_SHORT).show();
    }

    private void finishExam() {
        ExamToeicActivity activity = (ExamToeicActivity) getActivity();
        if (activity != null) {
            activity.setCorrectAnswerPart1(correctAnswerPart1);
            activity.setCorrectAnswerPart2(correctAnswerPart2);
            activity.setCorrectAnswerPart3(correctAnswerPart3);
            activity.setCorrectAnswerPart4(correctAnswerPart4);

            activity.updatedCorrect(correctAnswers);
            activity.onPartComplete(); 
            activity.setTotalListening(correctAnswers);
        } else {
            Log.e("Error", "Activity is null or not an instance of ExamToeicActivity");
        }
    }


    private void updatePartThreeAndFourUI(ImageListeningModel currentQuestion) {
        if (!currentQuestion.getImage().equals("Image is not support here")) {
            imageListeningThirdAndFour.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(currentQuestion.getImage())
                    .into(imageListeningThirdAndFour);
        } else {
            imageListeningThirdAndFour.setVisibility(View.GONE);
        }
       // image.setVisibility(View.VISIBLE);
        radioD.setVisibility(View.VISIBLE);
        layoutPartFirst.setVisibility(View.GONE);
        layoutPartThirdAndFour1.setVisibility(View.VISIBLE);
        numberQuestion1.setText("Câu " + (currentIndex + 1) + " " + list.get(currentIndex).getQuestion());
        numberQuestion2.setText("Câu " + (currentIndex + 2) + " " + list.get(currentIndex + 1).getQuestion());
        numberQuestion3.setText("Câu " + (currentIndex + 3) + " " + list.get(currentIndex + 2).getQuestion());
        updateRadioButtonText(currentIndex, radioButtonA1, radioButtonB1, radioButtonC1, radioButtonD1);
        updateRadioButtonText(currentIndex + 1, radioButtonA2, radioButtonB2, radioButtonC2, radioButtonD2);
        updateRadioButtonText(currentIndex + 2, radioButtonA3, radioButtonB3, radioButtonC3, radioButtonD3);
        setAnswerPartThirdAndFourClickListeners();
    }
    private void updateRadioButtonText(int index, RadioButton... radioButtons) {
        radioButtons[0].setText("A. " + list.get(index).getOptionA());
        radioButtons[1].setText("B. " + list.get(index).getOptionB());
        radioButtons[2].setText("C. " + list.get(index).getOptionC());
        radioButtons[3].setText("D. " + list.get(index).getOptionD());
    }

    public void showLoadingDialog() {
        if (dialog == null) {
            dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.activity_splash, null));
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    public void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void setAnswerPartThirdAndFourClickListeners() {
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean isCorrect = checkedRadioButton.getText().toString().startsWith(list.get(currentIndex).getAnswerCorrect());
            handleAnswer2(isCorrect);
        });
        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean isCorrect = checkedRadioButton.getText().toString().startsWith(list.get(currentIndex + 1).getAnswerCorrect());
            handleAnswer2(isCorrect);
        });
        radioGroup3.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean isCorrect = checkedRadioButton.getText().toString().startsWith(list.get(currentIndex + 2).getAnswerCorrect());
            handleAnswer2(isCorrect);
        });
    }

    private void setAnswerClickListeners() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean isCorrect = checkedRadioButton.getText().toString().startsWith(list.get(currentIndex).getAnswerCorrect());
            handleAnswer(isCorrect);

        });
    }

    private void handleAnswer2(boolean isCorrect) {
        if (isCorrect) {
            checkAnswerByAnyPart();
            correctAnswers++;
        } else {

        }
    }

    private void handleAnswer(boolean isCorrect) {
        if (isCorrect) {
            setCheckCorrect = true;
        } else {
            setCheckCorrect = false;
        }
    }

    private void playAudio(String audioUrl) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer(); // Initialize MediaPlayer
                } else {
                    mediaPlayer.reset(); // Reset if it already exists
                }
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> {
                    new Handler().postDelayed(() -> {
                        if (setCheckCorrect) {
                            checkAnswerByAnyPart();
                            correctAnswers++;
                            setCheckCorrect = false;
                        }
                        currentIndex += (currentIndex > 30 && currentIndex < 100) ? 3 : 1;
                        if (currentIndex >= list.size()) {
                            finishExam();
                        } else {
                            showQuestion();
                        }

                    }, 100); // 0.1 second delay
                });
            } catch (IOException e) {
                Log.e("MediaPlayerError", "Error playing audio", e);
            }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void checkAnswerByAnyPart() {
        if (currentIndex >= 0 && currentIndex < 6) {
            ++correctAnswerPart1;
        }
        if (currentIndex >= 6 && currentIndex < 31) {
            ++correctAnswerPart2;
        }
        if (currentIndex >= 31 && currentIndex < 70) {
            ++correctAnswerPart3;
        }
        if (currentIndex >= 70 && currentIndex < 100) {
            ++correctAnswerPart4;
        }
    }


}