package com.example.allison.bigfootsmallfoot;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        Button vehicleBackButton = (Button) findViewById(R.id.vehicleBackButton);
        vehicleBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleActivity.this, Dashboard.class));
            }
        });
    }

    public void onSaveClick (View v) {
        EditText vehicle1 = (EditText)findViewById(R.id.vehicle1Text);
        String vehicle1String = vehicle1.getText().toString();
        EditText mpg1 = (EditText)findViewById(R.id.mpg1Text);
        String mpg1String = mpg1.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("/users/test/user1/vehicle1/vehicle");
        myRef1.setValue(vehicle1String);
        DatabaseReference myRef2 = database.getReference("/users/test/user1/vehicle1/mpg");
        myRef2.setValue(mpg1String);
    }
}
