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

public class ResultActivity extends AppCompatActivity {

    private TextView resultText;
    private TextView playerPointsText;
    private TextView dealerPointsText;
    private TextView sessionStatsText;

    private Button playAgainButton;
    private Button menuButton;

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

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

        resultText = findViewById(R.id.resultText);
        playerPointsText = findViewById(R.id.playerPointsText);
        dealerPointsText = findViewById(R.id.dealerPointsText);
        sessionStatsText = findViewById(R.id.sessionStatsText);

        playAgainButton = findViewById(R.id.playAgainButton);
        menuButton = findViewById(R.id.menuButton);

        playerName = getIntent().getStringExtra("PLAYER_NAME");
        String result = getIntent().getStringExtra("GAME_RESULT");

        int playerPoints = getIntent().getIntExtra("PLAYER_POINTS", 0);
        int dealerPoints = getIntent().getIntExtra("DEALER_POINTS", 0);

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Jugador";
        }

        // Solo la primera vez: al rotar la pantalla no se vuelve a contar.
        if (savedInstanceState == null) {
            SessionStats.register(result);
        }

        showResult(result);

        playerPointsText.setText(
                "Puntos del jugador: " + playerPoints
        );

        dealerPointsText.setText(
                "Puntos del crupier: " + dealerPoints
        );

        showSessionStats();

        playAgainButton.setOnClickListener(v -> playAgain());
        menuButton.setOnClickListener(v -> backToMenu());
    }

    private void showResult(String result) {

        String message;
        int color;

        if ("WIN".equals(result)) {

            message = "¡" + playerName + ", ganaste!";
            color = 0xFFFFD166;

        } else if ("LOSE".equals(result)) {

            message = playerName + ", perdiste esta ronda.";
            color = 0xFFEF6B6B;

        } else if ("DRAW".equals(result)) {

            message = playerName + ", fue un empate.";
            color = 0xFFFFFFFF;

        } else {

            message = playerName + ", la partida terminó.";
            color = 0xFFFFFFFF;
        }

        resultText.setText(message);
        resultText.setTextColor(color);
    }

    private void showSessionStats() {

        sessionStatsText.setText(
                "Sesión — Ganadas: " + SessionStats.getWins() +
                        " · Perdidas: " + SessionStats.getLosses() +
                        " · Empates: " + SessionStats.getDraws()
        );
    }

    private void playAgain() {

        Intent intent =
                new Intent(
                        ResultActivity.this,
                        GameActivity.class
                );

        // Limpia Result y la partida anterior: la pila vuelve a ser Main -> Game.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(
                "PLAYER_NAME",
                playerName
        );

        startActivity(intent);
    }

    private void backToMenu() {

        Intent intent =
                new Intent(
                        ResultActivity.this,
                        MainActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
    }
}
