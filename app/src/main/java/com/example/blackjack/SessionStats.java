package com.example.blackjack;

public final class SessionStats {

    private static int wins;
    private static int losses;
    private static int draws;

    private SessionStats() {
    }

    public static void register(String result) {

        if ("WIN".equals(result)) {
            wins++;
        } else if ("LOSE".equals(result)) {
            losses++;
        } else if ("DRAW".equals(result)) {
            draws++;
        }
    }

    public static int getWins() {
        return wins;
    }

    public static int getLosses() {
        return losses;
    }

    public static int getDraws() {
        return draws;
    }
}
