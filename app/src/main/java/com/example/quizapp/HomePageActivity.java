package com.example.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {
    char difficultyLevel='e',nodeName;
    Switch difficultyLevelSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        difficultyLevelSwitch = (Switch) findViewById(R.id.difficultySwitch);

        difficultyLevelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    difficultyLevel = 'h';
                }else{
                    difficultyLevel = 'e';
                }
            }
        });

    }

    public void clickPhyEvent(View view){
        Intent intent;
        if(difficultyLevel == 'e') {
            intent = new Intent(getApplicationContext(), QuizActivity.class);
            intent.putExtra("subject", getString(R.string.dbnode_phy));
            startActivity(intent);
        }else  if(difficultyLevel == 'h'){
            intent = new Intent(getApplicationContext(), QuizActivityHard.class);
            intent.putExtra("subject", getString(R.string.dbnode_phy_hard));
            startActivity(intent);
        }
    }

    public void clickBioEvent(View view){
        Intent intent;
        if(difficultyLevel == 'e') {
            intent = new Intent(getApplicationContext(), QuizActivity.class);
            intent.putExtra("subject", getString(R.string.dbnode_bio));
            startActivity(intent);
        }else  if(difficultyLevel == 'h'){
            intent = new Intent(getApplicationContext(), QuizActivityHard.class);
            intent.putExtra("subject", getString(R.string.dbnode_bio_hard));
            startActivity(intent);
        }
    }

    public void clickCheEvent(View view){
        Intent intent;
        if(difficultyLevel == 'e') {
            intent = new Intent(getApplicationContext(), QuizActivity.class);
            intent.putExtra("subject", getString(R.string.dbnode_che));
            startActivity(intent);
        }else  if(difficultyLevel == 'h'){
            intent = new Intent(getApplicationContext(), QuizActivityHard.class);
            intent.putExtra("subject", getString(R.string.dbnode_che_hard));
            startActivity(intent);
        }
    }
    //-----------------------Option Menu------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
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
            case R.id.userAccountDetails:
                Intent intent1=new Intent(getApplicationContext(),UserProfileActivity.class);
                startActivity(intent1);
                return true;
            default:
                return false;
        }
    }

}
