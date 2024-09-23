package com.example.testaudioenglish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testaudioenglish.R;

public class LayoutFinishActivity extends AppCompatActivity {
    private TextView tvScore, tvTimer, tvTotalListening, tvTotalReading;
    private SeekBar[] seekBars = new SeekBar[7];
    private static final int[] MAX_VALUES = {6, 25, 39, 30, 30, 16, 54};
    private int countPercent[]  = new int[7];
    private TextView[] tvParts = new TextView[7];
    private TextView[] tvPartPercent = new TextView[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_finish);
        setupToolbar();
        initializeViews();
        handleIntentData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initializeViews() {
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTime);
        tvTotalListening = findViewById(R.id.tvListeningCorrect);
        tvTotalReading = findViewById(R.id.tvReadingCorrect);
        for (int i = 0; i < seekBars.length; i++) {
            int seekBarId = getResources().getIdentifier("seekBar" + (i + 1), "id", getPackageName());
            int textViewId = getResources().getIdentifier("tvPart" + (i+1) , "id",getPackageName());
            int textViewPercentId = getResources().getIdentifier("tvPart" + (i+1) + "Percent" , "id",getPackageName());
            tvParts[i] = findViewById(textViewId);
            tvPartPercent[i] = findViewById(textViewPercentId);
            seekBars[i] = findViewById(seekBarId);
            seekBars[i].setMax(MAX_VALUES[i]);
            seekBars[i].setEnabled(false);
        }
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        String score = intent.getStringExtra("point");
        String time = intent.getStringExtra("time");
        String totalListening = intent.getStringExtra("pointListening");
        String totalReading = intent.getStringExtra("pointReading");

        int[] correctAnswers = new int[7];
        for (int i = 0; i < correctAnswers.length; i++) {
            correctAnswers[i] = intent.getIntExtra("correctAnswerPart" + (i + 1), 0);
            seekBars[i].setProgress(correctAnswers[i]);
            countPercent[i] = (int) ((correctAnswers[i] / (float) MAX_VALUES[i]) * 100);

        }
        for (int i = 0; i < tvParts.length; i++) {
            if (tvParts[i] != null) {
                tvParts[i].setText("Part " + (i + 1) + ": (" + correctAnswers[i] + "/" + MAX_VALUES[i] + ")");
            }
            if (tvPartPercent[i] != null) {
                tvPartPercent[i].setText(countPercent[i] + "%");
            }
        }
        if (score == null) {
            tvScore.setText("0");
            tvTotalListening.setText("Bài nghe: 0");
            tvTotalReading.setText("Bài đọc: 0");
        } else {
            tvScore.setText(score);
            tvTotalListening.setText("Bài nghe: " + totalListening);
            tvTotalReading.setText("Bài đọc: " + totalReading);
        }

        tvTimer.setText("Thời gian hoàn thành " + (time != null ? time : "0"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }
}
