package com.example.testaudioenglish.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testaudioenglish.Adapter.ViewPagerAdapter;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        viewPager2 = findViewById(R.id.viewpager);
        setUpViewPager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mainActivityViewModel.selectItem(item.getItemId());
                return true;
            }
        });

        mainActivityViewModel.getSelectedItemId().observe(this, itemId -> {
            if (itemId != null) {
                handleNavigation(itemId);
            }
        });

        // Sync ViewPager2 with BottomNavigationView
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.homeNavigate);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.flashcardNavigate);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.searchNavigate);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.notificationNavigate);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.accountNavigate);
                        break;
                }
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
    }
    private void handleNavigation(int itemId) {
        if(itemId == R.id.homeNavigate){
            viewPager2.setCurrentItem(0);
        }
        if(itemId == R.id.searchNavigate){
            viewPager2.setCurrentItem(2);
        }
        if(itemId == R.id.accountNavigate){
            viewPager2.setCurrentItem(4);

        }
        if(itemId == R.id.flashcardNavigate){
            viewPager2.setCurrentItem(1);

        }
        if(itemId == R.id.notificationNavigate){
            viewPager2.setCurrentItem(3);

        }
    }

}
