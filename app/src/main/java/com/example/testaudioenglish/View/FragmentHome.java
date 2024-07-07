package com.example.testaudioenglish.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.HomeFragmentViewModel;

public class FragmentHome extends Fragment {
    private HomeFragmentViewModel homeFragmentViewModel;
    private TextView us;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        us = view.findViewById(R.id.us);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

       us.setText("Welcome, " + storedUsername);

        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
    }
}
