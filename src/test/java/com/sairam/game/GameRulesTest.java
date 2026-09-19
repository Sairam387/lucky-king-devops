package com.sairam.game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameRulesTest {

    private static final int MIN_BET = 10;
    private static final int MAX_BET = 100000;

    @Test
    void shouldAcceptMinimumBet() {
        int betAmount = 10;
        boolean valid = betAmount >= MIN_BET && betAmount <= MAX_BET;
        assertTrue(valid);
    }

    @Test
    void shouldAcceptMaximumBet() {
        int betAmount = 100000;
        boolean valid = betAmount >= MIN_BET && betAmount <= MAX_BET;
        assertTrue(valid);
    }

    @Test
    void shouldRejectBetBelowMinimum() {
        int betAmount = 9;
        boolean valid = betAmount >= MIN_BET && betAmount <= MAX_BET;
        assertFalse(valid);
    }

    @Test
    void shouldRejectBetAboveMaximum() {
        int betAmount = 100001;
        boolean valid = betAmount >= MIN_BET && betAmount <= MAX_BET;
        assertFalse(valid);
    }
}
