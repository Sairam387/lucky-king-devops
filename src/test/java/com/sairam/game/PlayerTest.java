package com.sairam.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void shouldReturnPlayerName() {
        Player player = new Player("Sairam", 1000);

        assertEquals("Sairam", player.getName());
    }

    @Test
    void shouldReturnInitialCredits() {
        Player player = new Player("Sairam", 1000);

        assertEquals(1000, player.getCredits());
    }

    @Test
    void shouldAddCredits() {
        Player player = new Player("Sairam", 1000);

        player.addCredits(500);

        assertEquals(1500, player.getCredits());
    }

    @Test
    void shouldRemoveCredits() {
        Player player = new Player("Sairam", 1000);

        player.removeCredits(300);

        assertEquals(700, player.getCredits());
    }
}
