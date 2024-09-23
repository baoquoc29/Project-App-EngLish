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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.example.testaudioenglish.Model.ToeicModel.MultichoiceToeicModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.ListeningResponse;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;
import com.example.testaudioenglish.databinding.FragmentIncompleteSentencesBinding;
import com.example.testaudioenglish.databinding.FragmentPictureListeningBinding;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncompleteSentencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncompleteSentencesFragment extends Fragment implements OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_TAGPART = "tagpart";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncompleteSentencesFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static IncompleteSentencesFragment newInstance(String param1, String param2,String tagPart) {
        IncompleteSentencesFragment fragment = new IncompleteSentencesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_TAGPART,tagPart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            tagPart = getArguments().getString(ARG_TAGPART);
        }
    }
    private ExamAnyFragmentViewModel examAnyFragmentViewModel;
    private RadioButton radioButtonA,radioButtonB,radioButtonC,radioButtonD;

    private ImageView buttonBack,buttonForward;
    private int currentIndex = 0;
    private TextView item;
    private List<MultichoiceToeicModel> list = new ArrayList<>();
    private List<CheckListAnswer> checkAnswer = new ArrayList<>();

    private RadioGroup radioGroup;
    private TextView correctAnswer;
    private TextView questionPart5;
    private SheetsAdapter adapter;
    private String tagPart;
    private WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentIncompleteSentencesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_incomplete_sentences, container, false);
        examAnyFragmentViewModel = new ViewModelProvider(this).get(ExamAnyFragmentViewModel.class);
        binding.setViewModel(examAnyFragmentViewModel);
        binding.setLifecycleOwner(this);
        mapping(binding);
        loadData();
        setToClose();
        clickToDialogSheets();
        clickToBackQuestion();
        clickToNextQuestion();
        saveWordToFlashCard();
        return binding.getRoot();
    }
    public void saveWordToFlashCard(){
        questionPart5.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                menu.clear();

                mode.getMenuInflater().inflate(R.menu.menusaveword, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }
    public void mapping(FragmentIncompleteSentencesBinding binding){
        radioButtonA = binding.radioA;
        radioButtonB = binding.radioB;
        radioButtonC = binding.radioC;
        radioButtonD = binding.radioD;
        buttonBack = binding.buttonBack;
        buttonForward = binding.buttonForward;
        item = binding.item;
        radioGroup = binding.choicesRadioGroup;
        correctAnswer = binding.correctAnswer;
        questionPart5 = binding.questionPart5;
        if(currentIndex == 0){
            buttonBack.setVisibility(View.GONE);
        }
        else{
            buttonBack.setVisibility(View.VISIBLE);
        }
    }
    public void clickToDialogSheets(){
        examAnyFragmentViewModel.getNavigationToSheets().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                showCustomSheetsDialog();
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
            radioGroup.clearCheck();

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



    public void clickToBackQuestion() {
        examAnyFragmentViewModel.getNavigationToBack().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                // Kiểm tra để đảm bảo currentIndex không xuống dưới 0
                if (currentIndex > 0) {
                    currentIndex = currentIndex - 1; // Giảm index xuống 1
                    if(!checkAnswer.get(currentIndex).isCorrect()){
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


    private void setToRadioText(){
        radioButtonA.setText("A: " + list.get(currentIndex).getOptionA());
        radioButtonB.setText("B: " + list.get(currentIndex).getOptionB());
        radioButtonC.setText("C: " + list.get(currentIndex).getOptionC());
        radioButtonD.setText("D: " +list.get(currentIndex).getOptionD());
    }

    public void checkAnswerClicked(){
        setToRadioText();
    }
    public void clickToCheck(){
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
    public void showCustomSheetsDialog() {

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
                currentIndex = position;
                showQuestion(currentIndex);
                dialog.dismiss();
                item.setText(currentIndex + 1 + " / " + list.size());
                if(!checkAnswer.get(currentIndex).isCorrect()){
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

    public void updateRadioButtonsForNewQuestion(MultichoiceToeicModel currentAnswer) {
        setToRadioText();
    }

    private void checkAnswer(int id,RadioGroup radioGroupCheck) {

        int selectedId = radioGroupCheck.getCheckedRadioButtonId();
        if (selectedId != -1) {
            MultichoiceToeicModel currentQuestion = list.get(id);
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



    private void showQuestion(int index) {
        if (index >= 0 && index < list.size()) {
            if(checkAnswer.get(currentIndex).getSelectedAnswer() == null){
                radioGroup.clearCheck();
            }
            if(currentIndex == 0){
                buttonBack.setVisibility(View.GONE);
            }
            else{
                buttonBack.setVisibility(View.VISIBLE);
            }
            MultichoiceToeicModel currentQuestion = list.get(index);
            radioButtonA.setText("A: " + currentQuestion.getOptionA());
            radioButtonB.setText("B: " + currentQuestion.getOptionB());
            radioButtonC.setText("C: " + currentQuestion.getOptionC());
            radioButtonD.setText("D: " + currentQuestion.getOptionD());
            questionPart5.setText("Câu " + (currentIndex + 1) + ": " + currentQuestion.getQuestion());
            item.setText(currentIndex + 1 + " / " + list.size());

            clickToCheck();
        }
    }


    private void loadData() {
        examAnyFragmentViewModel.getListeningAllDataPart5(1).observe(getViewLifecycleOwner(), new Observer<MultipleChoiceRespone>() {
            @Override
            public void onChanged(MultipleChoiceRespone multipleChoiceRespone) {
                if (multipleChoiceRespone == null || multipleChoiceRespone.getData() == null) {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    return;
                }
                list.clear();
                list.addAll(multipleChoiceRespone.getData());

                if (!list.isEmpty()) {
                    setDataForCheckAnswer();
                    showQuestion(currentIndex);
                } else {
                    Log.d("Bug404", "No audio data available.");
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {

    }
}