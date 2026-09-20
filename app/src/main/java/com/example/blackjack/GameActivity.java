package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    private BlackjackGame game;

    private TextView playerNameText;
    private TextView dealerPointsText;
    private TextView playerPointsText;

    private LinearLayout dealerPrimaryRow;
    private LinearLayout dealerReserveRow;
    private LinearLayout playerPrimaryRow;
    private LinearLayout playerReserveRow;

    private Button hitButton;
    private Button standButton;

    private String playerName;

    private int cardWidth;
    private int cardHeight;
    private int cardMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    int sidePadding = dpToPx(20);

                    v.setPadding(
                            systemBars.left + sidePadding,
                            systemBars.top,
                            systemBars.right + sidePadding,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        playerNameText = findViewById(R.id.playerNameText);
        dealerPointsText = findViewById(R.id.dealerPointsText);
        playerPointsText = findViewById(R.id.playerPointsText);

        dealerPrimaryRow = findViewById(R.id.dealerPrimaryRow);
        dealerReserveRow = findViewById(R.id.dealerReserveRow);
        playerPrimaryRow = findViewById(R.id.playerPrimaryRow);
        playerReserveRow = findViewById(R.id.playerReserveRow);

        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);

        calculateCardSize();

        playerName = getIntent().getStringExtra("PLAYER_NAME");

        if (playerName == null || playerName.isEmpty()) {
            playerName = "Jugador";
        }

        playerNameText.setText(playerName);

        game = new BlackjackGame();
        game.startGame();

        updateScreen(false);

        if (game.getPlayerPoints() == 21) {

            game.dealerTurn();
            updateScreen(true);

            hitButton.setEnabled(false);
            standButton.setEnabled(false);

            hitButton.postDelayed(
                    this::finishGame,
                    2000
            );
        }

        hitButton.setOnClickListener(v -> {

            game.playerHit();
            updateScreen(false);

            if (game.isPlayerBusted()) {

                updateScreen(true);

                hitButton.setEnabled(false);
                standButton.setEnabled(false);

                hitButton.postDelayed(
                        this::finishGame,
                        2000
                );

            } else if (game.getPlayerPoints() == 21) {

                game.dealerTurn();
                updateScreen(true);

                hitButton.setEnabled(false);
                standButton.setEnabled(false);

                hitButton.postDelayed(
                        this::finishGame,
                        2000
                );
            }
        });

        standButton.setOnClickListener(v -> {

            game.dealerTurn();
            updateScreen(true);

            hitButton.setEnabled(false);
            standButton.setEnabled(false);

            standButton.postDelayed(
                    this::finishGame,
                    2000
            );
        });
    }

    private void calculateCardSize() {

        DisplayMetrics metrics =
                getResources().getDisplayMetrics();

        int screenWidth = metrics.widthPixels;

        int horizontalPadding = dpToPx(40);
        int totalMargins = dpToPx(32);

        int availableWidth =
                screenWidth - horizontalPadding - totalMargins;

        int calculatedWidth =
                availableWidth / 4;

        int maxWidth = dpToPx(82);

        cardWidth = Math.min(
                calculatedWidth,
                maxWidth
        );

        cardHeight =
                Math.round(cardWidth * 1.4f);

        cardMargin = dpToPx(4);
    }

    private void updateScreen(boolean revealDealerCards) {

        showDealerCards(revealDealerCards);
        showPlayerCards();

        playerPointsText.setText(
                "Puntos del jugador: " +
                        game.getPlayerPoints()
        );

        if (revealDealerCards) {

            dealerPointsText.setText(
                    "Puntos del crupier: " +
                            game.getDealerPoints()
            );

        } else {

            dealerPointsText.setText(
                    "Puntos del crupier: ?"
            );
        }
    }

    private void showDealerCards(
            boolean revealDealerCards
    ) {

        dealerPrimaryRow.removeAllViews();
        dealerReserveRow.removeAllViews();

        List<String> cards =
                game.getDealerHand();

        for (int i = 0; i < cards.size(); i++) {

            boolean hidden =
                    !revealDealerCards && i == 1;

            if (i < 4) {

                addCardImage(
                        dealerPrimaryRow,
                        cards.get(i),
                        hidden
                );

            } else if (i < 8) {

                addCardImage(
                        dealerReserveRow,
                        cards.get(i),
                        hidden
                );
            }
        }

        addReservePlaceholders(
                dealerReserveRow,
                Math.max(0, cards.size() - 4)
        );
    }

    private void showPlayerCards() {

        playerPrimaryRow.removeAllViews();
        playerReserveRow.removeAllViews();

        List<String> cards =
                game.getPlayerHand();

        for (int i = 0; i < cards.size(); i++) {

            if (i < 4) {

                addCardImage(
                        playerPrimaryRow,
                        cards.get(i),
                        false
                );

            } else if (i < 8) {

                addCardImage(
                        playerReserveRow,
                        cards.get(i),
                        false
                );
            }
        }

        addReservePlaceholders(
                playerReserveRow,
                Math.max(0, cards.size() - 4)
        );
    }

    private void addReservePlaceholders(
            LinearLayout container,
            int occupiedSlots
    ) {

        int placeholders =
                4 - Math.min(occupiedSlots, 4);

        for (int i = 0; i < placeholders; i++) {

            TextView placeholder =
                    new TextView(this);

            LinearLayout.LayoutParams params =
                    createCardLayoutParams();

            placeholder.setLayoutParams(params);

            placeholder.setBackgroundResource(
                    R.drawable.card_slot_background
            );

            placeholder.setContentDescription(
                    "Espacio para carta"
            );

            container.addView(placeholder);
        }
    }

    private void addCardImage(
            LinearLayout container,
            String card,
            boolean hidden
    ) {

        ImageView imageView =
                new ImageView(this);

        imageView.setLayoutParams(
                createCardLayoutParams()
        );

        imageView.setScaleType(
                ImageView.ScaleType.FIT_CENTER
        );

        if (hidden) {

            imageView.setImageResource(
                    R.drawable.back_dark
            );

            imageView.setContentDescription(
                    "Carta oculta"
            );

        } else {

            imageView.setImageResource(
                    getCardResource(card)
            );

            imageView.setContentDescription(
                    "Carta " + card
            );
        }

        container.addView(imageView);
    }

    private LinearLayout.LayoutParams
    createCardLayoutParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        cardWidth,
                        cardHeight
                );

        params.setMargins(
                cardMargin,
                0,
                cardMargin,
                0
        );

        return params;
    }

    private int getCardResource(String card) {

        String suitSymbol =
                card.substring(
                        card.length() - 1
                );

        String value =
                card.substring(
                        0,
                        card.length() - 1
                ).toLowerCase();

        String suit;

        switch (suitSymbol) {

            case "♣":
                suit = "clubs";
                break;

            case "♦":
                suit = "diamonds";
                break;

            case "♥":
                suit = "hearts";
                break;

            case "♠":
                suit = "spades";
                break;

            default:
                throw new IllegalArgumentException(
                        "Palo desconocido: " +
                                suitSymbol
                );
        }

        String resourceName =
                suit + "_" + value;

        int resourceId =
                getResources().getIdentifier(
                        resourceName,
                        "drawable",
                        getPackageName()
                );

        if (resourceId == 0) {

            throw new IllegalArgumentException(
                    "No se encontró la imagen: " +
                            resourceName
            );
        }

        return resourceId;
    }

    private int dpToPx(int dp) {

        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(
                dp * density
        );
    }

    private void finishGame() {

        Intent intent =
                new Intent(
                        GameActivity.this,
                        ResultActivity.class
                );

        intent.putExtra(
                "PLAYER_NAME",
                playerName
        );

        intent.putExtra(
                "GAME_RESULT",
                game.getResult()
        );

        intent.putExtra(
                "PLAYER_POINTS",
                game.getPlayerPoints()
        );

        intent.putExtra(
                "DEALER_POINTS",
                game.getDealerPoints()
        );

        startActivity(intent);
    }
}