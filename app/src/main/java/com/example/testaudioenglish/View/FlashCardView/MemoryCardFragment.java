package com.example.testaudioenglish.View.FlashCardView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testaudioenglish.Adapter.MemoryCardAdapter;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.FragmentMemoryCardBinding;
import com.example.testaudioenglish.viewmodel.MemoryCardViewModel;

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

public class MemoryCardFragment extends Fragment {
    private static final String ARG_ID_TOPIC = "idTopic";
    private long idTopic;
    private List<FlashCardEntity> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoryCardViewModel memoryCardViewModel;
    private MemoryCardAdapter memoryCardAdapter;
    private int currentPosition = 0;
    private TextView itemTotal;
    private LinearLayout linearMemory;
    private LinearLayout linearFinish;
    private int checkFinish = 0;
    private int checkError = 0;
    private AppCompatButton skip;
    private TextView pass;
    private TextView notPass;
    private TextView skipped;
    private TextView notStudy;
    private TextView isStudy;
    private TextView titleEnding;
    private KonfettiView konfettiView;
    private boolean isMemoryCardAgainSetUp = false;
    private boolean isMemoryCardSetUp = false;
    public MemoryCardFragment() {
        // Required empty public constructor
    }

    public static MemoryCardFragment newInstance(long idTopic) {
        MemoryCardFragment fragment = new MemoryCardFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMemoryCardBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memory_card, container, false);
        memoryCardViewModel = new ViewModelProvider(this).get(MemoryCardViewModel.class);
        binding.setViewModel(memoryCardViewModel);
        binding.setLifecycleOwner(this);

        initializeViews(binding);
        initializeRecyclerView(binding);
        setUpObservers();

        skip.setOnClickListener(view -> showSkip());
        binding.nextButton.setOnClickListener(v -> showNextItem());

        setUpStudy();
        setUpMemoryCard();

        return binding.getRoot();
    }

    private void initializeViews(FragmentMemoryCardBinding binding) {
        itemTotal = binding.item;
        linearMemory = binding.layoutCard;
        linearFinish = binding.layoutFinish;
        skip = binding.previousItem;
        pass = binding.pass;
        notPass = binding.notPass;
        notStudy = binding.notStudy;
        isStudy = binding.isStudy;
        titleEnding = binding.titleEnding;
        konfettiView = binding.konfettiView;
    }

    private void initializeRecyclerView(FragmentMemoryCardBinding binding) {
        recyclerView = binding.memoryRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memoryCardAdapter = new MemoryCardAdapter(new ArrayList<>(), getContext(), memoryCardViewModel);
        recyclerView.setAdapter(memoryCardAdapter);
    }

    private void setUpObservers() {
        memoryCardViewModel.getClickToClose().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                requireActivity().onBackPressed();
            }
        });

        memoryCardViewModel.getClickToAgain().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                getActivity().onBackPressed();
                konfettiView.setVisibility(View.GONE);
            }
        });

        memoryCardViewModel.getClickToReset().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                memoryCardViewModel.resetCheck(idTopic);
                resetExam();
                setToZero();
                setUpMemoryCard();
                konfettiView.setVisibility(View.GONE);
            }
        });
    }
    public void setToZero(){
        checkFinish = 0;
        checkError = 0;
        currentPosition =  0;
    }
    private void setVisible(boolean finished) {
        if (finished) {
            titleEnding.setText(R.string.completed_all_terms);
        } else if (checkError > 0) {
            titleEnding.setText(R.string.continue_practicing);
        }
        linearMemory.setVisibility(View.GONE);
        linearFinish.setVisibility(View.VISIBLE);
    }

    private void resetExam() {
        linearFinish.setVisibility(View.GONE);
        linearMemory.setVisibility(View.VISIBLE);
    }

    private void setTextCountItem() {
        if (itemTotal != null) {
            String text = (currentPosition + 1) + " / " + list.size();
            itemTotal.setText(text);
        } else {
            Log.e("Error", "itemTotal is null");
        }
    }

    private void setCountCheck() {
        memoryCardViewModel.count(idTopic).observe(getViewLifecycleOwner(), integer -> {
            if (integer.equals(list.size())) {
                setVisible(true);
            }
        });
    }

    private void setUpMemoryCard() {
        if(isMemoryCardSetUp){
            return;
        }
        isMemoryCardSetUp = true;
        memoryCardViewModel.getListByTopic(idTopic).observe(getViewLifecycleOwner(), flashCardEntities -> {
            list.clear();
            list.addAll(flashCardEntities);
            setCountCheck();
            if (!list.isEmpty()) {
                setUpSingleCard();
                setTextCountItem();
            } else {
                setVisible(true);
                showFinish();
            }
        });
    }
//    private void setUpMemoryCardAgain() {
//        if (isMemoryCardAgainSetUp) {
//            return; // Nếu đã gọi trước đó, không làm gì thêm
//        }
//        isMemoryCardAgainSetUp = true;
//        if (checkFinish == list.size()) {
//            Toast.makeText(getContext(), "Finish", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            setToZero();
//            resetExam();
//            memoryCardViewModel.getListAgainByTopic(idTopic).observe(getViewLifecycleOwner(), flashCardEntities -> {
//                Log.d("Debug402", "The List Call");
//                list.clear();
//                list.addAll(flashCardEntities);
//                if (!list.isEmpty()) {
//                    setCountCheck();
//                    setUpSingleCard();
//                    setTextCountItem();
//                } else {
//                    setVisible(true);
//                    showFinish();
//                }
//            });
//        }
//    }

    private void setUpSingleCard() {
        if (!list.isEmpty() && currentPosition < list.size()) {
            List<FlashCardEntity> singleItemList = Collections.singletonList(list.get(currentPosition));
            memoryCardAdapter.updateData(singleItemList);
        }
    }

    private void setUpStudy() {
        if (notStudy != null && isStudy != null) {
            notStudy.setText(String.valueOf(checkError));
            isStudy.setText(String.valueOf(checkFinish));
        } else {
            Log.e("Error", "notStudy or isStudy is null");
        }
    }

    private void showSkip() {
        if (currentPosition < list.size()) {
            checkError++;
            currentPosition++;
            if (currentPosition >= list.size()) {
                setVisible(false);
                showFinish();
            } else {
                updateUI();
            }
        }
    }

    private void showNextItem() {
        if (currentPosition < list.size()) {
            updateCheck(idTopic, list.get(currentPosition).getId());
            checkFinish++;
            currentPosition++;
            if (currentPosition >= list.size()) {
                setVisible(false);
                showFinish();
            } else {
                updateUI();
            }
        }
    }

    private void updateUI() {
        setUpSingleCard();
        setTextCountItem();
        setUpStudy();
    }

    private void showFinish() {
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true, true);
        pass.setText(getString(R.string.known) + " " + checkFinish);
        notPass.setText(getString(R.string.studying ) + " " +  checkError );

        konfettiView.setVisibility(View.VISIBLE);
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

    private void updateCheck(long idTopic, long idWord) {
        memoryCardViewModel.updateCheck(idTopic, idWord);
    }
}
