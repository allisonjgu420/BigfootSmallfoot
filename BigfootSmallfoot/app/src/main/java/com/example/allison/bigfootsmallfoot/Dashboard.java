package com.example.allison.bigfootsmallfoot;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.GenericTypeIndicator;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private boolean getLocation;
    private ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
    private ArrayList<Double> emissions = new ArrayList<Double>();
    private int mpg = 20;
    ImageView drawingImageView;
    //boolean editGoalClicked;
    int count = 0;
    int checkbox = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, MainActivity.class));
            }
        });

        Button vehicleButton = (Button) findViewById(R.id.vehicleButton);
        vehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (Dashboard.this, VehicleActivity.class));
            }
        });

        Button editGoalsButton = (Button) findViewById(R.id.editGoalsButton);
        editGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, SetGoals.class));
            }
        });

        Button locationButton = (Button) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Dashboard.this, LocationFinder.class));
                //if null - start, else stop for button click on and off
                if (getLocation == false) {
                    startLocationUpdates();
                    //onLocationChanged(Location location);
                }
                else {
                    stopLocationUpdates();
                    double dist = findDistance();
                    double emission = calculateEmission();
                    Toast.makeText(getApplicationContext(), "distance (mi) " + dist, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "CO2 emissions (lbs) " + emission, Toast.LENGTH_SHORT).show();

                    emissions.add(emission);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users/user1/emissions");


                    myRef.setValue(emissions, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(getApplicationContext(), "in oncomplete", Toast.LENGTH_SHORT).show();
                                    if (databaseError != null) {
                                        Toast.makeText(getApplicationContext(), "data could not be saved " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
//                    myRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            //GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator
//                            Object emissions = dataSnapshot.getValue();
//                            Toast.makeText(getApplicationContext(), "value " + emissions, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });


                            coordinates.clear();
                    //convert to miles per gallon
                    // save to firebase
                }
            }
        });


    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            //startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        /* 10 secs */
        getLocation = true;
        long UPDATE_INTERVAL = 2 * 1000;
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

        Log.d("reque", "--->>>>");
    }

    protected void stopLocationUpdates() {
        getLocation = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());



        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        coordinates.add(latLng);
    }

    protected double findDistance () {
        double totalDistance = 0;
        for (int i = 0; i < coordinates.size()-1; i++) {
            totalDistance += distance(coordinates.get(i).latitude, coordinates.get(i+1).latitude,
                    coordinates.get(i).longitude, coordinates.get(i+1).longitude);

        }
        return totalDistance;

    }

    protected double calculateEmission () {
        double dist = findDistance();
        double emission = dist / mpg ;
        return emission;
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        distance = Math.sqrt(distance);
        return (distance / 1609.344); //converts to miles
    }


    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//                startActivity(new Intent(Dashboard.this, VehicleActivity.class));
//            }
//        });

    }



    public void onNewGoalClick(View v) {

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/test/goals");

        //ref.setValue("Hello, World!");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                count++;
                //editGoalClicked = true;

                // Prints all the values and goals in the database
                System.out.println(dataSnapshot);

                // Specifies that array list is storing strings and not some other type, e.g. integers, doubles
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                };

                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("christine", 23);
                int age = map.get("christine");
                ArrayList<String> messages = dataSnapshot.getValue(t);

                //Prints out all the goals in the database
                System.out.println(messages);

                //Prints out one specific goal

                System.out.println(messages.get(1));


                TextView goal1Text = (TextView) findViewById(R.id.goal1Text);
                goal1Text.setVisibility(View.INVISIBLE);

                final CheckBox goal1CheckBox = (CheckBox) findViewById(R.id.goal1CheckBox);
                final CheckBox goal2CheckBox = (CheckBox) findViewById(R.id.goal2CheckBox);
                final CheckBox goal3CheckBox = (CheckBox) findViewById(R.id.goal3CheckBox);

                //Set first checkbox
                if (count == 1) {

                    goal1CheckBox.setVisibility(View.VISIBLE);
                    goal1CheckBox.setText(messages.get(count));
                }

                //Set second checkbox
                if (count == 2) {
                    goal2CheckBox.setVisibility(View.VISIBLE);
                    goal2CheckBox.setText(messages.get(count));
                }

                //Set third checkbox
                if (count == 3) {
                    goal3CheckBox.setVisibility(View.VISIBLE);
                    goal3CheckBox.setText(messages.get(count));
                    //count = 0;
                }

                // If first checkbox is checked
                goal1CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    //recording the click of the checkbox
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            goal1CheckBox.setText(goal2CheckBox.getText());
                            goal2CheckBox.setText(goal3CheckBox.getText());
                            goal3CheckBox.setVisibility(View.INVISIBLE);
                            checkbox = 3;
                        }


                    }
                });

                // If second checkbox is checked
                goal2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    //recording the click of the checkbox
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            goal2CheckBox.setText(goal3CheckBox.getText());
                            goal3CheckBox.setVisibility(View.INVISIBLE);
                            checkbox = 3;
                        }
                    }
                });

                // If third checkbox is checked
                goal3CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    //recording the click of the checkbox
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            goal3CheckBox.setVisibility(View.INVISIBLE);
                            checkbox = 3;
                        }
                    }
                });

                // Set third checkbox if first checkbox is checked
                if (checkbox == 3) {
                    goal3CheckBox.setVisibility(View.VISIBLE);
                    goal3CheckBox.setText(messages.get(count));
                    checkbox = 0;
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
    }

    public void onGraphsClick (View v) {
        startActivity(new Intent(Dashboard.this, Graphs.class));
    }
}
