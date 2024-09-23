package com.example.testaudioenglish.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.testaudioenglish.Adapter.ViewPagerToeicAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnPartCompleteListener;
import com.example.testaudioenglish.Model.Answer;
import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.View.ToeicView.ListeningToeicFragment;
import com.example.testaudioenglish.viewmodel.ExamToeicViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExamToeicActivity extends AppCompatActivity implements OnPartCompleteListener {
    private ExamToeicViewModel examToeicViewModel;
    private ViewPager2 viewPager2;
    private TextView timerText;
    private CountDownTimer countDownTimer;
    private TextView correct;
    private int index;
    private List<Answer> list = new ArrayList<>();
    private HashMap<Integer,String> hashAnswer = new HashMap<>();
    private static final int BASE_INDEX = 101;
    private int indexCorrect = 0;
    private AppCompatButton button;
    private boolean checkFinish = false;
    private long idTopic;
    private  BottomNavigationView bottomNavigationView;
    private int totalListening = 0;
    private int totalReading = 0 ;

    private int correctAnswerPart1 = 0;
    private int correctAnswerPart2 = 0;
    private int correctAnswerPart3 = 0;
    private int correctAnswerPart4 = 0;

    private int correctAnswerPart5 = 0;

    private Button checkRes;
    private int correctAnswerPart6 = 0;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    private int correctAnswerPart7 = 0;
    private String test;
    private boolean setCheckFragment = false;
    public int getCorrectAnswerPart1() {
        return correctAnswerPart1;
    }

    public void setCorrectAnswerPart1(int correctAnswerPart1) {
        this.correctAnswerPart1 = correctAnswerPart1;
    }

    public int getCorrectAnswerPart2() {
        return correctAnswerPart2;
    }

    public void setCorrectAnswerPart2(int correctAnswerPart2) {
        this.correctAnswerPart2 = correctAnswerPart2;
    }

    public Button getCheckRes() {
        return checkRes;
    }

    public void setCheckRes(Button checkRes) {
        this.checkRes = checkRes;
    }

    public int getCorrectAnswerPart3() {
        return correctAnswerPart3;
    }

    public void setCorrectAnswerPart3(int correctAnswerPart3) {
        this.correctAnswerPart3 = correctAnswerPart3;
    }

    public int getCorrectAnswerPart4() {
        return correctAnswerPart4;
    }

    public void setCorrectAnswerPart4(int correctAnswerPart4) {
        this.correctAnswerPart4 = correctAnswerPart4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exam_toeic2);

        examToeicViewModel = new ViewModelProvider(this).get(ExamToeicViewModel.class);
        viewPager2 = findViewById(R.id.viewpager);
        timerText = findViewById(R.id.timer);
        button = findViewById(R.id.button);
        checkRes = findViewById(R.id.buttonCheck);
        setUpViewPager();
        setUpBottomNavigationView();
        Intent intent = getIntent();
        examToeicViewModel.getSelectedItemId().observe(this, this::handleNavigation);
         bottomNavigationView = findViewById(R.id.bottom_navigation);
         idTopic = intent.getLongExtra("value",-1);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            examToeicViewModel.selectItem(item.getItemId());
            return true;
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.part1);
                        button.setVisibility(View.GONE);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.part5);
                        button.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.part6);
                        button.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.part7);
                        button.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        setupToolbar();
        // Attach fragment and set the listener
        ListeningToeicFragment fragment = new ListeningToeicFragment();
        fragment.setOnPartCompleteListener(this);
        setUpHashMap();
        clickSuccess();

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
    public long getIdTopic() {
        return idTopic;
    }

    public boolean isCheckFinish() {
        return checkFinish;
    }

    public void setCheckFinish(boolean checkFinish) {
        this.checkFinish = checkFinish;
    }

    public void clickSuccess() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog to confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamToeicActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn nộp bài không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            setPoint(); // Ensure this method is safe to call here
                            // Extract time from timerText
                            String timerTextStr = timerText.getText().toString();
                            String[] parts = timerTextStr.split(":");
                            int minutesElapsed = Integer.parseInt(parts[0]);
                            int secondsElapsed = Integer.parseInt(parts[1]);

                            // Convert elapsed time to seconds
                            int totalSecondsElapsed = (minutesElapsed * 60) + secondsElapsed;

                            // Total available time in seconds (120 minutes)
                            int totalSecondsAvailable = 120 * 60;

                            // Calculate remaining time in seconds
                            int totalSecondsRemaining = totalSecondsAvailable - totalSecondsElapsed;

                            // Convert remaining time back to "MM:SS"
                            int remainingMinutes = totalSecondsRemaining / 60;
                            int remainingSeconds = totalSecondsRemaining % 60;
                            String remainingTimeStr = String.format("%02d:%02d", remainingMinutes, remainingSeconds);

                            // Prepare the intent
                            Intent intent = new Intent(ExamToeicActivity.this, LayoutFinishActivity.class);
                            intent.putExtra("point", indexCorrect + "");
                            intent.putExtra("time", remainingTimeStr); // Use remainingTimeStr here
                            intent.putExtra("pointListening", totalListening + "");
                            intent.putExtra("pointReading", (indexCorrect - totalListening) + "");
                            intent.putExtra("idTopic", idTopic + "");
                            intent.putExtra("correctAnswerPart1",correctAnswerPart1);
                            intent.putExtra("correctAnswerPart2",correctAnswerPart2);
                            intent.putExtra("correctAnswerPart3",correctAnswerPart3);
                            intent.putExtra("correctAnswerPart4",correctAnswerPart4);
                            intent.putExtra("correctAnswerPart5",correctAnswerPart5);
                            intent.putExtra("correctAnswerPart6",correctAnswerPart6);
                            intent.putExtra("correctAnswerPart7",correctAnswerPart7);
                            // Get the customer ID from SharedPreferences
                            SharedPreferences userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                            Long idCustomer = userPrefs.getLong("idCustomer", 1L);
                            intent.putExtra("idCustomer", idCustomer);

                            // Format the date
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String formattedDate = LocalDate.now().format(formatter);

                            // Create the UserScoreModel
                            UserScoreModel userScoreModel = new UserScoreModel(idCustomer, idTopic, indexCorrect, remainingTimeStr, formattedDate, Long.valueOf(totalListening), Long.valueOf(totalReading));

                            // Handle the UserScoreModel
                            examToeicViewModel.setUpToCompeleteExam(userScoreModel);

                            // Start the new activity
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Log.e("ExamToeicActivity", "Error processing exam completion", e);
                            // Show an error message to the user if necessary
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }



    private void setUpViewPager() {
        ViewPagerToeicAdapter adapter = new ViewPagerToeicAdapter(this);
        viewPager2.setAdapter(adapter);
    }

    private void setUpBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            examToeicViewModel.selectItem(item.getItemId());
            return true;
        });
    }

    private void startCountDownTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String time = String.format("%02d:%02d", minutes, seconds);
                timerText.setText(time);
            }

            public void onFinish() {
                timerText.setText("00:00");
                Toast.makeText(ExamToeicActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                // Handle what happens when time is up
            }
        }.start();
    }
    public void setTimer(){
        startCountDownTimer(120 * 60 * 1000); // 1 hour 15 minutes in milliseconds
    }

    public int getTotalListening() {
        return totalListening;
    }

    public void setTotalListening(int totalListening) {
        this.totalListening = totalListening;
    }

    public int getTotalReading() {
        return totalReading;
    }

    public void setTotalReading(int totalReading) {
        this.totalReading = totalReading;
    }

    private void handleNavigation(int itemId) {
        if(itemId == R.id.part1){
            viewPager2.setUserInputEnabled(false);
        }
         else if (itemId == R.id.part5 && viewPager2.getCurrentItem() != 0) {
            viewPager2.setCurrentItem(1);
            setCheckFragment = true;
            viewPager2.setUserInputEnabled(false);
        } else if (itemId == R.id.part6 && viewPager2.getCurrentItem() != 0) {
            viewPager2.setCurrentItem(2);
            viewPager2.setUserInputEnabled(false);
        } else if (itemId == R.id.part7 && viewPager2.getCurrentItem() != 0) {
            viewPager2.setCurrentItem(3);
            viewPager2.setUserInputEnabled(false);
        }
    }

    @Override
    public void onPartComplete() {
        runOnUiThread(() -> {
            viewPager2.setCurrentItem(1);
            viewPager2.setUserInputEnabled(false);
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void setUpAnswer(List<Answer> listAnswer){
        list.addAll(listAnswer);
    }
    public void setUpHashMap(){
        for(int i = 0;i<BASE_INDEX;i++){
            hashAnswer.put(BASE_INDEX + 0,"");
        }
    }
    public void pushUpHashMap(int key,String value){
        hashAnswer.put(key,value);
    }
    public void setPoint(){
        for(int i = 0;i<list.size();i++){
            if(list.get(i).getAnswer().equals(hashAnswer.get(i + BASE_INDEX))){
                if(i + BASE_INDEX > 100 || i + BASE_INDEX < 131){
                    correctAnswerPart5++;
                }
                else if(i + BASE_INDEX > 130 || i + BASE_INDEX < 146){
                    correctAnswerPart6++;
                }
                else{
                    correctAnswerPart7++;
                }
                ++indexCorrect;
            }
        }
    }
    public void updatedCorrect(int indexPush){
        indexCorrect = indexCorrect + indexPush;
    }

    public void setVisibleBottomNavigation(){
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
