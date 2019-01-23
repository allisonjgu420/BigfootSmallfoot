package com.example.allison.bigfootsmallfoot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    ImageView drawingImageView;
    //boolean editGoalClicked;
    int count = 0;
    int checkbox = 0;
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

    }

    //public void onEditGoalsClick(View v) {
      //  startActivity(new Intent(Dashboard.this, SetGoals.class));

//    }

    public void onNewGoalClick(View v){

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
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> messages = dataSnapshot.getValue(t);

                //Prints out all the goals in the database
                System.out.println(messages);

                //Prints out one specific goal

                System.out.println(messages.get(1));


                TextView goal1Text = (TextView)findViewById(R.id.goal1Text);
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
}