package com.application.ayakimenko.breakinglight.beans;


public class BestPlayer {
    private String name;
    private int score;

    public BestPlayer() {
    }

    public BestPlayer(String name, int userScore) {
        this.name = name;
        this.score = userScore;
    }

    public String getDisplayName() {
        if (name == null) {
            return "";
        } else {
            return name + ": " + score;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "BestPlayer{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
