package com.example.my_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {

    private TextView textViewAnswer;
    private boolean correctAnswer;
    public static final String KEY_EXTRA_ANSWER_SHOW = "answerShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        correctAnswer = getIntent().getBooleanExtra(MainActivity.KEY_EXTRA_ANSWER, true); // Odbieramy wartość is_correct
        textViewAnswer = findViewById(R.id.textViewAnswer);

        Button showAnswerButton = findViewById(R.id.buttonShowAnswer);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
                setAnswerShownResult(true); // Ustaw wynik, że odpowiedź została pokazana
            }
        });

        // Dodaj obsługę przycisku "Cofnij"
        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHintUsedResult();
                finish(); // Zakończ aktywność i wróć do poprzedniej
            }
        });
    }

    private void setAnswerShownResult(boolean answerWasShown) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOW, answerWasShown);
        setResult(RESULT_OK, resultIntent);
    }

    private void showAnswer() {
        int answer = correctAnswer ? R.string.button_true : R.string.button_false;
        textViewAnswer.setText(answer);
    }

    // Dodaj nową metodę do wysłania informacji o użyciu podpowiedzi
    private void sendHintUsedResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOW, true); // Informacja, że podpowiedź została użyta
        setResult(RESULT_OK, resultIntent);
    }
}