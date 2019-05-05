package com.example.quizapp;

import android.content.Intent;
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

public class QuizActivity extends AppCompatActivity {
    TextView questionView,option1,option2,option3,option4,quesNo;
    Button nextButton,previousButton,submitButton;
    int count=1,questionCount=1,phy_s,bio_s,che_s;
    static  int correctAnswerCount=0;
    String subject;
    char[] userAnswer;
    char[] correctAnswerOption;
    String phy_score,phy_qno,bio_qno,bio_score,che_qno,che_score,rank;
    ProgressBar progressBar2;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quesNo = (TextView) findViewById(R.id.quesNo);
        questionView = (TextView) findViewById(R.id.questionView);
        option1 = (TextView) findViewById(R.id.option1);
        option2 = (TextView) findViewById(R.id.option2);
        option3 = (TextView) findViewById(R.id.option3);
        option4 = (TextView) findViewById(R.id.option4);
        nextButton = (Button) findViewById(R.id.nextButton);
        previousButton = (Button) findViewById(R.id.previousButtton);
        submitButton = (Button) findViewById(R.id.submitButton);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        reference = FirebaseDatabase.getInstance().getReference();
        userAnswer = new char[6];
        correctAnswerOption = new char[6];

        subject = getIntent().getExtras().getString("subject", " ");
        if (subject == " ") {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        }
        fetchQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                questionCount++;
                fetchQuestion();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                questionCount--;
                fetchQuestion();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }

    public void checkAnswer(View view){

        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);

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
    public void fetchQuestion(){
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);

        option1.setBackground(getDrawable(R.drawable.imgsty));
        option2.setBackground(getDrawable(R.drawable.imgsty));
        option3.setBackground(getDrawable(R.drawable.imgsty));
        option4.setBackground(getDrawable(R.drawable.imgsty));

        if(count<5){
            if(submitButton.getVisibility()==View.VISIBLE)
                submitButton.setVisibility(View.INVISIBLE);
            if(nextButton.getVisibility()==View.INVISIBLE)
                nextButton.setVisibility(View.VISIBLE);
            if(count==1) {
                previousButton.setVisibility(View.INVISIBLE);
                fetchQuestionCount();
            }
            else {
                previousButton.setVisibility(View.VISIBLE);
            }
            databaseCode();
        }else if (count==5){
            databaseCode();
            submitButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        }
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

                    if(subject.equals("physics")){
                        questionCount = Integer.parseInt(phy_qno);
                        phy_s = Integer.parseInt(phy_score);
                    }else if(subject.equals("chemistry")){
                        questionCount = Integer.parseInt(che_qno);
                        che_s = Integer.parseInt(che_score);
                    }else if(subject.equals("biology")) {
                        questionCount = Integer.parseInt(bio_qno);
                        bio_s = Integer.parseInt(bio_score);
                    }
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

                    quesNo.setText(Integer.toString(count));
                    questionView.setText(question.getQuestion());
                    option1.setText(question.getOption_a());
                    option2.setText(question.getOption_b());
                    option3.setText(question.getOption_c());
                    option4.setText(question.getOption_d());
                    progressBar2.setVisibility(View.INVISIBLE);
                    correctAnswerOption[count]=question.getCorrect_option().charAt(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
