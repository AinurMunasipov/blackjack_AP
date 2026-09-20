package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText nameInput;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        nameInput = findViewById(R.id.nameInput);
        playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(v -> startGame());
    }

    private void startGame() {

        String playerName =
                nameInput.getText().toString().trim();

        if (playerName.isEmpty()) {

            nameInput.setError("Ingresá tu nombre para jugar");
            nameInput.requestFocus();

            return;
        }

        Intent intent =
                new Intent(
                        MainActivity.this,
                        GameActivity.class
                );

        intent.putExtra(
                "PLAYER_NAME",
                playerName
        );

        startActivity(intent);
    }
}
