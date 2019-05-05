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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResendVerificationActivity extends AppCompatActivity {

    TextView emailIdforResend,passwordForResend;
    Button resendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_verification);

        emailIdforResend = (TextView) findViewById(R.id.emailIdForResend);
        passwordForResend = (TextView) findViewById(R.id.password);
        resendEmail = (Button) findViewById(R.id.signInButton);

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(emailIdforResend.getText().toString())&&isEmpty(passwordForResend.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please Fill Out The Fields",Toast.LENGTH_SHORT).show();
                }else{
                    authenticateAndResendEmail(emailIdforResend.getText().toString(),passwordForResend.getText().toString());
                }
            }
        });

    }

    public boolean isEmpty(String string){
        return string.equals("");
    }

    public void authenticateAndResendEmail(String emailId,String password){
        AuthCredential credential = EmailAuthProvider.getCredential(emailId,password);

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){
                    Log.i("Message:","Trying To Resend With The Credentials");
                    sendVerificationEmail();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(getApplicationContext(),LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Invalid EmailId Or Password Try Again",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //---------------------------Email Verification-------------------------------------------------
    private void sendVerificationEmail(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sent Verification Email",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error In Sending Verification Email",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}