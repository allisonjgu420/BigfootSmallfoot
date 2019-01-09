package com.example.allison.bigfootsmallfoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

       // Button editGoalsButton = (Button) findViewById(R.id.editGoalsButton);
       // editGoalsButton.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {
           //     startActivity(new Intent(Dashboard.this, SetGoals.class));
        //    }
       // });


    }

    public void onEditGoalsClick(View v) {
        startActivity(new Intent(Dashboard.this, SetGoals.class));

    }

    public void onNewGoalClick(View v){

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/test/goals");

        //ref.setValue("Hello, World!");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Prints all the values and goals in the database
                System.out.println(dataSnapshot);

                // Specifies that array list is storing strings and not some other type, e.g. integers, doubles
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> messages = dataSnapshot.getValue(t);

                //Prints out all the goals in the database
                System.out.println(messages);

                //Prints out one specific goal
                System.out.println(messages.get(1));

                TextView goal1 = (TextView)findViewById(R.id.goal1Text);
                goal1.setText(messages.get(1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
    }
}