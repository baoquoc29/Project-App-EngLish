package com.example.testaudioenglish.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.testaudioenglish.R;
import com.example.testaudioenglish.View.ExamAnyPartView.IncompleteSentencesFragment;
import com.example.testaudioenglish.View.ExamAnyPartView.PictureListeningFragment;
import com.example.testaudioenglish.View.ExamAnyPartView.QuestionListeningFragment;
import com.example.testaudioenglish.View.ExamAnyPartView.ReadingPart7Fragment;
import com.example.testaudioenglish.View.ExamAnyPartView.ReadingPartSixFragment;
import com.example.testaudioenglish.View.ExamAnyPartView.ShortConversationFragment;

public class ManagementExamAnyPartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_exam_any_part);

        Intent intent = getIntent();
        String part = intent.getStringExtra("part");

        Fragment fragment = null;
        String fragmentTag = null;

        if (part != null) {
            switch (part) {
                case "Part 1: Hình ảnh":
                    fragment = (PictureListeningFragment) getSupportFragmentManager()
                            .findFragmentByTag(PictureListeningFragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = PictureListeningFragment.newInstance("param1", "param2");
                        fragmentTag = PictureListeningFragment.class.getSimpleName();
                    }
                    break;
                case "Part 2: Hỏi đáp":
                    fragment = (QuestionListeningFragment) getSupportFragmentManager()
                            .findFragmentByTag(QuestionListeningFragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = QuestionListeningFragment.newInstance("param1", "param2");
                        fragmentTag = QuestionListeningFragment.class.getSimpleName();
                    }
                    break;
                case "Part 3: Hội thoại ngắn":
                    fragment = (ShortConversationFragment) getSupportFragmentManager()
                            .findFragmentByTag(ShortConversationFragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = ShortConversationFragment.newInstance("param1", "param2", "Part 3");
                        fragmentTag = ShortConversationFragment.class.getSimpleName();
                    }
                    break;
                case "Part 4: Đoạn thông tin ngắn":
                    fragment = (ShortConversationFragment) getSupportFragmentManager()
                            .findFragmentByTag(ShortConversationFragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = ShortConversationFragment.newInstance("param1", "param2", "Part 4");
                        fragmentTag = ShortConversationFragment.class.getSimpleName();
                    }
                    break;

                case "Part 5: Ngữ pháp":
                    fragment = (IncompleteSentencesFragment) getSupportFragmentManager()
                            .findFragmentByTag(ShortConversationFragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = IncompleteSentencesFragment.newInstance("param1", "param2", "Part 5");
                        fragmentTag = IncompleteSentencesFragment.class.getSimpleName();
                    }
                    break;
                case "Part 6: Hoàn thành đoạn văn":
                    fragment = (ReadingPartSixFragment) getSupportFragmentManager()
                            .findFragmentByTag(ShortConversationFragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = ReadingPartSixFragment.newInstance("param1", "param2", "Part 6");
                        fragmentTag = ReadingPartSixFragment.class.getSimpleName();
                    }
                    break;

                case "Part 7: Đọc hiểu":
                    fragment = (ReadingPart7Fragment) getSupportFragmentManager()
                            .findFragmentByTag(ReadingPart7Fragment.class.getSimpleName());
                    if (fragment == null) {
                        fragment = ReadingPart7Fragment.newInstance("param1", "param2", "Part 7");
                        fragmentTag = ReadingPart7Fragment.class.getSimpleName();
                    }
                    break;
            }

            if (fragment != null && fragmentTag != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_exam, fragment, fragmentTag);
                transaction.commit();
            }
        }
    }
}
