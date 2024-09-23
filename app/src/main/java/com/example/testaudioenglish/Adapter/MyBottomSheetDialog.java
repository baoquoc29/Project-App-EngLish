package com.example.testaudioenglish.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testaudioenglish.InterfaceAdapter.SortClicked;
import com.example.testaudioenglish.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {
    private SortClicked mListener;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "SortPreferences";
    private static final String SORT_KEY = "sort_key";

    public MyBottomSheetDialog() {
        // Required empty public constructor
    }

    public static MyBottomSheetDialog newInstance(SortClicked listener) {
        MyBottomSheetDialog fragment = new MyBottomSheetDialog();
        fragment.mListener = listener;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            View bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.customshapedialog);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.customsort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView natureSortText = view.findViewById(R.id.natureSortText);
        TextView alphabetSortText = view.findViewById(R.id.alphabetSortText);
        ImageView natureSortTick = view.findViewById(R.id.NatureSortTick);
        ImageView alphabetSortTick = view.findViewById(R.id.alphabetSortTick);


        String sortSelection = sharedPreferences.getString(SORT_KEY, "nature");
        if ("nature".equals(sortSelection)) {
            natureSortTick.setVisibility(View.VISIBLE);
            alphabetSortTick.setVisibility(View.GONE);
        } else if ("alphabet".equals(sortSelection)) {
            natureSortTick.setVisibility(View.GONE);
            alphabetSortTick.setVisibility(View.VISIBLE);
        }
        else if(sortSelection.isEmpty()){
            natureSortTick.setVisibility(View.VISIBLE);
            alphabetSortTick.setVisibility(View.GONE);
        }

        natureSortText.setOnClickListener(v -> {
            sharedPreferences.edit().putString(SORT_KEY, "nature").apply();
            if (mListener != null) {
                mListener.onNatureSortSelected();
            }
        });

        alphabetSortText.setOnClickListener(v -> {
            sharedPreferences.edit().putString(SORT_KEY, "alphabet").apply();
            if (mListener != null) {
                mListener.onAlphabetSortSelected();
            }
        });
    }
}
