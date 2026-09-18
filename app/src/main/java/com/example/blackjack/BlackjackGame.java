package com.example.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackjackGame {

    private final List<String> deck = new ArrayList<>();
    private final List<String> playerHand = new ArrayList<>();
    private final List<String> dealerHand = new ArrayList<>();

    public void startGame() {
        deck.clear();
        playerHand.clear();
        dealerHand.clear();

        createDeck();
        shuffleDeck();

        playerHand.add(drawCard());
        dealerHand.add(drawCard());

        playerHand.add(drawCard());
        dealerHand.add(drawCard());
    }

    private void createDeck() {
        String[] suits = {"♠", "♥", "♦", "♣"};

        String[] values = {
                "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "J", "Q", "K", "A"
        };

        for (String suit : suits) {
            for (String value : values) {
                deck.add(value + suit);
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private String drawCard() {
        if (deck.isEmpty()) {
            return null;
        }

        return deck.remove(0);
    }

    public void playerHit() {
        String card = drawCard();

        if (card != null) {
            playerHand.add(card);
        }
    }

    public void dealerTurn() {
        while (getDealerPoints() < 17) {
            String card = drawCard();

            if (card == null) {
                break;
            }

            dealerHand.add(card);
        }
    }

    public int getPlayerPoints() {
        return calculatePoints(playerHand);
    }

    public int getDealerPoints() {
        return calculatePoints(dealerHand);
    }

    public List<String> getPlayerHand() {
        return playerHand;
    }

    public List<String> getDealerHand() {
        return dealerHand;
    }

    public boolean isPlayerBusted() {
        return getPlayerPoints() > 21;
    }

    public String getResult() {
        int playerPoints = getPlayerPoints();
        int dealerPoints = getDealerPoints();

        if (playerPoints > 21) {
            return "LOSE";
        }

        if (dealerPoints > 21) {
            return "WIN";
        }

        if (playerPoints > dealerPoints) {
            return "WIN";
        }

        if (playerPoints < dealerPoints) {
            return "LOSE";
        }

        return "DRAW";
    }

    private int calculatePoints(List<String> hand) {
        int total = 0;
        int aces = 0;

        for (String card : hand) {

            String value = card.substring(0, card.length() - 1);

            switch (value) {

                case "J":
                case "Q":
                case "K":
                    total += 10;
                    break;

                case "A":
                    total += 11;
                    aces++;
                    break;

                default:
                    total += Integer.parseInt(value);
                    break;
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}