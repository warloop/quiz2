package com.example.my_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {


    public static final String KEY_EXTRA_ANSWER = "extra_answer";
    private static final String TAG = "MainActivity";
    private static final String KEY_CURRENT_INDEX = "current_index";
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button hintButton;
    private TextView questionTextView;
    private int currentIndex = 0;
    private int correctAnswersCount = 0;
    private boolean answered = false;
    private static final int REQUEST_CODE_PROMPT = 0; // Definiuj unikalny kod żądania
    private boolean answerWasShown = false;

    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false)
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Metoda cyklu życia onSaveInstanceState została wywołana.");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        if (answered) {
            return; // Jeśli odpowiedź została już udzielona, nie można jej zmienić
        }

        boolean correctAnswer = questions[currentIndex].isCorrect();
        int resultMessageId;

        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                correctAnswersCount++; // Zwiększ licznik poprawnych odpowiedzi
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
            answered = true; // Ustaw flagę, że odpowiedź została udzielona

            if (currentIndex == questions.length - 1) {
                showResult();
            }
        }

        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion() {
        currentIndex = (currentIndex + 1) % questions.length;
        answerWasShown = false;
        if (currentIndex == 0) {
            // Jeśli wracamy do pierwszego pytania, zresetuj licznik poprawnych odpowiedzi
            correctAnswersCount = 0;
        }
        questionTextView.setText(questions[currentIndex].getQuestionText());
        answered = false; // Zresetuj flagę odpowiedzi na nowe pytanie
    }

    private void showResult() {
        String resultMessage = getString(R.string.result_message, correctAnswersCount, questions.length);
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();
        if (correctAnswersCount == questions.length) {
            nextButton.setEnabled(false); // Wyłącz przycisk "Następne" po zakończeniu quizu
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) {
                return;
            }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOW, false);
            if (answerWasShown) {
                Toast.makeText(this, R.string.answer_was_shown, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Metoda cyklu życia onCreate() została wywołana.");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        hintButton = findViewById(R.id.hint_button);

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tworzymy intencję dla drugiej aktywności
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                // Przekazujemy poprawną odpowiedź za pomocą putExtra()
                boolean correctAnswer = questions[currentIndex].isCorrect();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                // Rozpoczynamy drugą aktywność
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextQuestion();
            }
        });

        // Wyświetl pierwsze pytanie przy uruchomieniu
        questionTextView.setText(questions[currentIndex].getQuestionText());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Metoda cyklu życia onStart() została wywołana.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Metoda cyklu życia onResume() została wywołana.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Metoda cyklu życia onPause() została wywołana.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Metoda cyklu życia onStop() została wywołana.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Metoda cyklu życia onDestroy() została wywołana.");
    }
}