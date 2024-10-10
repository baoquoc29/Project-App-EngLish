package com.example.testaudioenglish.View.AppView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testaudioenglish.Activity.HistoryExamActivity;
import com.example.testaudioenglish.Activity.LoginActivity;
import com.example.testaudioenglish.Activity.UpdateCustomerActivity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.FragmentAccountBinding;
import com.example.testaudioenglish.viewmodel.AccountFragmentViewModel;

public class AccountFragment extends Fragment {
    private AccountFragmentViewModel accountFragmentViewModel;
    private SharedPreferences sharedPref;
    private SharedPreferences userPrefs;
    private SharedPreferences getId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAccountBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        accountFragmentViewModel = new ViewModelProvider(this).get(AccountFragmentViewModel.class);
        binding.setViewModel(accountFragmentViewModel);
        binding.setLifecycleOwner(this);

        sharedPref = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        userPrefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        getId = requireActivity().getSharedPreferences("GetIDUser",Context.MODE_PRIVATE);
        initializeViewModel();
        setupObservers();

        return binding.getRoot();
    }

    private void initializeViewModel() {
        String storedUsername = sharedPref.getString("username", "");
        Long idCustomer = userPrefs.getLong("idCustomer", 1L);

        accountFragmentViewModel.getTotalExam(idCustomer);
        accountFragmentViewModel.getMaxPoints(idCustomer);
        accountFragmentViewModel.setDateNow();
        accountFragmentViewModel.setUsername(storedUsername);
    }

    private void setupObservers() {
        accountFragmentViewModel.getClickToHistory().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                Intent intent = new Intent(getContext(), HistoryExamActivity.class);
                startActivity(intent);
            }
        });

        accountFragmentViewModel.getClickToLogOut().observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                clearPreferences();
                requireActivity().finish();
            }
        });
        accountFragmentViewModel.getClickToChangeInfor().observe(getViewLifecycleOwner(),clicked->{
            if(clicked){
                Intent intent = new Intent(getContext(), UpdateCustomerActivity.class);
                startActivity(intent);
            }
        });

    }

    private void clearPreferences() {
        sharedPref.edit().clear().apply();
        userPrefs.edit().clear().apply();
        getId.edit().clear().apply();
    }
}
