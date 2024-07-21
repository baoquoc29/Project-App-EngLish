package com.example.testaudioenglish.View;

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
    private LinearLayout finish;
    private int index;
    private KonfettiView konfettiView;
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

        recyclerView = binding.memoryRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        konfettiView = binding.konfettiView;
        memoryCardAdapter = new MemoryCardAdapter(new ArrayList<>(), getContext(),memoryCardViewModel);
        recyclerView.setAdapter(memoryCardAdapter);
        itemTotal = binding.item;
        linearMemory = binding.layoutCard;
        linearFinish = binding.layoutFinish;
        skip = binding.previousItem;
        pass = binding.pass;
        notPass = binding.notPass;
        skipped = binding.skipped;
        notStudy = binding.notStudy;
        isStudy = binding.isStudy;
        titleEnding = binding.titleEnding;
        finish = binding.finish;
        skip.setOnClickListener(view -> showSkip());
        binding.nextButton.setOnClickListener(v -> showNextItem());

        memoryCardViewModel.getClickToClose().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                requireActivity().onBackPressed();
            }
        });

        // Initial call to set up study text
        setUpStudy();
        setUpClickReset();
        setUpMemoryCard();
        return binding.getRoot();
    }

    private void setVisible() {
        if (list.isEmpty()) {
            titleEnding.setText("Bạn đã hoàn thành tất cả các thuật ngữ! Xin chúc mừng");
        } else if (checkError > 0) {
            titleEnding.setText("Tiếp tục luyện tập thêm nhé");
        }
        linearMemory.setVisibility(View.GONE);
        linearFinish.setVisibility(View.VISIBLE);

        Log.d("Debug", "Visible " + linearFinish.getVisibility());
    }

    private void setExamAgain() {
        linearFinish.setVisibility(View.GONE);
        linearMemory.setVisibility(View.VISIBLE);
    }

    private void setTextCountItem() {
        if (itemTotal != null) {
            String text = (currentPosition + 1) + " / " + list.size();
            itemTotal.setText(text);
            Log.d("Debug", "ItemTotal updated to: " + text);
        } else {
            Log.e("Error", "itemTotal is null");
        }
    }

    private void setCountCheck() {
        memoryCardViewModel.count(idTopic).observe(getViewLifecycleOwner(), integer -> {
            if (integer.equals(list.size())) {
                setVisible();
            }
        });
    }

    private void setUpMemoryCard() {
        memoryCardViewModel.getListByTopic(idTopic).observe(getViewLifecycleOwner(), flashCardEntities -> {
            list.clear();
            list.addAll(flashCardEntities);
            setCountCheck();
            Log.d("Debug", "List size: " + list.size());
            if (!list.isEmpty()) {
                setUpSingleCard();
                setTextCountItem();

            } else {
                setVisible();
                showFinish();
            }
        });
    }
    private void setUpSingleCard() {
        if (!list.isEmpty() && currentPosition < list.size()) {
            List<FlashCardEntity> singleItemList = Collections.singletonList(list.get(currentPosition));
            memoryCardAdapter.updateData(singleItemList);
        }
    }

    private void setUpClickReset() {
        memoryCardViewModel.getClickToReset().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                memoryCardViewModel.resetCheck(idTopic);
                setExamAgain();
                setUpMemoryCard();
                setUpToZero();
                konfettiView.setVisibility(View.GONE);
            }
        });
    }

    private void setUpToZero() {
        currentPosition = 0;
        checkError = 0;
        checkFinish = 0;
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
                setVisible();
                showFinish();
            } else {
                updateUI();
            }
        }
    }

    private void showNextItem() {
        if (currentPosition < list.size()) {
            checkFinish++;
            updateCheck(idTopic, list.get(currentPosition).getId());
            currentPosition++;
            if (currentPosition >= list.size()) {
                setVisible();
                showFinish();
            } else {
                updateUI();
            }
        }
    }

    private void updateUI() {
        Log.d("Debug", "Current Position: " + currentPosition);
        setUpSingleCard();
        setTextCountItem();
        setUpStudy();
    }

    private void showFinish() {
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true, true);
        pass.setText("Biết: " + checkFinish);
        notPass.setText("Đang học: " + checkError);
        skipped.setText("Còn lại: " + (list.size() - (checkError + checkFinish)));
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

    private void updateCheck(long idTopic, long idWord) {
        memoryCardViewModel.updateCheck(idTopic, idWord);
    }
}
