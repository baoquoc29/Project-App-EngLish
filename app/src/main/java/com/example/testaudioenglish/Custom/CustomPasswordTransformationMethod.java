package com.example.testaudioenglish.Custom;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class CustomPasswordTransformationMethod extends PasswordTransformationMethod {
    private String correctAnswer;

    // Constructor nhận đáp án đúng
    public CustomPasswordTransformationMethod(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence source;

        public PasswordCharSequence(CharSequence source) {
            this.source = source;
        }

        @Override
        public char charAt(int index) {
            // Luôn hiển thị khoảng trắng
            if (source.charAt(index) == ' ') {
                return ' ';
            }
            if(source.charAt(index) == ','){
                return source.charAt(index);
            }
            if (index < correctAnswer.length() && String.valueOf(source.charAt(index)).equalsIgnoreCase(""+correctAnswer.charAt(index))) {
                return source.charAt(index);
            }
            else {
                return '*';
            }
        }

        @Override
        public int length() {
            return source.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return source.subSequence(start, end);
        }
    }
}
