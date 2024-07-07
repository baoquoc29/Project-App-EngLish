package com.example.testaudioenglish.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testaudioenglish.OnItemClickListener;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Adapter.TopicFlashCardAdapter;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Repository.TopicFlashCardRepository;
import com.example.testaudioenglish.databinding.FragmentVocabularyBinding;
import com.example.testaudioenglish.viewmodel.VocabularyFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class VocabularyFragment extends Fragment implements OnItemClickListener {
    private VocabularyFragmentViewModel vocabularyFragmentViewModel;
    private RecyclerView recyclerView;
    private TopicFlashCardAdapter adapter;
    private ArrayList<TopicFlashCardEntity> flashCardList;
    private EditText search;
    private int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentVocabularyBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vocabulary, container, false);
        vocabularyFragmentViewModel = new ViewModelProvider(requireActivity()).get(VocabularyFragmentViewModel.class);
        binding.setViewModelFlashCard(vocabularyFragmentViewModel);
        binding.setLifecycleOwner(this);

        // Configure RecyclerView
        recyclerView = binding.recyclerviewDocument;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search = binding.search;
        flashCardList = new ArrayList<>();
        adapter = new TopicFlashCardAdapter(flashCardList, new TopicFlashCardRepository(requireActivity().getApplication()), this);
        recyclerView.setAdapter(adapter);
        vocabularyFragmentViewModel.getFlashCardsByUsername("testab12").observe(getViewLifecycleOwner(), flashCards -> {
            flashCardList.clear();
            flashCardList.addAll(flashCards);
            adapter.notifyDataSetChanged();
        });

        // Observe navigation event
        vocabularyFragmentViewModel.navigateToAddFlashCard.observe(getViewLifecycleOwner(), navigate -> {
            Boolean shouldNavigate = navigate.getContentIfNotHandled();
            if (shouldNavigate != null && shouldNavigate) {
                Intent intent = new Intent(getActivity(), AddFlashCardActivity.class);
                startActivity(intent);
            }
        });
        vocabularyFragmentViewModel.navigateToTopic.observe(getViewLifecycleOwner(), navigate -> {
            Boolean shouldNavigate = navigate.getContentIfNotHandled();
            if (shouldNavigate != null && shouldNavigate) {
                Intent intent = new Intent(getActivity(), LearningActivity.class);
                intent.putExtra("value", flashCardList.get(position).getId());
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });
        return binding.getRoot();
    }

    public void filterData(String title) {
        vocabularyFragmentViewModel.getFlashCardsByTitle(title).observe(getViewLifecycleOwner(), topicFlashCardEntities -> {
            flashCardList.clear();
            if (topicFlashCardEntities != null) {
                flashCardList.addAll(topicFlashCardEntities);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(int position) {
        this.position = position; // Store the selected position
        // Trigger navigation to the topic
        vocabularyFragmentViewModel.navigateToTopic();
    }
}
