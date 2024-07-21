package com.example.testaudioenglish.View;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.FragmentMultipleChoiceBinding;
import com.example.testaudioenglish.viewmodel.MultichoicepleViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class MultipleChoiceFragment extends Fragment {

    private static final String ARG_ID_TOPIC = "idTopic";
    private long idTopic;
    private MultichoicepleViewModel multichoicepleViewModel;

    private ImageView close;
    private List<FlashCardEntity> flashCardList = new ArrayList<>();
    private TextView engVer;
    private TextView answerA;
    private TextView answerB;
    private TextView answerC;
    private TextView answerD;
    private int currentIndex = 0;
    private TextView title;
    private TextView titleCorrect;
    private TextView titleNotCorrect;
    private AppCompatButton buttonNext;
    private SeekBar seekBar;
    private int checkCorrect;
    private LinearLayout layoutMultiple;
    private TextView textFinishALl;
    private TextView textNotFinish;
    private KonfettiView konfettiView;
    private ImageView correctA;
    private ImageView correctB;
    private ImageView correctC;
    private ImageView correctD;
    private ImageView incorrectA;
    private ImageView incorrectB;
    private ImageView incorrectC;
    private ImageView incorrectD;
    private LinearLayout layoutMerge;
    private AppCompatButton checkMerge;
    private TextView textMerge;
    private TextView skipMerge;
    private LinearLayout layoutSkipped;
    private TextView answerCorrect;
    private LinearLayout layoutRes;
    private LinearLayout layoutInCorrect;
    private EditText resultMerge;
    private TextView mergeIncorrect;
    private TextView answerCorrectNoSkipped;
    private LinearLayout layoutCorrect;
    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    public static MultipleChoiceFragment newInstance(long idTopic) {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMultipleChoiceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_choice, container, false);
        multichoicepleViewModel = new ViewModelProvider(this).get(MultichoicepleViewModel.class);
        binding.setViewModel(multichoicepleViewModel);
        binding.setLifecycleOwner(this);

        initViews(binding);
        setUp();
        setQuestionTypeMerge();
        return binding.getRoot();
    }

    private void initViews(FragmentMultipleChoiceBinding binding) {
        close = binding.close;
        engVer = binding.engver;
        answerA = binding.answerA;
        answerB = binding.answerB;
        answerC = binding.answerC;
        answerD = binding.answerD;
        correctA = binding.correctA;
        correctB = binding.correctB;
        correctC = binding.correctC;
        correctD = binding.correctD;
        incorrectA = binding.incorrectA;
        incorrectB = binding.incorrectB;
        incorrectC = binding.incorrectC;
        incorrectD = binding.incorrectD;
        seekBar = binding.seekBar;
        konfettiView = binding.konfettiView;
        textNotFinish = binding.textNotFinish;
        textFinishALl = binding.textFinishALl;
        layoutMultiple = binding.multiple;
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

        close.setOnClickListener(view -> getActivity().onBackPressed());
    }

    private void setUp() {
        multichoicepleViewModel.getList(idTopic).observe(getViewLifecycleOwner(), flashCardEntities -> {
            flashCardList.clear();
            flashCardList.addAll(flashCardEntities);
            seekBar.setMax(flashCardList.size() - 1);
            updateQuestion();
        });
        setAnswerClickListeners();
        buttonNextClicked();
    }

    private void updateQuestion() {
        if (currentIndex < flashCardList.size()) {
            FlashCardEntity currentCard = flashCardList.get(currentIndex);
            engVer.setText(currentCard.getEnglishWord());
            List<String> answers = new ArrayList<>();
            answers.add(currentCard.getVietWord());
            multichoicepleViewModel.getListAnswer(currentCard.getEnglishWord()).observe(getViewLifecycleOwner(), answerList -> {
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

        if (isCorrect) {
            Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
            setCorrectChoice();
            checkCorrect++;
        } else {
            Toast.makeText(getContext(), "Incorrect", Toast.LENGTH_SHORT).show();
            setNotCorrectChoice();
        }
    }

    private void buttonNextClicked() {
        buttonNext.setOnClickListener(view -> {
            if (currentIndex < flashCardList.size() - 1) {
                Toast.makeText(getContext(), currentIndex + "", Toast.LENGTH_SHORT).show();
                currentIndex++;
                updateQuestion();
                setQuestionType();
            } else {
                setFinish();
                Toast.makeText(getContext(), "End of quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFinish() {
        layoutMerge.setVisibility(View.GONE);
        layoutMultiple.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE);

        if (checkCorrect == flashCardList.size() - 1  && currentIndex == flashCardList.size() - 1) {
            if(checkCorrect == 0){
                textFinishALl.setText("Chúc mừng bạn đã hoàn thành! " + checkCorrect  + "/" + (flashCardList.size()));
            }
            else {
                textFinishALl.setText("Chúc mừng bạn đã hoàn thành! " + (checkCorrect + 1) + "/" + (flashCardList.size()));
            }
            textFinishALl.setVisibility(View.VISIBLE);
            showFinish();
        } else if (currentIndex == flashCardList.size() - 1) {
            if(checkCorrect == 0){
                textNotFinish.setText("Hãy cố gắng lần sau! " + checkCorrect + "/" + (flashCardList.size()));
            }
            else{
                textNotFinish.setText("Hãy cố gắng lần sau! " + (checkCorrect + 1) + "/" + (flashCardList.size()));
            }
            textNotFinish.setVisibility(View.VISIBLE);
            showFinish();
        }
    }

    private void setNotCorrectChoice() {
        title.setVisibility(View.GONE);
        titleNotCorrect.setVisibility(View.VISIBLE);
    }

    private void setCorrectChoice() {
        title.setVisibility(View.GONE);
        titleCorrect.setVisibility(View.VISIBLE);
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

    private void setQuestionType() {
        if (currentIndex % 2 != 0) {
            resultMerge.setText("");
            layoutSkipped.setVisibility(View.GONE);
            layoutMultiple.setVisibility(View.GONE);
            layoutInCorrect.setVisibility(View.GONE);
            layoutMerge.setVisibility(View.VISIBLE);
            checkMerge.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.GONE);
            layoutRes.setVisibility(View.VISIBLE);
            setUpClickedMerge();
        } else {
            layoutMultiple.setVisibility(View.VISIBLE);
            layoutMerge.setVisibility(View.GONE);
            checkMerge.setVisibility(View.GONE);

            updateQuestion();
        }
    }

    private void setQuestionTypeMerge() {
        if (layoutMultiple.getVisibility() == View.VISIBLE) {
            buttonNextClicked();
        } else if (layoutMerge.getVisibility() == View.VISIBLE) {
            setUpClickedMerge();
        }
    }

    private void setUpClickedMerge() {
        textMerge.setText(flashCardList.get(currentIndex).getEnglishWord());
        checkMerge.setEnabled(true);
        layoutSkipped.setVisibility(View.GONE);
        layoutRes.setVisibility(View.VISIBLE);
        layoutCorrect.setVisibility(View.GONE);
        checkMerge.setOnClickListener(view -> {
            if (!resultMerge.getText().toString().equalsIgnoreCase(flashCardList.get(currentIndex).getVietWord()) && !textMerge.getText().toString().isEmpty()){
                layoutRes.setVisibility(View.GONE);
                layoutInCorrect.setVisibility(View.VISIBLE);
                mergeIncorrect.setText(resultMerge.getText().toString());
                answerCorrectNoSkipped.setText(flashCardList.get(currentIndex).getVietWord());
            }
            else if(resultMerge.getText().toString().equalsIgnoreCase(flashCardList.get(currentIndex).getVietWord())){
                layoutCorrect.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
            }
            checkMerge.setEnabled(false);
            new Handler().postDelayed(() -> {
                if (currentIndex < flashCardList.size() - 1) {
                    currentIndex++;
                    setQuestionType();
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
            layoutSkipped.setVisibility(View.VISIBLE);
            layoutRes.setVisibility(View.GONE);
            checkMerge.setEnabled(false);
            answerCorrect.setText(flashCardList.get(currentIndex).getVietWord());

            // Tự động chuyển câu hỏi sau 5 giây
            new Handler().postDelayed(() -> {
                if (currentIndex < flashCardList.size() - 1) {
                    currentIndex++;
                    setQuestionType();
                    seekBar.setProgress(currentIndex);
                } else {
                    Toast.makeText(getContext(), "End of quiz", Toast.LENGTH_SHORT).show();
                    setFinish();
                }
            }, 3000); // 3 giây
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

    private void showFinish() {
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true, true);
        // Ensure KonfettiView is visible
        konfettiView.setVisibility(View.VISIBLE);
        // Setup Konfetti
        EmitterConfig emitterConfig = new Emitter(300, TimeUnit.MILLISECONDS).max(300);
        konfettiView.start(new PartyFactory(emitterConfig)
                .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE, drawableShape)
                .spread(360)
                .position(0.5, 0.25, 1, 1)
                .sizes(new Size(8, 50, 10))
                .timeToLive(10000)
                .fadeOutEnabled(true)
                .build());
    }
}
