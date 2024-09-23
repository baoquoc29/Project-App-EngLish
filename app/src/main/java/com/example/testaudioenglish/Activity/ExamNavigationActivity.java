package com.example.testaudioenglish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.EachPartAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.EachPartModel;
import com.example.testaudioenglish.R;

import java.util.ArrayList;
import java.util.List;

public class ExamNavigationActivity extends AppCompatActivity implements OnItemClickListener {
    private List<EachPartModel> list = new ArrayList<>();
    private EachPartAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exam_each_part);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recylePart); // Make sure this ID matches your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupToolbar();
        adapter = new EachPartAdapter(list,this);
        recyclerView.setAdapter(adapter);
        loadList();

    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
        }
    }

    private void loadList() {
        // Add items to the list
        list.add(new EachPartModel("Part 1: Hình ảnh", "Bài nghe", 1));
        list.add(new EachPartModel("Part 2: Hỏi đáp",  "Bài nghe", 1));
        list.add(new EachPartModel("Part 3: Hội thoại ngắn", "Bài nghe",1));
        list.add(new EachPartModel("Part 4: Đoạn thông tin ngắn",  "Bài nghe", 1));
        list.add(new EachPartModel("Part 5: Ngữ pháp",  "Bài đọc", 1));
        list.add(new EachPartModel("Part 6: Hoàn thành đoạn văn", "Bài đọc", 1));
        list.add(new EachPartModel("Part 7: Đọc hiểu", "Bài đọc", 1));
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(int position) {
        // Handle item click
        EachPartModel clickedPart = list.get(position);
        Intent intent = new Intent(this, ManagementExamAnyPartActivity.class);
        intent.putExtra("part", clickedPart.getTitle());
        intent.putExtra("value",Long.valueOf(1));
        startActivity(intent);
    }
}

