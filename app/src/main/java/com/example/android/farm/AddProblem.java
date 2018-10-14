package com.example.android.farm;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import im.delight.android.location.SimpleLocation;

public class AddProblem extends AppCompatActivity implements View.OnClickListener,SensorEventListener  {

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //defining a database reference
    private DatabaseReference databaseReference;


    private Button Submit,buttonMap;


    private EditText editTextName;
    private EditText editTextDesc;
    private ProgressDialog progressDialog;

    //Buttons
    private ImageButton Back;
    private Button buttonChoose;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    private SimpleLocation location;

    SensorManager   sensorManager;
    Sensor sensor_light,sensor_temp;
    TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        sensorManager =(SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor_light=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_temp=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        textview=findViewById(R.id.textview);

        firebaseAuth = FirebaseAuth.getInstance();



        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, Login.class));
        }

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);
        Submit = (Button) findViewById(R.id.Submit);
        buttonMap =(Button) findViewById(R.id.MapButton);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        Back = (ImageButton) findViewById(R.id.back);


        imageView = (ImageView) findViewById(R.id.imageView);

        //attaching listener
        Back.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);
        buttonMap.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        Submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    private void showFileChooser() {
        //Open Intent to choose image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Submit() {
        String name = editTextName.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();
        progressDialog.setMessage("Submitting  Please Wait...");
        progressDialog.show();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String useruid = user.getUid().toString();
        String UniqueIdOfProblem = UUID.randomUUID().toString();


        sensorManager =(SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor_light=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        
        Problem prob = new Problem(desc, name, useruid, UniqueIdOfProblem,location.getLongitude(), location.getLatitude(),textview.getText().toString(),"0");

        //Create db Reference
        databaseReference.child("Problems").child(UniqueIdOfProblem).setValue(prob);


        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            StorageReference riversRef = storageReference.child(UniqueIdOfProblem);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(this, "Problem with the DB", Toast.LENGTH_LONG).show();
        }


        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
        SystemClock.sleep(5000);
        progressDialog.dismiss();

    }


    @Override
    public void onClick(View v) {
        if (v == Submit) {
            Submit();
            finish();
            startActivity(new Intent(this, MyProblems.class));
        }
        if(v == Back) {
            finish();
            startActivity(new Intent(this, Profile.class));
        }
        if (v == buttonChoose) {
            showFileChooser();
        }

        if(v==buttonMap){
            Intent i = new Intent(AddProblem.this, MapsActivity.class);
            i.putExtra("Longitude", location.getLongitude());
            i.putExtra("Latitude", location.getLatitude());
            finish();
            startActivity(i);
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       if(event.sensor.getType() == Sensor.TYPE_LIGHT){
           textview.setText(" "+event.values[0]);
       }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor_light,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensor_temp,SensorManager.SENSOR_DELAY_NORMAL);
    }
}