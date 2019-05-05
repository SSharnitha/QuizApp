package com.example.quizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.quizapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuizActivityHard extends AppCompatActivity {
    String subject,phy_score,phy_qno,bio_qno,bio_score,che_qno,che_score,rank;
    TextView timerText,hardQuestionView,hardOption1,hardOption2,hardOption3,hardOption4,qno1,qno2,qno3,qno4,qno5;
    Button hardSubmitButton;
    int stableCount=0;
    boolean flag = false;
    int count=1,questionCount,phy_s,bio_s,che_s;
    static  int correctAnswerCount=0;
    char[] userAnswer,correctAnswerOption;
    ProgressBar progressBar3;
    DatabaseReference reference;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_hard);

        hardQuestionView = (TextView) findViewById(R.id.hardQuestionView);
        hardOption1 = (TextView) findViewById(R.id.hardOption1);
        hardOption2 = (TextView) findViewById(R.id.hardOption2);
        hardOption3 = (TextView) findViewById(R.id.hardOption3);
        hardOption4 = (TextView) findViewById(R.id.hardOption4);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        timerText = (TextView) findViewById(R.id.timerText);
        hardSubmitButton = (Button) findViewById(R.id.hardSubmitButton);
        qno1 = (TextView) findViewById(R.id.qno1);
        qno2 = (TextView) findViewById(R.id.qno2);
        qno3 = (TextView) findViewById(R.id.qno3);
        qno4 = (TextView) findViewById(R.id.qno4);
        qno5 = (TextView) findViewById(R.id.qno5);
        reference = FirebaseDatabase.getInstance().getReference();
        userAnswer = new char[6];
        correctAnswerOption = new char[6];

        subject = getIntent().getExtras().getString("subject", " ");
        if (subject == " ") {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        }

        fetchQuestion();
    }

    public void hardSubmitButtonClick(View view){
        questionCount++;
        countCorrectAnswer();
        int score;
        if(subject.equals("physics")){
            score = phy_s+correctAnswerCount;
            reference.child(getString(R.string.dbnode_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_phy_score))
                    .setValue(Integer.toString(score));
            reference.child(getString(R.string.dbnode_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_phy_main_qno))
                    .setValue(Integer.toString(questionCount));
        }else if(subject.equals("chemistry")){
            score = che_s+correctAnswerCount;
            reference.child(getString(R.string.dbnode_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_che_score))
                    .setValue(Integer.toString(score));
            reference.child(getString(R.string.dbnode_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_che_main_qno))
                    .setValue(Integer.toString(questionCount));
        }else if(subject.equals("biology")){
            score = bio_s+correctAnswerCount;
            reference.child(getString(R.string.dbnode_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_bio_score))
                    .setValue(Integer.toString(score));
            reference.child(getString(R.string.dbnode_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_bio_main_qno))
                    .setValue(Integer.toString(questionCount));
        }
        countDownTimer.cancel();
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        startActivity(intent);
        finish();
    }


    public void fetchQuestion(){

        hardOption1.setBackground(getDrawable(R.drawable.imgsty));
        hardOption2.setBackground(getDrawable(R.drawable.imgsty));
        hardOption3.setBackground(getDrawable(R.drawable.imgsty));
        hardOption4.setBackground(getDrawable(R.drawable.imgsty));

        fetchQuestionCount();
        playFunction();
    }

    public void questionNumber(View view){
        qno1.setBackgroundColor(getResources().getColor(R.color.transperant));
        qno2.setBackgroundColor(getResources().getColor(R.color.transperant));
        qno3.setBackgroundColor(getResources().getColor(R.color.transperant));
        qno4.setBackgroundColor(getResources().getColor(R.color.transperant));
        qno5.setBackgroundColor(getResources().getColor(R.color.transperant));

        hardOption1.setEnabled(true);
        hardOption2.setEnabled(true);
        hardOption3.setEnabled(true);
        hardOption4.setEnabled(true);

        switch (view.getTag().toString()){
            case "1":
                questionCount = stableCount;
                count = 1;
                view.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case "2":
                questionCount = stableCount+1;
                count = 2;
                view.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case "3":
                questionCount = stableCount+2;
                count = 3;
                view.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case "4":
                questionCount = stableCount+3;
                count = 4;
                view.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case "5":
                questionCount = stableCount+4;
                count = 5;
                view.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            default:
                Toast.makeText(getApplicationContext(),"Choose Question Number",Toast.LENGTH_SHORT).show();
                break;
        }

        hardOption1.setBackground(getDrawable(R.drawable.imgsty));
        hardOption2.setBackground(getDrawable(R.drawable.imgsty));
        hardOption3.setBackground(getDrawable(R.drawable.imgsty));
        hardOption4.setBackground(getDrawable(R.drawable.imgsty));
        databaseCode();
    }

    public void fetchQuestionCount(){
        final Query query = reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    User user = singleSnapshot.getValue(User.class);

                    phy_qno = user.getPhy_qno();
                    che_qno = user.getChe_qno();
                    bio_qno = user.getBio_qno();

                    phy_score = user.getPhy_score();
                    che_score = user.getChe_score();
                    bio_score = user.getBio_score();

                    if(subject.equals("physics_hard")){
                        questionCount = Integer.parseInt(phy_qno);
                        phy_s = Integer.parseInt(phy_score);
                    }else if(subject.equals("chemistry_hard")){
                        questionCount = Integer.parseInt(che_qno);
                        che_s = Integer.parseInt(che_score);
                    }else if(subject.equals("biology_hard")) {
                        questionCount = Integer.parseInt(bio_qno);
                        bio_s = Integer.parseInt(bio_score);
                    }
                    stableCount = questionCount;
                    databaseCode();
                    rank = user.getRank();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void databaseCode(){
        final Query query1 = reference.child(subject)
                .orderByKey()
                .equalTo(Integer.toString(questionCount));

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Question question = singleSnapshot.getValue(Question.class);

                    hardQuestionView.setText(question.getQuestion());
                    hardOption1.setText(question.getOption_a());
                    hardOption2.setText(question.getOption_b());
                    hardOption3.setText(question.getOption_c());
                    hardOption4.setText(question.getOption_d());
                    progressBar3.setVisibility(View.INVISIBLE);
                    correctAnswerOption[count]=question.getCorrect_option().charAt(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Database Failure"+databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void playFunction(){
        fetchQuestionCount();
        countDownTimer = new CountDownTimer(600200,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int valueInSec = ((int) millisUntilFinished)/1000;
                if(valueInSec>60){
                    timerText.setText((valueInSec/60)+"m "+(valueInSec%60)+"s");
                }else{
                    timerText.setText((valueInSec%60)+"s");
                }

            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"Time Over",Toast.LENGTH_LONG).show();
                hardSubmitButtonClick(hardSubmitButton);
            }
        }.start();
    }

    public void checkAnswer(View view){
        hardOption1.setEnabled(false);
        hardOption2.setEnabled(false);
        hardOption3.setEnabled(false);
        hardOption4.setEnabled(false);
        switch(view.getTag().toString()){
            case "1":
                userAnswer[count]='a';
                view.setBackgroundColor(getResources().getColor(R.color.correctAnswer));
                break;
            case "2":
                userAnswer[count]='b';
                view.setBackgroundColor(getResources().getColor(R.color.correctAnswer));
                break;
            case "3":
                userAnswer[count]='c';
                view.setBackgroundColor(getResources().getColor(R.color.correctAnswer));
                break;
            case "4":
                userAnswer[count]='d';
                view.setBackgroundColor(getResources().getColor(R.color.correctAnswer));
                break;
            default:
                userAnswer[count]='e';
                break;
        }
    }

    public  void countCorrectAnswer(){

        for(int i=1;i<=5;i++){
            if(userAnswer[i]==correctAnswerOption[i]) {
                correctAnswerCount++;
            }
        }
    }
}
