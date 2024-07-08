package com.example.testaudioenglish.Adapter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testaudioenglish.R;
import com.example.testaudioenglish.SortClicked;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {
    private SortClicked mListener;

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
        return inflater.inflate(R.layout.customsort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView natureSortText = view.findViewById(R.id.natureSortText);
        TextView alphabetSortText = view.findViewById(R.id.alphabetSortText);
        ImageView natureSortTick = view.findViewById(R.id.NatureSortTick);
        ImageView alphabetSortTick = view.findViewById(R.id.alphabetSortTick);

        natureSortText.setOnClickListener(v -> {
            natureSortTick.setVisibility(View.VISIBLE);
            alphabetSortTick.setVisibility(View.GONE);
            if (mListener != null) {
                mListener.onNatureSortSelected();
            }
        });

        alphabetSortText.setOnClickListener(v -> {
            natureSortTick.setVisibility(View.GONE);
            alphabetSortTick.setVisibility(View.VISIBLE);
            if (mListener != null) {
                mListener.onAlphabetSortSelected();
            }
        });
    }
}
