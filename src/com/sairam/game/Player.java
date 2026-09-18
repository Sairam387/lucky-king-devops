package com.sairam.game;

public class Player {

    private String name;
    private int credits;

    public Player(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public void addCredits(int amount) {
        credits += amount;
    }

    public void removeCredits(int amount) {
        credits -= amount;
    }
}