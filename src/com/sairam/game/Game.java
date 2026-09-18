package com.sairam.game;

import java.util.Random;

/**
 * Lucky King game engine.
 * Generates a random number from 0 to 9 and its corresponding color.
 *
 * Color rules used by GameUi:
 * 0       -> YELLOW
 * 2,4,6,8 -> RED
 * 1,3,5,7,9 -> BLUE
 */
public class Game {

    private static final int MIN_NUMBER = 0;
    private static final int MAX_NUMBER = 9;

    private final Random random;

    public Game() {
        random = new Random();
    }

    /**
     * Generates one complete game result.
     * This method always returns a non-null Result.
     */
    public Result generateResult() {
        int number = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;
        String color = getColorForNumber(number);
        return new Result(number, color);
    }

    private String getColorForNumber(int number) {
        if (number == 0) {
            return "YELLOW";
        }

        if (number % 2 == 0) {
            return "RED";
        }

        return "BLUE";
    }

    public int getMinNumber() {
        return MIN_NUMBER;
    }

    public int getMaxNumber() {
        return MAX_NUMBER;
    }
}
