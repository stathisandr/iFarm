package com.example.android.farm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileChange extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Tag";
    EditText editTextName, editTextSurname, editTextDate, editTextAddress;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    public ArrayList<User> userlist = new ArrayList<User>();
    protected User Current_User;
    private Button Sumbit;
    private ImageButton Back;
    private TextView un;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        un = (TextView)  findViewById(R.id.un);
        Sumbit=findViewById(R.id.Submit);
        Back = (ImageButton) findViewById(R.id.back);
        Back.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                // shake hands with each of them.
                for (DataSnapshot child : children) {
                    String name = (String) child.child("name").getValue();
                    String surname = (String) child.child("surname").getValue();
                    String address = (String) child.child("address").getValue();
                    String date_of_birth = (String) child.child("date_of_birth").getValue();
                    String id = (String) child.child("user_id").getValue();
                    Current_User= new User(name,surname,address,date_of_birth,id);
                    userlist.add(Current_User);

                }
                //Display the Current known information for the logged in user
                FirebaseUser user = mAuth.getCurrentUser();
                userID = user.getUid();
                for( int i=0;i<userlist.size();i++){
                   if (userlist.get(i).getUser_id().equals(userID)){
                       editTextName.setText(userlist.get(i).getName());
                       editTextSurname.setText(userlist.get(i).getSurname());
                       editTextDate.setText(userlist.get(i).getDate_of_birth());
                       editTextAddress.setText(userlist.get(i).getAddress());
                       un.setText(user.getEmail());
                   }

                }


            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }


        });

        Sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=editTextName.getText().toString();
                String surname=editTextSurname.getText().toString();
                String address=editTextAddress.getText().toString();
                String date_of_birth =editTextDate.getText().toString();

                mAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseuser = mAuth.getCurrentUser();
                String userid=firebaseuser.getUid();
                //Sumbit the changes to the DB
                mFirebaseDatabase= FirebaseDatabase.getInstance();
                myRef= mFirebaseDatabase.getReference();
                myRef.child("users").child(userid).child("name").setValue(name);
                myRef.child("users").child(userid).child("surname").setValue(surname);
                myRef.child("users").child(userid).child("address").setValue(address);
                myRef.child("users").child(userid).child("date_of_birth").setValue(date_of_birth);


                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name+" "+surname).build();
                firebaseuser.updateProfile(profileUpdates);



                Toast.makeText(ProfileChange.this, "Changes have been saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(view.getContext(),Profile.class));
                finish();;


            }
        });
    }


    @Override
    public void onBackPressed() {
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v == Back){
            finish();
            startActivity(new Intent(this, Profile.class));
        }
    }
}

