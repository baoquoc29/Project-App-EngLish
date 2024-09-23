package com.example.testaudioenglish.View.FlashCardView;

import static android.app.ProgressDialog.show;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.FragmentExamBinding;
import com.example.testaudioenglish.viewmodel.ExamViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import nl.dionsegijn.konfetti.xml.KonfettiView;


public class ExamFragment extends Fragment {

    private static final String ARG_ID_TOPIC = "idTopic";
    private long idTopic;
    private String mParam1;
    private String mParam2;
    private int currentIndex = 0;
    private ExamViewModel examViewModel;

    public ExamFragment() {
        // Required empty public constructor
    }

    public static ExamFragment newInstance(long idTopic) {
        ExamFragment fragment = new ExamFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID_TOPIC, idTopic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idTopic = getArguments().getLong(ARG_ID_TOPIC);
        }
    }
    // TextViews
    private TextView title, titleAnswer, engVer, answerA, answerB, answerC, answerD, titleCorrect, titleNotCorrect, textFinishALl, textNotFinish, textMerge, skipMerge, answerCorrect, mergeIncorrect, answerCorrectNoSkipped, itemTotal, textEngver, textVietver, titleMergeCorrect, totalCorrect, totalIncorrect;

    // EditTexts
    private EditText answerMax, resultMerge;

    // Switches
    private Switch switchVisibleAnswer, switchCorrectInCorrect, switchMultiple, switchFill;

    // LinearLayouts
    private LinearLayout layoutExam, layoutMerge, layoutMultiple, layoutSkipped, layoutRes, layoutInCorrect, layoutCorrect, layoutSettings, layoutCorrectOrIncorrect, layoutTotalFinish;

    // ImageViews
    private ImageView correctA, correctB, correctC, correctD, incorrectA, incorrectB, incorrectC, incorrectD, btnCorrect, btnIncorrect;

    // AppCompatButtons
    private AppCompatButton buttonNext, checkMerge, turnBack, examAgain;

    // Other Components
    private SeekBar seekBar;
    private KonfettiView konfettiView;
    private List<FlashCardEntity> flashCardList = new ArrayList<>();
    private int checkCorrect;
    private boolean isFinishComplete = false;
    private Boolean setOnlyMultipleChoice = false, setOnlyMerge = false, setOnlyCorrectOrIncorrect = false, setVisibleAnswer = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentExamBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam, container, false);
        examViewModel = new ViewModelProvider(this).get(ExamViewModel.class);
        binding.setViewModel(examViewModel);
        binding.setLifecycleOwner(this);
        setInitsView(binding);
        setTitle();
        setupToolbar(binding.getRoot());
        setCountIdTopic();
        setNumberOfTopic();
        setButtonClick();
        return binding.getRoot();
    }
    public void setInitsView(FragmentExamBinding binding){
        title = binding.title;
        titleAnswer = binding.titleAnswer;
        answerMax = binding.answerMax;
        switchCorrectInCorrect = binding.switchCorrectInCorrect;
        switchFill = binding.switchFill;
        switchVisibleAnswer = binding.switchVisibleAnswer;
        switchMultiple = binding.switchMultiple;
        layoutExam = binding.layoutExam;
        layoutMerge = binding.layoutMerge;
        engVer = binding.engver;
        itemTotal = binding.item;
        answerA = binding.answerA;
        totalCorrect = binding.totalCorrect;
        totalIncorrect = binding.totalIncorrect;
        answerB = binding.answerB;
        textEngver = binding.textEngver;
        textVietver = binding.textVietver;
        btnCorrect = binding.buttonDung;
        turnBack = binding.turnback;
        examAgain = binding.againExam;
        btnIncorrect = binding.buttonSai;
        answerC = binding.answerC;
        layoutCorrectOrIncorrect = binding.layoutCorrectOrIncorrect;
        layoutSettings = binding.layoutSettings;
        answerD = binding.answerD;
        correctA = binding.correctA;
        correctB = binding.correctB;
        correctC = binding.correctC;
        correctD = binding.correctD;
        incorrectA = binding.incorrectA;
        incorrectB = binding.incorrectB;
        layoutTotalFinish = binding.layoutFinish;
        incorrectC = binding.incorrectC;
        incorrectD = binding.incorrectD;
        layoutMultiple = binding.multiple;
        titleMergeCorrect = binding.titleMergeCorrect;
        title = binding.title;
        layoutCorrect = binding.layoutCorrect;
        titleCorrect = binding.titleCorrect;
        titleNotCorrect = binding.titleNotCorrect;
        buttonNext = binding.buttonNext;
        layoutMerge = binding.layoutMerge;
        checkMerge = binding.checkMerge;
        textMerge = binding.textMerge;
        skipMerge = binding.skipMerge;
        answerCorrect = binding.answerCorrect;
        layoutSkipped = binding.layoutSkipped;
        layoutRes = binding.layoutRes;
        layoutInCorrect = binding.layoutInCorrect;
        resultMerge = binding.resultMerge;
        mergeIncorrect = binding.mergeIncorrect;
        answerCorrectNoSkipped = binding.answerCorrectNoSkipped;
        seekBar = binding.seekBar;
        switchMultiple.setChecked(true);
        switchVisibleAnswer.setChecked(true);
    }
    public void setCorrectOrIncorrect() {
        layoutExam.setVisibility(View.GONE);
        showQuizLayout();
        textEngver.setText(flashCardList.get(currentIndex).getEnglishWord());
        examViewModel.getListAnswerInATopic(idTopic).observe(getViewLifecycleOwner(), start -> {
            textVietver.setText(start.get(0));
        });

        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAnswer(true);
                setTimeToTranfer();
            }
        });

        btnIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAnswer(false);
                setTimeToTranfer();
            }
        });

    }

    public void setTimeToTranfer() {
        if (currentIndex < flashCardList.size() - 1) {
            currentIndex++;
            setCorrectOrIncorrect();
            setTextCountItem();
            seekBar.setProgress(currentIndex);
        } else {
            setFinish();
        }
    }

    private void showQuizLayout() {
        seekBar.setVisibility(View.VISIBLE);
        itemTotal.setVisibility(View.VISIBLE);
        layoutSettings.setVisibility(View.GONE);
        layoutCorrectOrIncorrect.setVisibility(View.VISIBLE);
    }

    private void handleAnswer(boolean isCorrectButton) {
        String userAnswer = textVietver.getText().toString();
        String correctAnswer = flashCardList.get(currentIndex).getVietWord();
        boolean isCorrect = userAnswer.equals(correctAnswer);
        if (isCorrect == isCorrectButton) {
            checkCorrect++;
        }
        if (setVisibleAnswer) {
            Toast.makeText(getContext(), isCorrect == isCorrectButton ? "True" : "False", Toast.LENGTH_SHORT).show();
        }
    }



    public void setTitle() {
        examViewModel.getTitleTopic(idTopic).observe(getViewLifecycleOwner(), getTitle -> {
            if (!getTitle.isEmpty()) {
                title.setText(getTitle.toString());
            }
        });
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
                toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
            }
        }
    }

    private void setNumberOfTopic() {
        examViewModel.getCountWord(idTopic).observe(getViewLifecycleOwner(), index -> {
            if (answerMax != null) {
                int number = Integer.valueOf(answerMax.getText().toString());
                titleAnswer.setText("Số câu hỏi (Tối đa " + index + ")");
            }
        });
    }
    private void setTextCountItem() {
        if (itemTotal != null) {
            String text = (currentIndex + 1) + " / " + flashCardList.size();
            itemTotal.setText(text);
        } else {
            Log.e("Error", "itemTotal is null");
        }
    }
    private void setButtonClick() {
        examViewModel.getButtonClick().observe(getViewLifecycleOwner(), clickedToExam -> {
            if (clickedToExam) {
                setUp();
            }
        });
    }


    private void setVisibleMultiple() {
        setOnlyMultipleChoice = true;
        setOnlyMerge = false;
        setOnlyCorrectOrIncorrect = false;

    }
    private void setVisibleMultipleAndIncorrect() {
        setOnlyMultipleChoice = true;
        setOnlyMerge = false;
        setOnlyCorrectOrIncorrect = true;

    }
    private void setVisibleMergeAndIncorrect() {
        setOnlyMultipleChoice = false;
        setOnlyMerge = true;
        setOnlyCorrectOrIncorrect = true;

    }
    private void setVisileOnlyMerge() {
        setOnlyMerge = true;
        setOnlyCorrectOrIncorrect = false;
        setOnlyMultipleChoice = false;

    }
    private void setVisibleAllTypeExam(){
        setOnlyMultipleChoice = true;
        setOnlyMerge = true;
        setOnlyCorrectOrIncorrect = true;

    }
    private void setVisibleMultipleAndMerge() {
        setOnlyMultipleChoice = true;
        setOnlyMerge = true;


    }
    private void setCountIdTopic(){
        examViewModel.getCountWord(idTopic).observe(getViewLifecycleOwner(),index ->{
            answerMax.setText(String.valueOf(index));
        });
    }
    private void setUp() {
        examViewModel.getList(idTopic, Integer.valueOf(answerMax.getText().toString())).observe(getViewLifecycleOwner(), flashCardEntities -> {
            flashCardList.clear();
            flashCardList.addAll(flashCardEntities);
            Collections.shuffle(flashCardList);
            seekBar.setMax(flashCardList.size() - 1);
            setTextCountItem();
            updateQuestion();
            if(checkSize()){
                Toast.makeText(getContext(), "Again", Toast.LENGTH_SHORT).show();
            }
            else{
                updateExamVisibility();
                setQuestionType();
            }
        });

        setAnswerClickListeners();
        buttonNextClicked();

    }
    private void updateExamVisibility() {
        // Use separate method to update visibility based on switch states
        if (switchVisibleAnswer.isChecked()) {
            setVisibleAnswer = true;
        }

        if (switchCorrectInCorrect.isChecked()) {
            setCorrectOrIncorrect();
        }

        if (switchMultiple.isChecked() && switchFill.isChecked() && switchCorrectInCorrect.isChecked()) {
            setVisibleAllTypeExam();
        } else if (switchMultiple.isChecked() && switchFill.isChecked()) {
            setVisibleMultipleAndMerge();
        } else if (switchMultiple.isChecked() && switchCorrectInCorrect.isChecked()) {
            setVisibleMultipleAndIncorrect();
        } else if (switchFill.isChecked() && switchCorrectInCorrect.isChecked()) {
            setVisibleMergeAndIncorrect();
        } else if (switchMultiple.isChecked()) {
            setVisibleMultiple();
        } else if (switchFill.isChecked()) {
            setVisileOnlyMerge();
        }
        else{
            Toast.makeText(getContext(), "Please enter the switch", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateQuestion() {
        if (currentIndex < flashCardList.size()) {
            FlashCardEntity currentCard = flashCardList.get(currentIndex);
            engVer.setText(currentCard.getEnglishWord());
            List<String> answers = new ArrayList<>();
            answers.add(currentCard.getVietWord());
            examViewModel.getListAnswer(currentCard.getEnglishWord()).observe(getViewLifecycleOwner(), answerList -> {
                if (answerList.size() >= 3) {
                    answers.addAll(answerList.subList(0, 3));
                    Collections.shuffle(answers);
                    answerA.setText(answers.get(0));
                    answerB.setText(answers.get(1));
                    answerC.setText(answers.get(2));
                    answerD.setText(answers.get(3));
                }
            });

            seekBar.setProgress(currentIndex);

            resetUI();
        }
    }

    private void setAnswerClickListeners() {
        View.OnClickListener answerClickListener = view -> {
            TextView clickedAnswer = (TextView) view;
            boolean isCorrect = clickedAnswer.getText().toString().equals(flashCardList.get(currentIndex).getVietWord());
            setAnswerResult(view, isCorrect);
            disableAnswers();
            buttonNext.setVisibility(View.VISIBLE);
        };

        answerA.setOnClickListener(answerClickListener);
        answerB.setOnClickListener(answerClickListener);
        answerC.setOnClickListener(answerClickListener);
        answerD.setOnClickListener(answerClickListener);
    }

    private void setAnswerResult(View view, boolean isCorrect) {
        int correctIcon = isCorrect ? View.VISIBLE : View.INVISIBLE;
        int incorrectIcon = isCorrect ? View.INVISIBLE : View.VISIBLE;
        if(setVisibleAnswer) {
            if (view.getId() == R.id.answerA) {
                correctA.setVisibility(correctIcon);
                incorrectA.setVisibility(incorrectIcon);
            } else if (view.getId() == R.id.answerB) {
                correctB.setVisibility(correctIcon);
                incorrectB.setVisibility(incorrectIcon);
            } else if (view.getId() == R.id.answerC) {
                correctC.setVisibility(correctIcon);
                incorrectC.setVisibility(incorrectIcon);
            } else if (view.getId() == R.id.answerD) {
                correctD.setVisibility(correctIcon);
                incorrectD.setVisibility(incorrectIcon);
            }
        }

        if (isCorrect && setVisibleAnswer) {
            Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
            setCorrectChoice();
            checkCorrect++;
        } else if(!isCorrect && setVisibleAnswer){
            Toast.makeText(getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            setNotCorrectChoice();
        }
        else if(isCorrect){
            checkCorrect++;
        }
        else{

        }
    }

    private void buttonNextClicked() {
        buttonNext.setOnClickListener(view -> {
            if (currentIndex < flashCardList.size() - 1) {
                currentIndex++;
                updateQuestion();
                setQuestionType();
                setTextCountItem();
            } else {
                setFinish();
            }
        });
    }

    private void setFinish() {
        layoutMerge.setVisibility(View.GONE);
        layoutMultiple.setVisibility(View.GONE);
        layoutSettings.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE);
        layoutTotalFinish.setVisibility(View.VISIBLE);
        layoutCorrectOrIncorrect.setVisibility(View.GONE);
        totalIncorrect.setText("Sai: " + (flashCardList.size()  - checkCorrect));
        totalCorrect.setText("Đúng: " + checkCorrect);
        examViewModel.getButton_turn_back().observe(getViewLifecycleOwner(),clicked ->{
            if(clicked){
                getActivity().onBackPressed();
            }
        });
        examViewModel.getButton_exam_again().observe(getViewLifecycleOwner(),clicked ->{
            if(clicked && !isFinishComplete){
                setToZero();
                layoutSettings.setVisibility(View.VISIBLE);
                layoutTotalFinish.setVisibility(View.GONE);
                examAgain.setEnabled(false);
                isFinishComplete = true;
            }
            else{
                isFinishComplete = false;
                layoutSettings.setVisibility(View.GONE);
            }
        });
    }
    private void setToZero(){
        checkCorrect = 0;
        currentIndex = 0;
    }

    private void setNotCorrectChoice() {
        title.setVisibility(View.GONE);
        titleNotCorrect.setVisibility(View.VISIBLE);
    }

    private void setCorrectChoice() {
        title.setVisibility(View.GONE);
        titleCorrect.setVisibility(View.VISIBLE);
    }
    private boolean checkSize() {
        int numOfQuestions = Integer.parseInt(answerMax.getText().toString());
        return numOfQuestions < 0 || numOfQuestions > flashCardList.size();
    }
    private void disableAnswers() {
        answerA.setEnabled(false);
        answerB.setEnabled(false);
        answerC.setEnabled(false);
        answerD.setEnabled(false);
    }

    private void resetUI() {
        title.setVisibility(View.VISIBLE);
        titleCorrect.setVisibility(View.GONE);
        titleNotCorrect.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE);
        enableAnswers();
        hideAllIcons();
    }
    private void showMultipleChoiceLayoutNotVisible() {
        hideCorrectIncorrectLayouts();
        seekBar.setVisibility(View.VISIBLE);
        itemTotal.setVisibility(View.VISIBLE);
        layoutSettings.setVisibility(View.GONE);
        layoutMultiple.setVisibility(View.VISIBLE);
        layoutExam.setVisibility(View.VISIBLE);
        layoutMerge.setVisibility(View.GONE);
        checkMerge.setVisibility(View.GONE);
        hideAllIcons();
        updateQuestion();
    }

    private void showMergeLayoutNotVisible() {
        resultMerge.setText("");
        layoutSkipped.setVisibility(View.GONE);
        layoutMultiple.setVisibility(View.GONE);
        layoutInCorrect.setVisibility(View.GONE);
        layoutSettings.setVisibility(View.GONE);
        layoutExam.setVisibility(View.VISIBLE);
        layoutMerge.setVisibility(View.VISIBLE);
        checkMerge.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.GONE);
        layoutRes.setVisibility(View.VISIBLE);
        hideAllIcons();
        hideCorrectIncorrectLayouts();
        setUpClickedMerge();
    }

    private void showMultipleChoiceLayout() {
        seekBar.setVisibility(View.VISIBLE);
        itemTotal.setVisibility(View.VISIBLE);
        layoutExam.setVisibility(View.VISIBLE);
        layoutSettings.setVisibility(View.GONE);
        layoutMultiple.setVisibility(View.VISIBLE);
        layoutMerge.setVisibility(View.GONE);
        checkMerge.setVisibility(View.GONE);
        updateQuestion();
    }

    private void showMergeLayout() {
        resultMerge.setText("");
        layoutSkipped.setVisibility(View.GONE);
        layoutMultiple.setVisibility(View.GONE);
        layoutInCorrect.setVisibility(View.GONE);
        layoutExam.setVisibility(View.VISIBLE);
        layoutSettings.setVisibility(View.GONE);
        layoutMerge.setVisibility(View.VISIBLE);
        checkMerge.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.GONE);
        layoutRes.setVisibility(View.VISIBLE);
        setUpClickedMerge();
    }

    private void setQuestionType() {
        Random random = new Random();
        boolean isVisibleAnswerChecked = setVisibleAnswer;
        boolean isMultipleChecked = setOnlyMultipleChoice;
        boolean isMergeChecked = setOnlyMerge;
        boolean isCorrectInCorrectChecked = setOnlyCorrectOrIncorrect;
        if (isVisibleAnswerChecked) {
            if (isMultipleChecked && !isMergeChecked && !isCorrectInCorrectChecked) {
                showMultipleChoiceLayout();
            } else if (!isMultipleChecked && isMergeChecked && !isCorrectInCorrectChecked) {
                showMergeLayout();
            } else if (isMultipleChecked && isMergeChecked && !isCorrectInCorrectChecked) {
                if (random.nextInt(2) == 0) {
                    showMultipleChoiceLayout();
                } else {
                    showMergeLayout();
                }
            } else if (isMultipleChecked && isMergeChecked && isCorrectInCorrectChecked) {
                int randomNumber = random.nextInt(3);
                if (randomNumber == 0) {
                    showMultipleChoiceLayout();
                } else if (randomNumber == 1) {
                    showMergeLayout();
                } else {
                    setCorrectOrIncorrect();
                }
            } else if (isMultipleChecked && isCorrectInCorrectChecked && !isMergeChecked) {
                if (random.nextInt(2) == 0) {
                    showMultipleChoiceLayout();
                } else {
                    setCorrectOrIncorrect();
                }
            } else if (!isMultipleChecked && isCorrectInCorrectChecked && isMergeChecked) {
                if (random.nextInt(2) == 0) {
                    showMergeLayout();
                } else {
                    setCorrectOrIncorrect();
                }
            }
        } else {
            if (isMultipleChecked && !isMergeChecked && !isCorrectInCorrectChecked) {
                showMultipleChoiceLayoutNotVisible();
            } else if (!isMultipleChecked && isMergeChecked && !isCorrectInCorrectChecked) {
                showMergeLayoutNotVisible();
            } else if (isMultipleChecked && isMergeChecked && !isCorrectInCorrectChecked) {
                if (random.nextInt(2) == 0) {
                    showMultipleChoiceLayoutNotVisible();
                } else {
                    showMergeLayoutNotVisible();
                }
            } else if (isMultipleChecked && isMergeChecked && isCorrectInCorrectChecked) {
                int randomNumber = random.nextInt(3);
                if (randomNumber == 0) {
                    showMultipleChoiceLayoutNotVisible();
                } else if (randomNumber == 1) {
                    showMergeLayoutNotVisible();
                } else {
                    setCorrectOrIncorrect();
                }
            } else if (isMultipleChecked && isCorrectInCorrectChecked && !isMergeChecked) {
                if (random.nextInt(2) == 0) {
                    showMultipleChoiceLayoutNotVisible();
                } else {
                    setCorrectOrIncorrect();
                }
            } else if (!isMultipleChecked && isCorrectInCorrectChecked && isMergeChecked) {
                if (random.nextInt(2) == 0) {
                    showMergeLayoutNotVisible();
                } else {
                    setCorrectOrIncorrect();
                }
            }
        }
    }

    private void hideCorrectIncorrectLayouts() {
        layoutCorrect.setVisibility(View.GONE);
        layoutInCorrect.setVisibility(View.GONE);
        titleNotCorrect.setVisibility(View.GONE);
        layoutSkipped.setVisibility(View.GONE);
        titleCorrect.setVisibility(View.GONE);
        correctA.setVisibility(View.GONE);
        correctB.setVisibility(View.GONE);
        correctC.setVisibility(View.GONE);
        correctD.setVisibility(View.GONE);
        incorrectA.setVisibility(View.GONE);
        incorrectB.setVisibility(View.GONE);
        incorrectC.setVisibility(View.GONE);
        incorrectD.setVisibility(View.GONE);
    }



    private void setUpClickedMerge() {
        textMerge.setText(flashCardList.get(currentIndex).getEnglishWord());
        checkMerge.setEnabled(true);
        layoutSkipped.setVisibility(View.GONE);
        layoutRes.setVisibility(View.VISIBLE);
        layoutCorrect.setVisibility(View.GONE);

        checkMerge.setOnClickListener(view -> {
            if (setVisibleAnswer) {
                if (!resultMerge.getText().toString().equalsIgnoreCase(flashCardList.get(currentIndex).getVietWord()) && !textMerge.getText().toString().isEmpty()) {
                    layoutRes.setVisibility(View.GONE);
                    layoutInCorrect.setVisibility(View.VISIBLE);
                    mergeIncorrect.setText(resultMerge.getText().toString());
                    answerCorrectNoSkipped.setText(flashCardList.get(currentIndex).getVietWord());
                } else if (resultMerge.getText().toString().equalsIgnoreCase(flashCardList.get(currentIndex).getVietWord())) {
                    layoutCorrect.setVisibility(View.VISIBLE);
                    checkCorrect++;
                } else {
                    Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                }
            } else if(resultMerge.getText().toString().equalsIgnoreCase(flashCardList.get(currentIndex).getVietWord())) {
                checkCorrect++;
            }
            checkMerge.setEnabled(false);

            new Handler().postDelayed(() -> {
                if (currentIndex < flashCardList.size() - 1) {
                    currentIndex++;
                    setQuestionType();
                    setTextCountItem();
                    seekBar.setProgress(currentIndex);
                } else {
                    Toast.makeText(getContext(), "End of quiz", Toast.LENGTH_SHORT).show();
                    layoutMerge.setVisibility(View.GONE);
                    layoutMultiple.setVisibility(View.GONE);
                    setFinish();
                }
            }, 2000); // 2 giây
        });

        skipMerge.setOnClickListener(view -> {
            if (setVisibleAnswer) {
                layoutSkipped.setVisibility(View.VISIBLE);
                layoutRes.setVisibility(View.GONE);
                checkMerge.setEnabled(false);
                answerCorrect.setText(flashCardList.get(currentIndex).getVietWord());

                new Handler().postDelayed(() -> {
                    if (currentIndex < flashCardList.size() - 1) {
                        currentIndex++;
                        setTextCountItem();
                        setQuestionType();
                        seekBar.setProgress(currentIndex);
                    } else {
                        Toast.makeText(getContext(), "End of quiz", Toast.LENGTH_SHORT).show();
                        checkMerge.setVisibility(View.GONE);
                        setFinish();
                    }
                }, 3000); // 3 giây
            } else {
                layoutSkipped.setVisibility(View.VISIBLE);
                answerCorrect.setVisibility(View.GONE);
                layoutRes.setVisibility(View.GONE);
                checkMerge.setEnabled(false);
                answerCorrect.setText(flashCardList.get(currentIndex).getVietWord());
                titleMergeCorrect.setVisibility(View.GONE);
                new Handler().postDelayed(() -> {
                    if (currentIndex < flashCardList.size() - 1) {
                        currentIndex++;
                        setTextCountItem();
                        setQuestionType();
                        seekBar.setProgress(currentIndex);
                    } else {
                        Toast.makeText(getContext(), "End of quiz", Toast.LENGTH_SHORT).show();
                        checkMerge.setVisibility(View.GONE);
                        setFinish();
                    }
                }, 3000); // 3 giây

            }
        });
    }



    private void hideAllIcons() {
        correctA.setVisibility(View.INVISIBLE);
        correctB.setVisibility(View.INVISIBLE);
        correctC.setVisibility(View.INVISIBLE);
        correctD.setVisibility(View.INVISIBLE);
        incorrectA.setVisibility(View.INVISIBLE);
        incorrectB.setVisibility(View.INVISIBLE);
        incorrectC.setVisibility(View.INVISIBLE);
        incorrectD.setVisibility(View.INVISIBLE);
    }

    private void enableAnswers() {
        answerA.setEnabled(true);
        answerB.setEnabled(true);
        answerC.setEnabled(true);
        answerD.setEnabled(true);
    }
}