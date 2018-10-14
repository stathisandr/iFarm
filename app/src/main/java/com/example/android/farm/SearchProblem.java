package com.example.android.farm;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProblem extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    public ArrayList<Problem> problemslist = new ArrayList<Problem>();
    public ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
   public Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_problem);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

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

        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String position = marker.getTag().toString();

                if(position.equals("My_Own_Problem")){
                    return false;
                }
                else{
                    Intent i = new Intent(SearchProblem.this, DisplayProblem.class);
                    String  UID =position;
                    i.putExtra("UniqueID",UID);
                    startActivity(i);
                }


                return false;
            }
        });
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Problems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                // shake hands with each of them.
                for (DataSnapshot child : children) {
                    String name = (String) child.child("name").getValue();
                    String desc = (String) child.child("desc").getValue();
                    String uniqueID = (String) child.child("uniqueID").getValue();
                    String userid = (String) child.child("userid").getValue();
                    double longitude = (double) child.child("longitude").getValue();
                    double latitude = (double) child.child("latitude").getValue();
                    String lux = (String) child.child("lux").getValue();
                    String temp = (String) child.child("temperature").getValue();
                    Problem pro = new Problem(desc, name, userid, uniqueID,longitude,latitude,lux,temp);
                    problemslist.add(pro);
                }
                for (int i =0;i<problemslist.size();i++){
                    LatLng latLng = new LatLng(problemslist.get(i).getLatitude(),problemslist.get(i).getLongitude());

                    if(problemslist.get(i).getUserid().equals(user.getUid())){
                        Marker marker=mMap.addMarker(new MarkerOptions().position(latLng).title(problemslist.get(i).getName()).icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
                        marker.setTag("My_Own_Problem");
                    }

                    else{
                        Marker marker=mMap.addMarker(new MarkerOptions().position(latLng).title(problemslist.get(i).getName()).icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));
                        marker.setTag(problemslist.get(i).getUniqueID());
                    }


                    latLngs.add(latLng);
                }
            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
