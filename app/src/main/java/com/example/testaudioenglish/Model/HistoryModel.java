package com.example.testaudioenglish.Model;

public class HistoryModel {
    private String word;
    private String pronunciation;
    private String meaning;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HistoryModel)) return false;
        HistoryModel that = (HistoryModel) obj;
        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return word.hashCode(); // Tính toán mã băm dựa trên từ
    }
    public HistoryModel(String word, String pronunciation, String meaning) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }
}
