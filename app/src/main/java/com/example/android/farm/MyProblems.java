package com.example.android.farm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProblems extends AppCompatActivity implements View.OnClickListener{

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ListView lv;
    private ImageButton Back;


    public ArrayList<Problem> problemslist = new ArrayList<Problem>();
    public List<String> problemslistname = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_problems);

        Back = (ImageButton) findViewById(R.id.back);
        lv = (ListView) findViewById(R.id.your_list_view_id);
        // this will hold our collection of specimens.


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("My problems");

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Problems").addValueEventListener(new ValueEventListener(){

            /**
             * This method will be invoked any time the data on the database changes.
             * Additionally, it will be invoked as soon as we connect the listener, so that we can get an initial snapshot of the data on the database.
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get all of the children at this level.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                // shake hands with each of them.
                for (DataSnapshot child : children) {
                    String name = (String) child.child("name").getValue();
                    String desc = (String) child.child("desc").getValue();
                    String uniqueID = (String) child.child("uniqueID").getValue();
                    String userid = (String) child.child("userid").getValue();
                    double longitude = (double) child.child("longitude").getValue();
                    double latitude = (double)  child.child("latitude").getValue();
                    String lux = (String) child.child("lux").getValue();
                    String temp = (String) child.child("temperature").getValue();
                    Problem pro = new Problem(desc, name, userid, uniqueID,longitude,latitude,lux,temp);

                    if (user.getUid().equals(userid)) {

                        problemslist.add(pro);
                        problemslistname.add(name);
                        //   }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            MyProblems.this,
                            android.R.layout.simple_list_item_1,
                            problemslistname );



                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                        public void onItemClick(AdapterView<? > arg0, View view, int position, long id) {
                            // When clicked, show a toast with the TextView text
                            Intent i = new Intent(MyProblems.this, DisplayProblem.class);
                            String  UID = problemslist.get(position).getUniqueID();
                            i.putExtra("UniqueID",UID);
                            startActivity(i);

                        }

                    });
                    lv.setAdapter(arrayAdapter);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        if(v == Back){
            finish();
            startActivity(new Intent(this, Profile.class));
        }
    }
}
