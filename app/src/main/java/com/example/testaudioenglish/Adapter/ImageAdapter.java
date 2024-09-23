package com.example.testaudioenglish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.R;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private List<String> list;  // Assuming list contains image URLs

    // Constructor to initialize context and list
    public ImageAdapter(Context context, List<String> list) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.customimage, container, false);

        // Find views in the layout
        ImageView imageView = view.findViewById(R.id.image_list);


        String imageUrl = list.get(position);


        Glide.with(context)
                .load(imageUrl)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
