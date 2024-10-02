package com.example.testaudioenglish.Model;

public class Translations {
    private String translation;
    private double confidence;

    public String getTranslation() {
        return translation;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getPosTag() {
        return posTag;
    }

    private String posTag;
}
