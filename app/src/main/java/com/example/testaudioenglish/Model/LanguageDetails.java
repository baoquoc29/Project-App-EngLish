package com.example.testaudioenglish.Model;

import java.util.ArrayList;
import java.util.List;

public class LanguageDetails {
    private String word;
    private String language;
    private String proficiency;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public List<Pronunciations> getPronunciations() {
        return pronunciations;
    }

    public void setPronunciations(List<Pronunciations> pronunciations) {
        this.pronunciations = pronunciations;
    }

    public List<Translations> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translations> translations) {
        this.translations = translations;
    }

    private List<Pronunciations> pronunciations = new ArrayList<>();

    private List<Translations> translations = new ArrayList<>();
}
