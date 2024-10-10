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
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Adapter.ImageAdapter;
import com.example.testaudioenglish.Adapter.SheetsAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.ReadingResponse;
import com.example.testaudioenglish.databinding.FragmentReadingPart7Binding;
import com.example.testaudioenglish.viewmodel.ExamAnyFragmentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadingPartSixFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingPart7Fragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final  String ARG_TAGPART = "tag";

    // TODO: Rename and change types of parameters
    private static final String ID_TOPIC  ="IdTopic";
    private String mParam1;
    private String mParam2;
    private Long idTopic;

    public ReadingPart7Fragment() {
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
    public static ReadingPart7Fragment newInstance(String param1, String param2,String tagPart,Long idTopic) {
        ReadingPart7Fragment fragment = new ReadingPart7Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_TAGPART,tagPart);
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
            tagPart = getArguments().getString(ARG_TAGPART);
            idTopic = getArguments().getLong(ID_TOPIC);
        }
    }
    private ExamAnyFragmentViewModel examAnyFragmentViewModel;
    private ImageView buttonBack,buttonForward;
    private int currentIndex = 0;
    private TextView item;
    private List<ReadingToeicModel> list = new ArrayList<>();
    private List<CheckListAnswer> checkAnswer = new ArrayList<>();
    SparseArray<Integer> indexMapping = new SparseArray<>();
    private SheetsAdapter adapter;
    private String tagPart;
    private RadioGroup[] radioGroups = new RadioGroup[5];
    private RadioButton[] radioButtons = new RadioButton[20];
    private TextView[] questionParts = new TextView[5];
    private ImageView imageReading;
    private TextView correctAnswer1,correctAnswer2,correctAnswer3,correctAnswer4,correctAnswer5;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ImageAdapter imageAdapter;
    private LinearLayout list_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReadingPart7Binding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reading_part7, container, false);
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
    public void mapping(FragmentReadingPart7Binding binding) {
        for (int i = 0; i < 5; i++) {
            radioButtons[i * 4] = binding.getRoot().findViewById(getResources().getIdentifier("radioA_" + (i + 1), "id", getContext().getPackageName()));
            radioButtons[i * 4 + 1] = binding.getRoot().findViewById(getResources().getIdentifier("radioB_" + (i + 1), "id", getContext().getPackageName()));
            radioButtons[i * 4 + 2] = binding.getRoot().findViewById(getResources().getIdentifier("radioC_" + (i + 1), "id", getContext().getPackageName()));
            radioButtons[i * 4 + 3] = binding.getRoot().findViewById(getResources().getIdentifier("radioD_" + (i + 1), "id", getContext().getPackageName()));
        }

        for (int i = 0; i < 5; i++) {
            radioGroups[i] = binding.getRoot().findViewById(getResources().getIdentifier("choicesRadioGroup_" + (i + 1), "id", getContext().getPackageName()));
        }

        buttonBack = binding.buttonBack;
        buttonForward = binding.buttonForward;
        item = binding.item;
        correctAnswer1 = binding.correctAnswer1;
        correctAnswer2 = binding.correctAnswer2;
        correctAnswer3 = binding.correctAnswer3;
        correctAnswer4 = binding.correctAnswer4;
        correctAnswer5 = binding.correctAnswer5;
        list_image = binding.listImage;
        viewPager = binding.viewPager;
        circleIndicator = binding.circleIndicator;

        for (int i = 0; i < 5; i++) {
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
                    currentIndex += getIncrementValue();
                    buttonBack.setVisibility(View.VISIBLE);
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        setUnVisibleCorrectAnswer();
                        for(int i = 0;i<getIncrementValue();i++){
                            radioGroups[i].clearCheck();
                        }
                    }
                    else{
                        checkAnswerClicked();
                        setVisibleCorrectAnswer();
                        setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex+1).getSelectedAnswer(),checkAnswer.get(currentIndex +2).getSelectedAnswer(),checkAnswer.get(currentIndex + 3).getSelectedAnswer(),checkAnswer.get(currentIndex+4).getSelectedAnswer());
                    }
                    showQuestion(currentIndex);
                } else {
                    Toast.makeText(getContext(), "You are at the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setRadioButtonChecked(String answer1, String answer2, String answer3, String answer4,String answer5) {
        RadioButton[][] answerButtons = {
                {radioButtons[0], radioButtons[1], radioButtons[2], radioButtons[3]},
                {radioButtons[4], radioButtons[5], radioButtons[6], radioButtons[7]},
                {radioButtons[8], radioButtons[9], radioButtons[10], radioButtons[11]},
                {radioButtons[12], radioButtons[13], radioButtons[14], radioButtons[15]},
                {radioButtons[16], radioButtons[17], radioButtons[18], radioButtons[19]}
        };

        String[] answers = {answer1, answer2, answer3, answer4,answer5};
        int groupsToCheck = getRadioButtonRange();


        for (int i = 0; i < groupsToCheck; i++) {
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


    private int getIncrementValue() {
        if (currentIndex < 8) {
            return 2;
        } else if (currentIndex < 17) {
            return 3;
        } else if(currentIndex < 29){
            return 4;
        }
        else {
            return 5;
        }
    }

    public void clickToBackQuestion() {
        examAnyFragmentViewModel.getNavigationToBack().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                if (currentIndex >= 0) {
                    if(currentIndex == 8 || currentIndex == 17 || currentIndex == 29){
                        currentIndex -= getIncrementValue();
                        currentIndex = currentIndex + 1;
                    }
                    else{
                        currentIndex -= getIncrementValue();
                    }
                    if(!checkAnswer.get(currentIndex).isCorrect()){
                        setUnVisibleCorrectAnswer();
                        for(int i = 0;i<getIncrementValue();i++){
                            radioGroups[i].clearCheck();
                        }
                    }
                    else{
                        setVisibleCorrectAnswer();
                        checkAnswerClicked();
                        setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex+1).getSelectedAnswer(),checkAnswer.get(currentIndex +2).getSelectedAnswer(),checkAnswer.get(currentIndex + 3).getSelectedAnswer(),checkAnswer.get(currentIndex+4).getSelectedAnswer());
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
        updateRadioButtons();
    }

    public void checkAnswerClicked(){
        setToRadioText();
    }
    public void clickToCheck() {
        examAnyFragmentViewModel.getNavigationToCheck().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                int groupsToCheck = getIncrementValue();  // Get the number of groups to check

                if (!areAllGroupsChecked(groupsToCheck)) {
                    Toast.makeText(getContext(), "Hãy chọn đầy đủ các tùy chọn", Toast.LENGTH_SHORT).show();
                } else {
                    setVisibleCorrectAnswer();
                    for (int i = 0; i < groupsToCheck; i++) {
                        checkAnswerForGroup(currentIndex + i, radioGroups[i], getCorrectAnswer(i));
                    }

                    examAnyFragmentViewModel.clickedToUnCheck();
                }
            }
        });
    }

    private boolean areAllGroupsChecked(int groupsToCheck) {
        for (int i = 0; i < groupsToCheck; i++) {
            if (radioGroups[i].getCheckedRadioButtonId() == -1) {
                return false;
            }
        }
        return true;
    }
    private TextView getCorrectAnswer(int index) {
        switch (index) {
            case 0: return correctAnswer1;
            case 1: return correctAnswer2;
            case 2: return correctAnswer3;
            case 3: return correctAnswer4;
            case 4: return correctAnswer5;
            default: return null;
        }
    }

    public void setVisibleCorrectAnswer(){
        correctAnswer1.setVisibility(View.VISIBLE);
        correctAnswer2.setVisibility(View.VISIBLE);
        correctAnswer3.setVisibility(View.VISIBLE);
        correctAnswer4.setVisibility(View.VISIBLE);
        correctAnswer5.setVisibility(View.VISIBLE);
    }
    public void setUnVisibleCorrectAnswer(){
        correctAnswer1.setVisibility(View.GONE);
        correctAnswer2.setVisibility(View.GONE);
        correctAnswer3.setVisibility(View.GONE);
        correctAnswer4.setVisibility(View.GONE);
        correctAnswer5.setVisibility(View.GONE);

        for(int i = 0;i<radioButtons.length;i++){
            radioButtons[i].setChecked(false);
        }
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
                setMap();
                Integer index = indexMapping.get(position, -1);
                if (index != -1) {
                    currentIndex = index;
                }

                showQuestion(currentIndex);
                dialog.dismiss();
                item.setText(currentIndex + 1 + " / " + list.size());

                if (!checkAnswer.get(currentIndex).isCorrect()) {
                    setUnVisibleCorrectAnswer();
                } else {
                    checkAnswerClicked();
                    setRadioButtonChecked(checkAnswer.get(currentIndex).getSelectedAnswer(),checkAnswer.get(currentIndex+1).getSelectedAnswer(),checkAnswer.get(currentIndex +2).getSelectedAnswer(),checkAnswer.get(currentIndex + 3).getSelectedAnswer(),checkAnswer.get(currentIndex+4).getSelectedAnswer());
                    setVisibleCorrectAnswer();
                }
            }
        });

        recyclerView.setAdapter(adapter);
        dialog.show();
    }
    public void setMap(){
        indexMapping.put(0, 0);
        indexMapping.put(1, 0);
        indexMapping.put(2, 2);
        indexMapping.put(3, 2);
        indexMapping.put(4, 4);
        indexMapping.put(5, 4);
        indexMapping.put(6, 6);
        indexMapping.put(7, 6);
        indexMapping.put(8, 8);
        indexMapping.put(9, 8);
        indexMapping.put(10, 8);
        indexMapping.put(11, 11);
        indexMapping.put(12, 11);
        indexMapping.put(13, 11);
        indexMapping.put(14, 14);
        indexMapping.put(15, 14);
        indexMapping.put(16, 14);
        indexMapping.put(17, 17);
        indexMapping.put(18, 17);
        indexMapping.put(19, 17);
        indexMapping.put(20, 17);
        indexMapping.put(21, 21);
        indexMapping.put(22, 21);
        indexMapping.put(23, 21);
        indexMapping.put(24, 21);
        indexMapping.put(25, 25);
        indexMapping.put(26, 25);
        indexMapping.put(27, 25);
        indexMapping.put(28, 25);
        indexMapping.put(29, 29);
        indexMapping.put(30, 29);
        indexMapping.put(31, 29);
        indexMapping.put(32, 29);
        indexMapping.put(33, 29);
        indexMapping.put(34, 34);
        indexMapping.put(35, 34);
        indexMapping.put(36, 34);
        indexMapping.put(37, 34);
        indexMapping.put(38, 34);
        indexMapping.put(39, 39);
        indexMapping.put(40, 39);
        indexMapping.put(41, 39);
        indexMapping.put(42, 39);
        indexMapping.put(43, 39);
        indexMapping.put(44, 44);
        indexMapping.put(45, 44);
        indexMapping.put(46, 44);
        indexMapping.put(47, 44);
        indexMapping.put(48, 44);
        indexMapping.put(49, 49);
        indexMapping.put(50, 49);
        indexMapping.put(51, 49);
        indexMapping.put(52, 49);
        indexMapping.put(53, 49);
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
            handleAnswerSelection();

            handleNavigationButtonsVisibility();
            updateRadioButtons();
            setQuestionText(index);
            if(currentIndex >= 25){
                imageReading.setVisibility(View.GONE);
                list_image.setVisibility(View.VISIBLE);
                String imageUrlsString = list.get(currentIndex).getImage(); // Đảm bảo phương thức là getImage()
                List<String> list_images = Arrays.asList(imageUrlsString.split(","));
                ImageAdapter imageAdapter = new ImageAdapter(getContext(), list_images);
                viewPager.setAdapter(imageAdapter);

                circleIndicator.setViewPager(viewPager);
            }
            else{
                imageReading.setVisibility(View.VISIBLE);
                list_image.setVisibility(View.GONE);
                loadImageForQuestion(currentIndex);
            }


            item.setText((currentIndex + 1) + " / " + list.size());

            clickToCheck();
        }
    }


    private void handleAnswerSelection() {
        if (checkAnswer.get(currentIndex).getSelectedAnswer() == null) {

        }
    }


    private void handleNavigationButtonsVisibility() {
        if (currentIndex == 0) {
            buttonBack.setVisibility(View.GONE);
        } else {
            buttonBack.setVisibility(View.VISIBLE);
        }
        if(currentIndex >= 49){
            buttonForward.setVisibility(View.GONE);
        }
        else{
            buttonForward.setVisibility(View.VISIBLE);
        }
    }


    private void setQuestionText(int index) {
        for (int i = 0; i < getIncrementValue(); i++) {
            if (currentIndex + i < list.size()) {
                questionParts[i].setText("Question " + (currentIndex + i + 1) + ": " + list.get(currentIndex + i).getQuestion());
                questionParts[i].setVisibility(View.VISIBLE);
                radioGroups[i].setVisibility(View.VISIBLE);
            } else {
                questionParts[i].setVisibility(View.GONE);
                radioGroups[i].setVisibility(View.GONE);
            }
        }

    }


    private void loadImageForQuestion(int currentIndex) {
        Glide.with(this)
                .load(list.get(currentIndex).getImage())
                .into(imageReading);
    }
    private void updateRadioButtons() {
        int range = getRadioButtonRange();

        for (int i = 0; i < range; i++) {
            radioButtons[i * 4].setText("A: " + list.get(currentIndex + i).getOptionA());
            radioButtons[i * 4 + 1].setText("B: " + list.get(currentIndex + i).getOptionB());
            radioButtons[i * 4 + 2].setText("C: " + list.get(currentIndex + i).getOptionC());
            radioButtons[i * 4 + 3].setText("D: " + list.get(currentIndex + i).getOptionD());
        }
    }

    private int getRadioButtonRange() {
        if (currentIndex < 7) {
            radioGroups[2].setVisibility(View.GONE);
            radioGroups[3].setVisibility(View.GONE);
            questionParts[2].setVisibility(View.GONE);
            questionParts[3].setVisibility(View.GONE);
            questionParts[4].setVisibility(View.GONE);
            radioGroups[4].setVisibility(View.GONE);
            return 2;
        } else if (currentIndex < 16) {
            radioGroups[2].setVisibility(View.VISIBLE);
            radioGroups[3].setVisibility(View.GONE);
            questionParts[2].setVisibility(View.VISIBLE);
            questionParts[3].setVisibility(View.GONE);
            questionParts[4].setVisibility(View.GONE);
            radioGroups[4].setVisibility(View.GONE);
            return 3;
        } else if(currentIndex < 28) {
            radioGroups[3].setVisibility(View.VISIBLE);
            questionParts[3].setVisibility(View.VISIBLE);
            radioGroups[4].setVisibility(View.GONE);
            questionParts[4].setVisibility(View.GONE);
            return 4;
        }
        else{
            radioGroups[4].setVisibility(View.VISIBLE);
            questionParts[4].setVisibility(View.VISIBLE);
            return 5;
        }
    }



    private void loadData() {
        examAnyFragmentViewModel.getReadingDataPart6(idTopic,tagPart).observe(getViewLifecycleOwner(), new Observer<ReadingResponse>() {
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