package com.example.testaudioenglish.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.View.FlashCardView.CardPairingFragment;
import com.example.testaudioenglish.View.FlashCardView.ExamFragment;
import com.example.testaudioenglish.View.FlashCardView.MemoryCardFragment;
import com.example.testaudioenglish.View.FlashCardView.MultipleChoiceFragment;

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
            ExamFragment examFragment = (ExamFragment) getSupportFragmentManager().findFragmentByTag(ExamFragment.class.getSimpleName());
            if (fragmentCardPairing == null && game.equals("Pairing")) {
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
            else if(examFragment == null && game.equals("Exam")){
                examFragment = examFragment.newInstance(idTopic);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, examFragment, ExamFragment.class.getSimpleName())
                        .commit();
            }
        }
    }
}
