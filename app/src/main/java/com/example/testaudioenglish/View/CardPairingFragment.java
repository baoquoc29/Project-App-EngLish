package com.example.testaudioenglish.View;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.CardPairingAdapter;
import com.example.testaudioenglish.CongratulationClicked;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.FragmentCardPairingBinding;
import com.example.testaudioenglish.viewmodel.CardPairingViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class CardPairingFragment extends Fragment implements CongratulationClicked {

    private static final String TAG = "CardPairingFragment";
    private static final String ARG_ID_TOPIC = "idTopic";
    private long idTopic;
    private CardPairingViewModel cardPairingViewModel;
    private List<FlashCardEntity> list = new ArrayList<>();
    private CardPairingAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout layoutReady;
    private TextView timerTextView;
    private KonfettiView konfettiView;
    private TextView txtCongratulations;
    public CardPairingFragment() {
        // Required empty public constructor
    }

    public static CardPairingFragment newInstance(long idTopic) {
        CardPairingFragment fragment = new CardPairingFragment();
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
        Log.d(TAG, "onCreate: idTopic = " + idTopic);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using DataBinding
        FragmentCardPairingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_pairing, container, false);
        View rootView = binding.getRoot();

        // Initialize ViewModel
        cardPairingViewModel = new ViewModelProvider(this).get(CardPairingViewModel.class);

        binding.setViewModel(cardPairingViewModel);
        binding.setLifecycleOwner(this);
        layoutReady = binding.ready;
        recyclerView = binding.recyclerView;
        timerTextView = binding.timerTextView;
        konfettiView = binding.konfettiView;
        txtCongratulations = binding.txtCongratulations;
        // Setup Toolbar
        setupToolbar(rootView);

        // Setup Observers
        setupObservers();
        return rootView;
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
                activity.getSupportActionBar().setTitle("Ghép thẻ");
                toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
            }
        }
    }

    private void setUpVisible(){
        layoutReady.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);
    }

    private void setupObservers() {
        cardPairingViewModel.getReadyClicked().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    setUpVisible();
                    loadData();
                    timerTextView.setText("00:00");
                    cardPairingViewModel.startTimer();
                }
            }
        });

        cardPairingViewModel.getTimerText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                timerTextView.setText(s + " giây");
            }
        });

    }
    private void setUpCongratulatuions(){
        cardPairingViewModel.stopTimer();
        konfettiView.setVisibility(View.VISIBLE);
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true, true);
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
        txtCongratulations.setVisibility(View.VISIBLE);
        txtCongratulations.setText("Xin chúc mừng bạn đã hoàn thành với thời gian " + timerTextView.getText());
    }
    private void loadData() {
        cardPairingViewModel.getWordByIdTopic(idTopic).observe(getViewLifecycleOwner(), new Observer<List<FlashCardEntity>>() {
            @Override
            public void onChanged(List<FlashCardEntity> flashCardEntities) {
                list.clear();
                list.addAll(flashCardEntities);
                adapter = new CardPairingAdapter(list, cardPairingViewModel,CardPairingFragment.this);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
             //   Log.d(TAG, "Data loaded and adapter set: " + list.size() + " items");
            }
        });
    }

    @Override
    public void onCongratulationClicked() {
        setUpCongratulatuions();
    }
}
