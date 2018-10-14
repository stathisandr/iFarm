package com.example.android.farm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DisplayProblem extends AppCompatActivity   implements View.OnClickListener{
    private Button  ConvoList,Chat;
    private ImageButton Back, See_it_on_the_Map;
    private ImageView imageView;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    //defining a database reference
    public ArrayList<Problem> problemslist = new ArrayList<Problem>();
    public List<String> problemslistname = new ArrayList<String>();
    Intent iin;
    Bundle b ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_problem);
        imageView = (ImageView) findViewById(R.id.viewproblem);
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
        final  FirebaseUser user = firebaseAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        iin= getIntent();
        b = iin.getExtras();
        String problemid= iin.getStringExtra("UniqueID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Problems").addValueEventListener(new ValueEventListener(){
            /**
             * This method will be invoked any time the data on the database changes.
             * Additionally, it will be invoked as soon as we connect the listener, so that we can get an initial snapshot of the data on the database.
             * @param dataSnapshot
             */
            //Get the problems fron DB
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

                    problemslist.add(pro);
                    problemslistname.add(name);

                }
                iin= getIntent();
                b = iin.getExtras();
                if(b!=null)
                {
                    //Display only the current problem
                    String j =(String) b.get("UniqueID");
                    for (int i=0;i<problemslist.size();i++){
                        if (problemslist.get(i).getUniqueID().equals(j)){
                            TextView textViewName = (TextView)findViewById(R.id.textViewName);
                            TextView textViewDesc = (TextView)findViewById(R.id.textViewDesc);
                            TextView textViewLight = (TextView)findViewById(R.id.textViewLight);
                            //TextView textViewTemp = (TextView)findViewById(R.id.textViewtemperature);
                            String desc = problemslist.get(i).getDesc();
                            String name = problemslist.get(i).getName();
                            String lux = problemslist.get(i).getLux();
                            String temp = problemslist.get(i).getTemperature();
                            textViewLight.setText(lux);
                            textViewDesc.setText(desc);
                            textViewName.setText(name);
                            //textViewTemp.setText(temp);
                        }
                    }
                }
                String picturename=null;
                String j =(String) b.get("UniqueID");
                FirebaseStorage storage=FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                for (int i =0; i<problemslist.size();i++) {
                    if (problemslist.get(i).getUniqueID().equals(j)) {
                        picturename =problemslist.get(i).getUniqueID();
                    }
                }
                storageRef.child(picturename).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Use the bytes to display the image
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // Set the Bitmap data to the ImageView
                        imageView.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                            }
                                        }
                );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        See_it_on_the_Map = findViewById(R.id.See_it_on_the_Map);
        See_it_on_the_Map.setOnClickListener(this);
        ConvoList = (Button) findViewById(R.id.ConvoList);
        ConvoList.setOnClickListener(this);
        Back = (ImageButton) findViewById(R.id.back);
        Back.setOnClickListener(this);
        Chat = findViewById(R.id.ConvoList);
        Chat.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onClick(View v) {
        double longitude= 0;
        double latitude=0;
        String j =(String) b.get("UniqueID");
        if( v == See_it_on_the_Map){
            for (int i =0; i<problemslist.size();i++) {
                if (problemslist.get(i).getUniqueID().equals(j)) {
                    longitude =problemslist.get(i).getLongitude();
                    latitude =problemslist.get(i).getLatitude();
                }
            }
            Intent i = new Intent(DisplayProblem.this, MapsActivity.class);
            i.putExtra("Longitude",longitude);
            i.putExtra("Latitude", latitude);
            finish();
            startActivity(i);
        }

        if(v == ConvoList){
            Intent iin= getIntent();
            Bundle b = iin.getExtras();
             j =(String) b.get("UniqueID");
            Intent i = new Intent(DisplayProblem.this, Chat.class);
            String  UID = j;
            i.putExtra("UniqueID",UID);
            startActivity(i);
        }
        if(v == Back) {
            finish();
            startActivity(new Intent(this, Profile.class));
        }
    }
}


