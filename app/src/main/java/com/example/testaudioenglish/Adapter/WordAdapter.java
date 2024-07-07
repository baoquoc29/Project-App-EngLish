package com.example.testaudioenglish.Adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Model.WordFlashCard;
import com.example.testaudioenglish.R;

import java.util.List;

public class WordAdapter extends PagerAdapter {
    private Context context;
    private List<FlashCardEntity> list;

    // Constructor to initialize context and list
    public WordAdapter(Context context, List<FlashCardEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Inflate the layout for the flashcard
        View view = LayoutInflater.from(context).inflate(R.layout.custom_flash_card_double, container, false);
        FlashCardEntity wordFlashCard = list.get(position);
        TextView eng_ver = view.findViewById(R.id.tv_english);
        TextView viet_ver = view.findViewById(R.id.tv_viet);
        CardView cardView = view.findViewById(R.id.card_view);
        eng_ver.setText(wordFlashCard.getEnglishWord());
        viet_ver.setText(wordFlashCard.getVietWord());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.transaction);
                animatorSet.setTarget(view);
                animatorSet.start();
                if(viet_ver.getVisibility() == View.GONE){
                    viet_ver.setVisibility(View.VISIBLE);
                    eng_ver.setVisibility(View.GONE);
                }
                else{
                    viet_ver.setVisibility(View.GONE);
                    eng_ver.setVisibility(View.VISIBLE);
                }
            }
        });
        viet_ver.setVisibility(View.GONE);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
