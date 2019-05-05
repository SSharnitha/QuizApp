package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionAddActivity extends AppCompatActivity {
    Button addButton;
    EditText questionAdd,questionNumber,optionA,optionB,optionC,optionD,correctOption,correctAnswer,solution,subjectName;
    ProgressBar progressBar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);


        addButton = (Button) findViewById(R.id.addButton);
        questionAdd = (EditText) findViewById(R.id.question_add);
        questionNumber= (EditText) findViewById(R.id.question_number);
        optionA = (EditText) findViewById(R.id.option_a);
        optionB = (EditText) findViewById(R.id.option_b);
        optionC = (EditText) findViewById(R.id.option_c);
        optionD = (EditText) findViewById(R.id.option_d);
        correctAnswer = (EditText) findViewById(R.id.correct_answer);
        correctOption = (EditText) findViewById(R.id.correct_option);
        solution = (EditText) findViewById(R.id.solution_add);
        subjectName = (EditText) findViewById(R.id.subjectName);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = new Question();
                question.setQuestion_number(questionNumber.getText().toString());
                question.setQuestion(questionAdd.getText().toString());
                question.setOption_a(optionA.getText().toString());
                question.setOption_b(optionB.getText().toString());
                question.setOption_c(optionC.getText().toString());
                question.setOption_d(optionD.getText().toString());
                question.setCorrect_answer(correctAnswer.getText().toString());
                question.setCorrect_option(correctOption.getText().toString());
                question.setSolution(solution.getText().toString());

                FirebaseDatabase.getInstance().getReference()
                        .child(subjectName.getText().toString())
                        .child(questionNumber.getText().toString())
                        .setValue(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar3.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Question Added",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar3.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"SomeThing Went Wrong In Database-Question Not Added"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.options_menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }

}
