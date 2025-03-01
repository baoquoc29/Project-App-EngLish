package com.example.testaudioenglish.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testaudioenglish.View.AppView.AccountFragment;
import com.example.testaudioenglish.View.AppView.FragmentHome;
import com.example.testaudioenglish.View.AppView.NotificationFragment;
import com.example.testaudioenglish.View.AppView.SearchFragment;
import com.example.testaudioenglish.View.AppView.VocabularyFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentHome();
            case 1:
                return new VocabularyFragment();
            case 2:
                return new SearchFragment();
            case 3:
                return new NotificationFragment();
            case 4:
                return new AccountFragment();
            default:
                return new FragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
