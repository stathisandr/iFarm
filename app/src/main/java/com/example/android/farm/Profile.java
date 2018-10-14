package com.example.android.farm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    ImageButton LogoutBtn;
    ImageButton AccountUser;
    ImageButton AddProblem;
    ImageButton MyProblems;
    ImageButton SearchProblems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        LogoutBtn = findViewById(R.id.logoutBtn);
        AccountUser = findViewById(R.id.account);
        AddProblem = findViewById(R.id.AddProblem);
        MyProblems = findViewById(R.id.MyProblems);
        SearchProblems=findViewById(R.id.SearchProblems);
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }
        final  FirebaseUser user = firebaseAuth.getCurrentUser();
        LogoutBtn.setOnClickListener(this);
        AccountUser.setOnClickListener(this);
        AddProblem.setOnClickListener(this);
        MyProblems.setOnClickListener(this);
        SearchProblems.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {
        if(view == AccountUser){
            startActivity(new Intent(this, ProfileChange.class));
            finish();
        }
        if(view == MyProblems){
            startActivity(new Intent(this, MyProblems.class));
            finish();
        }
        if(view == AddProblem){
            startActivity(new Intent(this, AddProblem.class));
            finish();
        }
        if(view == SearchProblems){
            startActivity(new Intent(this, SearchProblem.class));
            finish();
        }
        if(view == LogoutBtn){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
