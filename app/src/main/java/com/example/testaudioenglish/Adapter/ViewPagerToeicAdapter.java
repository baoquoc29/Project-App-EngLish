package com.example.testaudioenglish.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testaudioenglish.View.ToeicView.ListeningToeicFragment;
import com.example.testaudioenglish.View.ToeicView.MultipleChoiceToeicFragment;
import com.example.testaudioenglish.View.ToeicView.ReadingComprehenFragment;
import com.example.testaudioenglish.View.ToeicView.TextCompletionFragment;

public class ViewPagerToeicAdapter extends FragmentStateAdapter {

    public ViewPagerToeicAdapter(@NonNull FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ListeningToeicFragment();
            case 1:
                return new MultipleChoiceToeicFragment();
            case 2:
                return new TextCompletionFragment();
            case 3:
                return new ReadingComprehenFragment();
            default:
                return new ListeningToeicFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
