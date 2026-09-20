package com.example.blackjack;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class BlackjackGameTest {

    @Test
    public void aceAndKingEquals21() throws Exception {
        BlackjackGame game = new BlackjackGame();

        Method method = BlackjackGame.class.getDeclaredMethod(
                "calculatePoints",
                List.class
        );

        method.setAccessible(true);

        int points = (int) method.invoke(
                game,
                Arrays.asList("A♠", "K♥")
        );

        assertEquals(21, points);
    }

    @Test
    public void aceCanBecomeOne() throws Exception {
        BlackjackGame game = new BlackjackGame();

        Method method = BlackjackGame.class.getDeclaredMethod(
                "calculatePoints",
                List.class
        );

        method.setAccessible(true);

        int points = (int) method.invoke(
                game,
                Arrays.asList("A♠", "9♥", "5♦")
        );

        assertEquals(15, points);
    }

    @Test
    public void faceCardsAreWorthTen() throws Exception {
        BlackjackGame game = new BlackjackGame();

        Method method = BlackjackGame.class.getDeclaredMethod(
                "calculatePoints",
                List.class
        );

        method.setAccessible(true);

        int points = (int) method.invoke(
                game,
                Arrays.asList("Q♠", "J♥")
        );

        assertEquals(20, points);
    }
}