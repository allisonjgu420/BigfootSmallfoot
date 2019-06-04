package com.example.allison.bigfootsmallfoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.Dash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // Button loginButton = (Button) findViewById(R.id.loginButton);




       /* loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Dashboard.class));
            }
        });*/
    }

    public void onLoginClick (View v) {
        startActivity(new Intent(MainActivity.this, Dashboard.class));
    }

    public void onCreateAccountClick (View v) {
        startActivity(new Intent(MainActivity.this, CreateAccount.class));
    }
}
