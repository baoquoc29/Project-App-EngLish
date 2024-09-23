package com.example.testaudioenglish.View.AppView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

import com.example.testaudioenglish.Activity.AddFlashCardActivity;
import com.example.testaudioenglish.Activity.LearningActivity;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.WordFlashCard;
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

        // Thiết lập RecyclerView
        recyclerView = binding.recyclerviewDocument;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search = binding.search;
        flashCardList = new ArrayList<>();
        adapter = new TopicFlashCardAdapter(flashCardList, new TopicFlashCardRepository(requireActivity().getApplication()), this);
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu flashcards
        vocabularyFragmentViewModel.getFlashCardsByUsername(getUserNameSharedPreferences()).observe(getViewLifecycleOwner(), flashCards -> {
            flashCardList.clear();
            if (flashCards != null) {
                flashCardList.addAll(flashCards);
            }
            adapter.notifyDataSetChanged(); // Cập nhật Adapter khi dữ liệu thay đổi
        });

        // Sự kiện điều hướng tới màn hình AddFlashCardActivity
        vocabularyFragmentViewModel.navigateToAddFlashCard.observe(getViewLifecycleOwner(), navigate -> {
            Boolean shouldNavigate = navigate.getContentIfNotHandled();
            if (shouldNavigate != null && shouldNavigate) {
                Intent intent = new Intent(getActivity(), AddFlashCardActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện điều hướng tới LearningActivity hoặc AddFlashCardActivity
        vocabularyFragmentViewModel.navigateToTopic.observe(getViewLifecycleOwner(), navigate -> {
            Boolean shouldNavigate = navigate.getContentIfNotHandled();
            if (shouldNavigate != null && shouldNavigate && position >= 0 && position < flashCardList.size()) {
                TopicFlashCardEntity selectedCard = flashCardList.get(position);
                if (selectedCard.getStatus() == 1) {
                    Intent intent = new Intent(getActivity(), LearningActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("value", selectedCard.getId());
                    vocabularyFragmentViewModel.getCount(selectedCard.getId()).observe(getViewLifecycleOwner(), count -> {
                        if (count != null) {
                            intent.putExtra("totalWord", String.valueOf(count));
                            intent.putExtra("title", selectedCard.getTitle());
                            intent.putExtra("name", selectedCard.getUsername());
                            startActivity(intent);
                        }
                    });
                } else {
                    Intent intent = new Intent(getActivity(), AddFlashCardActivity.class);
                    intent.putExtra("status", "unfinish");
                    intent.putExtra("value", selectedCard.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getContext(), "Không thể điều hướng, phần tử không tồn tại", Toast.LENGTH_SHORT).show();
            }
        });


        // Bộ lọc tìm kiếm
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không làm gì
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không làm gì
            }
        });

        return binding.getRoot();
    }

    // Hàm lọc dữ liệu dựa trên tiêu đề
    public void filterData(String title) {
        vocabularyFragmentViewModel.getFlashCardsByTitle(title, getUserNameSharedPreferences()).observe(getViewLifecycleOwner(), topicFlashCardEntities -> {
            flashCardList.clear();
            if (topicFlashCardEntities != null) {
                flashCardList.addAll(topicFlashCardEntities);
            }
            adapter.notifyDataSetChanged();
        });
    }

    // Lấy username từ SharedPreferences
    public String getUserNameSharedPreferences() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString("username", null); // Trả về null nếu không có username
    }

    // Xử lý sự kiện click item trong RecyclerView
    @Override
    public void onItemClick(int position) {
        if (position >= 0 && position < flashCardList.size()) {
            this.position = position; // Lưu vị trí của phần tử được chọn
            vocabularyFragmentViewModel.navigateToTopic(); // Kích hoạt điều hướng tới topic
        } else {
            Toast.makeText(getContext(), "Phần tử không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }
}
