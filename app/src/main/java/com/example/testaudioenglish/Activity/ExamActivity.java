package com.example.testaudioenglish.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.View.CardPairingFragment;
import com.example.testaudioenglish.View.MemoryCardFragment;
import com.example.testaudioenglish.View.MultipleChoiceFragment;

public class ExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        long idTopic = getIntent().getLongExtra("idTopic", -1);
        String game = getIntent().getStringExtra("game");
        if (savedInstanceState == null) {
            // Check if the fragment is already added
            CardPairingFragment fragmentCardPairing = (CardPairingFragment) getSupportFragmentManager().findFragmentByTag(CardPairingFragment.class.getSimpleName());
            MemoryCardFragment fragmentMemoryCard = (MemoryCardFragment) getSupportFragmentManager().findFragmentByTag(MemoryCardFragment.class.getSimpleName());
            MultipleChoiceFragment fragmentMultipleChoice = (MultipleChoiceFragment) getSupportFragmentManager().findFragmentByTag(MultipleChoiceFragment.class.getSimpleName());
            if (fragmentCardPairing == null && game.equals("Pairing")) {
                // Create new Fragment and pass data
                fragmentCardPairing = CardPairingFragment.newInstance(idTopic);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragmentCardPairing, CardPairingFragment.class.getSimpleName())
                        .commit();
            }
            else if(fragmentMemoryCard == null &&  game.equals("Memory")){
                fragmentMemoryCard = MemoryCardFragment.newInstance(idTopic);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragmentMemoryCard, MemoryCardFragment.class.getSimpleName())
                        .commit();
            }
            else if(fragmentMultipleChoice == null && game.equals("Choice")){
                fragmentMultipleChoice = fragmentMultipleChoice.newInstance(idTopic);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragmentMultipleChoice, MultipleChoiceFragment.class.getSimpleName())
                        .commit();
            }
        }
    }
}
