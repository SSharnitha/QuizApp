package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.quizapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    TextView userProfileLetter,userNameTextview,userId,userEmail,userRank,cheScore,phyScore,bioScore;
    Button homeButton;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfileLetter=(TextView) findViewById(R.id.userprofileLetter);
        userNameTextview=(TextView) findViewById(R.id.usernameTextview);
        userId= (TextView) findViewById(R.id.userId);
        userEmail=(TextView) findViewById(R.id.userEmail);
        userRank=(TextView) findViewById(R.id.userRank);
        cheScore=(TextView) findViewById(R.id.chemistryScoreOfUser);
        phyScore=(TextView) findViewById(R.id.physicsScoreOfUser);
        bioScore=(TextView) findViewById(R.id.biologyScoreOfUser);
        homeButton=(Button) findViewById(R.id.homeButton);

        email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userEmail.setText(email);
        email = email.substring(0,email.indexOf("@"));
        userNameTextview.setText(email);
        email=email.length() < 2 ? email : email.substring(0, 2);
        userProfileLetter.setText(email.toUpperCase());
        email=FirebaseAuth.getInstance().getCurrentUser().getUid();
        userId.setText(email);

        //Database read
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final Query query1 = reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(email);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    User user = singleSnapshot.getValue(User.class);

                    bioScore.setText(user.getBio_score());
                    phyScore.setText(user.getPhy_score());
                    cheScore.setText(user.getChe_score());
                    userRank.setText(user.getRank());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomePageActivity.class);
                startActivity(intent);
            }
        });
    }
}
