package com.example.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {
    TextView correctCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        correctCount = (TextView) findViewById(R.id.correctCount);

        correctCount.setText("Your Score is:"+Integer.toString(QuizActivity.correctAnswerCount)+"/5");
    }
}
