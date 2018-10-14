package com.example.android.farm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button signUp;
    TextView Login;
    EditText email;
    EditText pass;
    ProgressDialog   progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in


        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Profile.class));
        }

        Login= findViewById(R.id.loginBtn);
        signUp= findViewById(R.id.signupBtn);
        email =(EditText) findViewById(R.id.emailTxt);
        pass = (EditText) findViewById(R.id.passwordTxt);

        progressDialog = new ProgressDialog(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Register();

            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    public void Register(){

            final String userEmail=email.getText().toString();
            String userPass = pass.getText().toString();

        progressDialog.setMessage("Please wait");
        progressDialog.show();
        //Register user with email and password
        firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseAuth=FirebaseAuth.getInstance();
                    FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();
                    String userid=firebaseuser.getUid();

                    mFirebaseDatabase= FirebaseDatabase.getInstance();
                    myRef= mFirebaseDatabase.getReference();
                    myRef.child("users").child(userid).child("name").setValue("empty");
                    myRef.child("users").child(userid).child("surname").setValue("empty");
                    myRef.child("users").child(userid).child("address").setValue("empty");
                    myRef.child("users").child(userid).child("location").setValue("empty");
                    myRef.child("users").child(userid).child("date_of_birth").setValue("empty");
                    myRef.child("users").child(userid).child("user_id").setValue(userid);
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileChange.class));

                    Toast.makeText(MainActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
