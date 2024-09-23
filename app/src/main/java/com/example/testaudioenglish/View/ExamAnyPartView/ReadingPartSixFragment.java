package com.example.testaudioenglish.View.ExamAnyPartView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Adapter.SheetsAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.Model.ToeicModel.MultichoiceToeicModel;
import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;
import com.example.testaudioenglish.Response.ReadingResponse;
import com.example.testaudioenglish.databinding.FragmentIncompleteSentencesBinding;
import com.example.testaudioenglish.databinding.FragmentReadingPartSixBinding;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadingPartSixFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingPartSixFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final  String ARG_TAGPART = "tag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReadingPartSixFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadingPartSixFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadingPartSixFragment newInstance(String param1, String param2,String tagPart) {
        ReadingPartSixFragment fragment = new ReadingPartSixFragment();
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
    private ImageView buttonBack,buttonForward;
    private int currentIndex = 0;
    private TextView item;
    private List<ReadingToeicModel> list = new ArrayList<>();
    private List<CheckListAnswer> checkAnswer = new ArrayList<>();

    private RadioGroup radioGroup;
    private TextView correctAnswer;
    private SheetsAdapter adapter;
    private String tagPart;
    private RadioGroup[] radioGroups = new RadioGroup[4];
    private RadioButton[] radioButtons = new RadioButton[16];
    private TextView[] questionParts = new TextView[4];
    private ImageView imageReading;
    private TextView correctAnswer1,correctAnswer2,correctAnswer3,correctAnswer4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReadingPartSixBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reading_part_six, container, false);
        examAnyFragmentViewModel = new ViewModelProvider(this).get(ExamAnyFragmentViewModel.class);
        binding.setViewModel(examAnyFragmentViewModel);
        binding.setLifecycleOwner(this);
        mapping(binding);
        loadData();
        setToClose();
        clickToDialogSheets();
        clickToBackQuestion();
        clickToNextQuestion();
        return binding.getRoot();
    }
    public void mapping(FragmentReadingPartSixBinding binding) {
        for (int i = 0; i < 4; i++) {
            radioButtons[i * 4] = binding.getRoot().findViewById(getResources().getIdentifier("radioA_" + (i + 1), "id", getContext().getPackageName()));
            radioButtons[i * 4 + 1] = binding.getRoot().findViewById(getResources().getIdentifier("radioB_" + (i + 1), "id", getContext().getPackageName()));
            radioButtons[i * 4 + 2] = binding.getRoot().findViewById(getResources().getIdentifier("radioC_" + (i + 1), "id", getContext().getPackageName()));
            radioButtons[i * 4 + 3] = binding.getRoot().findViewById(getResources().getIdentifier("radioD_" + (i + 1), "id", getContext().getPackageName()));
        }

        for (int i = 0; i < 4; i++) {
            radioGroups[i] = binding.getRoot().findViewById(getResources().getIdentifier("choicesRadioGroup_" + (i + 1), "id", getContext().getPackageName()));
        }

        buttonBack = binding.buttonBack;
        buttonForward = binding.buttonForward;
        item = binding.item;
        correctAnswer1 = binding.correctAnswer1;
        correctAnswer2 = binding.correctAnswer2;
        correctAnswer3 = binding.correctAnswer3;
        correctAnswer4 = binding.correctAnswer4;
        for (int i = 0; i < 4; i++) {
            questionParts[i] = binding.getRoot().findViewById(getResources().getIdentifier("questionPart6_" + (i + 1), "id", getContext().getPackageName()));
        }
        imageReading = binding.imageReading;

        // Xử lý nút Back
        if (currentIndex == 0) {
            buttonBack.setVisibility(View.GONE);
        } else {
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
                if (currentIndex < list.size() - 1) {
                    currentIndex = currentIndex + 4;
                    buttonBack.setVisibility(View.VISIBLE);
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        setUnVisibleCorrectAnswer();
                        for(int i = 0;i<4;i++){
                            radioGroups[i].clearCheck();
                        }
                    }
                    else{
                        checkAnswerClicked();
                        setVisibleCorrectAnswer();
                        setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex+1).getSelectedAnswer(),checkAnswer.get(currentIndex +2).getSelectedAnswer(),checkAnswer.get(currentIndex + 3).getSelectedAnswer());
                    }
                    showQuestion(currentIndex);
                } else {
                    Toast.makeText(getContext(), "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setRadioButtonChecked(String answer1, String answer2, String answer3, String answer4) {
        RadioButton[][] answerButtons = {
                {radioButtons[0], radioButtons[1], radioButtons[2], radioButtons[3]},
                {radioButtons[4], radioButtons[5], radioButtons[6], radioButtons[7]},
                {radioButtons[8], radioButtons[9], radioButtons[10], radioButtons[11]},
                {radioButtons[12], radioButtons[13], radioButtons[14], radioButtons[15]}
        };
        String[] answers = {answer1, answer2, answer3, answer4};

        for (int i = 0; i < 4; i++) {
            radioGroups[i].clearCheck();
            switch (answers[i]) {
                case "A":
                    answerButtons[i][0].setChecked(true);
                    break;
                case "B":
                    answerButtons[i][1].setChecked(true);
                    break;
                case "C":
                    answerButtons[i][2].setChecked(true);
                    break;
                case "D":
                    answerButtons[i][3].setChecked(true);
                    break;
            }
        }
    }
    public void clickToBackQuestion() {
        examAnyFragmentViewModel.getNavigationToBack().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                if (currentIndex > 0) {
                    currentIndex = currentIndex - 4;
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        setUnVisibleCorrectAnswer();
                        for(int i = 0;i<4;i++){
                            radioGroups[i].clearCheck();
                        }
                    }
                    else{
                        setVisibleCorrectAnswer();
                        checkAnswerClicked();
                        setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex+1).getSelectedAnswer(),checkAnswer.get(currentIndex +2).getSelectedAnswer(),checkAnswer.get(currentIndex + 3).getSelectedAnswer());
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
        radioButtons[0].setText("A: " + list.get(currentIndex).getOptionA());
        radioButtons[1].setText("B: " + list.get(currentIndex).getOptionB());
        radioButtons[2].setText("C: " + list.get(currentIndex).getOptionC());
        radioButtons[3].setText("D: " +list.get(currentIndex).getOptionD());

        radioButtons[4].setText("A: " + list.get(currentIndex + 1).getOptionA());
        radioButtons[5].setText("B: " + list.get(currentIndex + 1).getOptionB());
        radioButtons[6].setText("C: " + list.get(currentIndex + 1).getOptionC());
        radioButtons[7].setText("D: " +list.get(currentIndex + 1).getOptionD());


        radioButtons[8].setText("A: " + list.get(currentIndex + 2).getOptionA());
        radioButtons[9].setText("B: " + list.get(currentIndex + 2).getOptionB());
        radioButtons[10].setText("C: " + list.get(currentIndex + 2).getOptionC());
        radioButtons[11].setText("D: " +list.get(currentIndex + 2).getOptionD());


        radioButtons[12].setText("A: " + list.get(currentIndex + 3).getOptionA());
        radioButtons[13].setText("B: " + list.get(currentIndex + 3).getOptionB());
        radioButtons[14].setText("C: " + list.get(currentIndex + 3).getOptionC());
        radioButtons[15].setText("D: " +list.get(currentIndex + 3).getOptionD());
    }

    public void checkAnswerClicked(){
        setToRadioText();
    }
    public void clickToCheck(){
        examAnyFragmentViewModel.getNavigationToCheck().observe(getViewLifecycleOwner(), clicked->{
            if(clicked){
                if (radioGroups[0].getCheckedRadioButtonId() == -1 || radioGroups[1].getCheckedRadioButtonId() == -1 ||  radioGroups[2].getCheckedRadioButtonId() == -1 || radioGroups[3].getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Hãy chọn đầy đủ các tùy chọn", Toast.LENGTH_SHORT).show();
                }
                else{
                    setVisibleCorrectAnswer();
                    checkAnswerForGroup(currentIndex, radioGroups[0], correctAnswer1);
                    checkAnswerForGroup(currentIndex + 1, radioGroups[1], correctAnswer2);
                    checkAnswerForGroup(currentIndex + 2, radioGroups[2], correctAnswer3);
                    checkAnswerForGroup(currentIndex + 3, radioGroups[2], correctAnswer4);
                    examAnyFragmentViewModel.clickedToUnCheck();
                }
            }
        });
    }
    public void setVisibleCorrectAnswer(){
        correctAnswer1.setVisibility(View.VISIBLE);
        correctAnswer2.setVisibility(View.VISIBLE);
        correctAnswer3.setVisibility(View.VISIBLE);
        correctAnswer4.setVisibility(View.VISIBLE);
    }
    public void setUnVisibleCorrectAnswer(){
        correctAnswer1.setVisibility(View.GONE);
        correctAnswer2.setVisibility(View.GONE);
        correctAnswer3.setVisibility(View.GONE);
        correctAnswer4.setVisibility(View.GONE);
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
                int groupIndex = position / 4;

                currentIndex = groupIndex * 4;

                showQuestion(currentIndex);


                dialog.dismiss();
                item.setText(currentIndex + 1 + " / " + list.size());
                if(!checkAnswer.get(currentIndex).isCorrect()){
                    setUnVisibleCorrectAnswer();
                }
                else{
                    checkAnswerClicked();
                    setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex+1).getSelectedAnswer(),checkAnswer.get(currentIndex +2).getSelectedAnswer(),checkAnswer.get(currentIndex + 3).getSelectedAnswer());
                    setVisibleCorrectAnswer();
                }
            }
        });
        recyclerView.setAdapter(adapter);


        dialog.show();
    }

    public void updateRadioButtonsForNewQuestion(ReadingToeicModel currentAnswer) {
        setToRadioText();
    }
    private void checkAnswerForGroup(int id, RadioGroup radioGroup, TextView correctAnswerView) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            ReadingToeicModel currentQuestion = list.get(id);
            RadioButton selectedRadioButton = getActivity().findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString().split(":")[0];
            checkAnswer.get(id).setSelectedAnswer(selectedAnswer);
            checkAnswer.get(id).setCorrect(true);
            correctAnswerView.setText("Đáp án đúng là: " + currentQuestion.getAnswerCorrect());
        } else {
            Toast.makeText(getContext(), "Please select an answer.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showQuestion(int index) {
        if (index >= 0 && index < list.size()) {
            if(checkAnswer.get(currentIndex).getSelectedAnswer() == null){

            }
            if(currentIndex == 0){
                buttonBack.setVisibility(View.GONE);
            }
            else{
                buttonBack.setVisibility(View.VISIBLE);
            }
            if(currentIndex == 12){
                buttonForward.setVisibility(View.GONE);
            }
            else{
                buttonForward.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 4; i++) {
                questionParts[i].setText("Question " + (index + i + 1) + ": ");
            }
            Glide.with(this)
                    .load(list.get(currentIndex).getIamge())
                    .into(imageReading);
            radioButtons[0].setText("A: " + list.get(currentIndex).getOptionA());
            radioButtons[1].setText("B: " + list.get(currentIndex).getOptionB());
            radioButtons[2].setText("C: " + list.get(currentIndex).getOptionC());
            radioButtons[3].setText("D: " +list.get(currentIndex).getOptionD());

            radioButtons[4].setText("A: " + list.get(currentIndex + 1).getOptionA());
            radioButtons[5].setText("B: " + list.get(currentIndex + 1).getOptionB());
            radioButtons[6].setText("C: " + list.get(currentIndex + 1).getOptionC());
            radioButtons[7].setText("D: " +list.get(currentIndex + 1).getOptionD());


            radioButtons[8].setText("A: " + list.get(currentIndex + 2).getOptionA());
            radioButtons[9].setText("B: " + list.get(currentIndex + 2).getOptionB());
            radioButtons[10].setText("C: " + list.get(currentIndex + 2).getOptionC());
            radioButtons[11].setText("D: " +list.get(currentIndex + 2).getOptionD());


            radioButtons[12].setText("A: " + list.get(currentIndex + 3).getOptionA());
            radioButtons[13].setText("B: " + list.get(currentIndex + 3).getOptionB());
            radioButtons[14].setText("C: " + list.get(currentIndex + 3).getOptionC());
            radioButtons[15].setText("D: " +list.get(currentIndex + 3).getOptionD());
            item.setText(currentIndex + 1 + " / " + list.size());

            clickToCheck();
        }
    }



    private void loadData() {
        examAnyFragmentViewModel.getReadingDataPart6(1,tagPart).observe(getViewLifecycleOwner(), new Observer<ReadingResponse>() {
            @Override
            public void onChanged(ReadingResponse readingResponse) {
                if ( readingResponse== null || readingResponse.getData() == null) {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                    return;
                }
                list.clear();
                list.addAll(readingResponse.getData());

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