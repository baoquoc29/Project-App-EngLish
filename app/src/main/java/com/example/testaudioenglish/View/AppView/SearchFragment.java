package com.example.testaudioenglish.View.AppView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Activity.SearchDetailsActivity;
import com.example.testaudioenglish.Adapter.SearchHistoryAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.InterfaceAdapter.OnItemMoveClicked;
import com.example.testaudioenglish.Model.HistoryModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.FragmentSearchBinding;
import com.example.testaudioenglish.viewmodel.SearchViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment  {
    private SearchViewModel searchViewModel;
    private SearchView searchView;
    private List<HistoryModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchHistoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FragmentSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.setViewModel(searchViewModel);
        binding.setLifecycleOwner(this);

        searchView = binding.searchView;
        recyclerView = binding.recyclerViewHistory;
        setupSearchView();

        return binding.getRoot();
    }

    private void setupSearchView() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                openNewActivity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private List<HistoryModel> loadVocabularyList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("VocabularyPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("vocabulary_list", null);

        Type type = new TypeToken<ArrayList<HistoryModel>>() {}.getType();
        list = gson.fromJson(json, type);

        if (list == null) {
            list = new ArrayList<>();
        }
         adapter = new SearchHistoryAdapter(list, new OnItemClickListener() {
             @Override
             public void onItemClick(int position) {
                 removeVocabulary(list.get(position).getWord());
                 list.remove(position);
                 adapter.notifyDataSetChanged();
             }
         }, new OnItemMoveClicked() {
             @Override
             public void onItemClick(int position) {
                 Intent intent = new Intent(getActivity(), SearchDetailsActivity.class);
                 intent.putExtra("search_query", list.get(position).getWord());
                 startActivity(intent);
             }
         });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return list;
    }
    private void openNewActivity(String query) {
        Intent intent = new Intent(getActivity(), SearchDetailsActivity.class);
        intent.putExtra("search_query", query);
        startActivity(intent);
    }
    private void removeVocabulary(String wordToRemove) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("VocabularyPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // Lấy danh sách từ SharedPreferences
        String json = sharedPreferences.getString("vocabulary_list", null);
        Type type = new TypeToken<ArrayList<HistoryModel>>() {}.getType();
        List<HistoryModel> vocabularyList = gson.fromJson(json, type);

        if (vocabularyList != null) {
            // Tìm và xóa từ vựng
            for (int i = 0; i < vocabularyList.size(); i++) {
                if (vocabularyList.get(i).getWord().equals(wordToRemove)) {
                    vocabularyList.remove(i);
                    break;
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String updatedJson = gson.toJson(vocabularyList);
            editor.putString("vocabulary_list", updatedJson);
            editor.apply(); // Áp dụng thay đổi
        }
    }

    // cap nhat ngay khi back
    @Override
    public void onResume() {
        super.onResume();
        loadVocabularyList();
    }

}
