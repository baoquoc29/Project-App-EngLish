package com.example.testaudioenglish.Model;

public class Answer {
    private int pos;
    private String answer;

    public Answer(int pos, String answer) {
        this.pos = pos;
        this.answer = answer;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "pos=" + pos +
                ", answer='" + answer + '\'' +
                '}';
    }
}
