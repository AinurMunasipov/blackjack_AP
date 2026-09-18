package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    private BlackjackGame game;

    private TextView playerNameText;
    private TextView dealerCardsText;
    private TextView playerCardsText;
    private TextView playerPointsText;
    private TextView dealerPointsText;

    private Button hitButton;
    private Button standButton;

    private String playerName;

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

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        playerNameText = findViewById(R.id.playerNameText);
        dealerCardsText = findViewById(R.id.dealerCardsText);
        playerCardsText = findViewById(R.id.playerCardsText);
        playerPointsText = findViewById(R.id.playerPointsText);
        dealerPointsText = findViewById(R.id.dealerPointsText);

        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);

        playerName = getIntent().getStringExtra("PLAYER_NAME");

        if (playerName == null || playerName.isEmpty()) {
            playerName = "Jugador";
        }

        playerNameText.setText(playerName);

        game = new BlackjackGame();
        game.startGame();

        updateScreen(false);

        hitButton.setOnClickListener(v -> {

            game.playerHit();
            updateScreen(false);

            if (game.isPlayerBusted()) {

                updateScreen(true);
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                hitButton.postDelayed(
                        this::finishGame,
                        1000
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
                    1000
            );

        });
    }

    private void updateScreen(boolean revealDealerCards) {

        playerCardsText.setText(

                "Tus cartas: " + game.getPlayerHand()

        );

        playerPointsText.setText(

                "Puntos: " + game.getPlayerPoints()

        );

        if (revealDealerCards) {

            dealerCardsText.setText(

                    "Crupier: " + game.getDealerHand()

            );

            dealerPointsText.setText(

                    "Puntos del crupier: " + game.getDealerPoints()

            );

        } else {

            if (game.getDealerHand().size() >= 2) {

                dealerCardsText.setText(

                        "Crupier: [" +

                                game.getDealerHand().get(0) +

                                ", ?]"

                );

            } else {

                dealerCardsText.setText("Crupier: ?");

            }

            dealerPointsText.setText(

                    "Puntos del crupier: ?"

            );

        }

    }

    private void finishGame() {

        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        Intent intent = new Intent(
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