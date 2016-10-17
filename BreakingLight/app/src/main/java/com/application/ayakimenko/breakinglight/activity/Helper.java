package com.application.ayakimenko.breakinglight.activity;

public class Helper {

    public static String getLanguageShortName(int languageId) {
        switch (languageId) {
            case 0:
                return "en";
            case 1:
                return "ru";
            default:
                return "en";
        }
    }
}
