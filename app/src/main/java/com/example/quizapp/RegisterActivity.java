package com.example.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button signInAgainButton,signUpButton;
    TextView emailAddress,passwaordInitial,reEnterPassword;
    ProgressBar progressBar;
    final static String DOMAIN_NAME = "gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signInAgainButton=(Button) findViewById(R.id.signInAgainButton);
        signUpButton=(Button) findViewById(R.id.signUpButton);
        emailAddress=(TextView) findViewById(R.id.emailAddress);
        passwaordInitial=(TextView) findViewById(R.id.passwordInitial);
        reEnterPassword=(TextView) findViewById(R.id.reEnterPassword);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        signInAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------------------------------------------VALIDATION CHECK-----------------------------------------------
                if(isEmpty(emailAddress.getText().toString())|| isEmpty(passwaordInitial.getText().toString()) || isEmpty(reEnterPassword.getText().toString())) {
                    Log.i("Message","Please Fill out The Field");
                    Toast.makeText(getApplicationContext(),"Please fill out the fields",Toast.LENGTH_SHORT).show();
                }else if(isEmailNotCorrect(emailAddress.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Email must be in the form of @gmail.com",Toast.LENGTH_SHORT).show();
                }else if(isPasswordNotCoorect(passwaordInitial.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password must be atleast 8 characters and atmost 15 chracters",Toast.LENGTH_SHORT).show();
                }else if(isPasswordNotMatch(passwaordInitial.getText().toString(),reEnterPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerNewUser(emailAddress.getText().toString(),passwaordInitial.getText().toString());
                }
            }
        });
    }
    //-------------------------REGISTER NEW USER----------------------------------------------------

    public void registerNewUser(final String emailId, String password){
        Log.i("Message:", "Came Here");
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("Message:", "Task-"+task.isSuccessful());
                if(task.isSuccessful()){
                    Log.i("Message:","onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Toast.makeText(getApplicationContext(),"Registered Sucessfully",Toast.LENGTH_SHORT).show();
                    sendVerificationEmail();

                    //Database Code
                    User user = new User();
                    user.setName(emailId.substring(0,emailId.indexOf("@")));
                    user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    user.setBio_qno("1");
                    user.setChe_qno("1");
                    user.setPhy_qno("1");
                    user.setPhy_score("0");
                    user.setChe_score("0");
                    user.setBio_score("0");

                    FirebaseDatabase.getInstance().getReference()
                            .child(getString(R.string.dbnode_users))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            redirectLogin();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            redirectLogin();
                            Toast.makeText(getApplicationContext(),"SomeThing Went Wrong In Database"+e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.i("VeryImp:",e.getMessage());
                        }
                    });

                }else {
                    Log.i("Message", "Issue In Registeration");
                    Toast.makeText(getApplicationContext(), "Registered Please Click Resend Verification Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //-------------------------VALIDATION METHODS---------------------------------------------------
    public boolean isEmpty(String string){
        return string.equals("");
    }

    public boolean isEmailNotCorrect(String string){
        String substring=string.substring(string.indexOf("@")+1).toLowerCase();
        return !substring.equals(DOMAIN_NAME);
    }

    public boolean isPasswordNotCoorect(String string){
        if(string.length()>=8 && string.length()<=15)
            return false;
        else
            return true;
    }

    public boolean isPasswordNotMatch(String pass,String repass){
        return !pass.equals(repass);
    }

    //---------------------------Email Verification-------------------------------------------------
    private void sendVerificationEmail(){
        Log.i("Message:", "Came Here For Verification");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Log.i("Message:", "Have User");
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("Message:", "Task-Email-"+task.isSuccessful());
                    if(task.isSuccessful()){
                        Log.i("Message:", "Sent Email");
                        Toast.makeText(getApplicationContext(),"Sent Verification Email",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error In Sending Verification Email",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //---------------------------Redirect To Login-------------------------------------------------
    private void redirectLogin(){
        Intent intent=new Intent(getApplicationContext(),LogInActivity.class);
        startActivity(intent);
        finish();
    }
}