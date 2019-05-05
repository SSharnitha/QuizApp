package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    Button registerButton,signInButton,resendEmailButton,forgotPassButton;
    TextView emailId,password;
    FirebaseAuth.AuthStateListener myAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        registerButton=(Button) findViewById(R.id.registerButton);
        signInButton=(Button) findViewById(R.id.signInButton);
        emailId=(TextView) findViewById(R.id.emailId) ;
        password=(TextView) findViewById(R.id.password);
        resendEmailButton = (Button) findViewById(R.id.resendEmailVerificationButton);
        forgotPassButton=(Button) findViewById(R.id.forgotPassword);

        setupAuthListener();

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isEmpty(emailId.getText().toString()) || isEmpty(password.getText().toString())){
                    Log.i("Message","Please Fill out The Field");
                    Toast.makeText(getApplicationContext(),"Please fill out the fields",Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.i("Message:", "Authenticated");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Message:", "Authentication Failed");
                            Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(emailId.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please Enter The EmailId",Toast.LENGTH_LONG).show();
                }else {
                    sendResendPasswordLink(emailId.getText().toString());
                }
            }
        });

        resendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ResendVerificationActivity.class);
                startActivity(intent);
            }
        });
    }

    //----------------------------FireBaseSetup-----------------------------------------------------
    public void setupAuthListener(){
        myAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    if(user.isEmailVerified()){
                        Log.i("Message-Signned In-","UserId:"+user.getUid());
                        Toast.makeText(getApplicationContext(),"Authenticated With Email\t"+user.getEmail(),Toast.LENGTH_SHORT).show();
                        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("sharnitha189@gmail.com")){
                            Intent intent = new Intent(getApplicationContext(), QuestionAddActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Log.i("Message-Not Signned In-","UserId:"+user.getUid());
                        Toast.makeText(getApplicationContext(),"Check Your Inbox For Verification Link in\t"+user.getEmail(),Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }else{
                    Log.i("Message-Not Signned In-","Not Authenticated");
                }
            }
        };
    }
    //-------------------------------------password reset---------------------------------
    private void sendResendPasswordLink(String emailId){
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("Message:","Password Reset Email Sent");
                    Toast.makeText(getApplicationContext(),"Password resentLink sent",Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("Message:","Password Reset Email Not Sent");
                    Toast.makeText(getApplicationContext(),"Password resentLink Not sent Try Again With Correct EmailId",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(myAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(myAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(myAuthListener);
        }
    }

    public boolean isEmpty(String string){
        return string.equals("");
    }

}
