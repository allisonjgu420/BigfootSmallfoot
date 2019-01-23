package com.example.allison.bigfootsmallfoot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TransportationInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

    private DatabaseReference mDatabase;
// ...
    mDatabase = FirebaseDatabase.getInstance().getReference();


@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    mDatabase.child("users").child(userId).child("username").setValue(name);



//https://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
//https://www.captechconsulting.com/blogs/firebase-realtime-database-android-tutorial
//https://stackoverflow.com/questions/8743349/hardcoded-string-row-three-should-use-string-resource