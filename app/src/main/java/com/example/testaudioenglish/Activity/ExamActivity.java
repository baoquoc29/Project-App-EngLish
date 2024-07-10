package com.example.testaudioenglish.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.View.CardPairingFragment;

public class ExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        // Receive data from Intent
        long idTopic = getIntent().getLongExtra("idTopic", -1);

        if (savedInstanceState == null) {
            // Create new Fragment and pass data
            CardPairingFragment fragment = CardPairingFragment.newInstance(idTopic);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
