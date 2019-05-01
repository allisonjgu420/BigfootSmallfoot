package com.example.allison.bigfootsmallfoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Dashboard.class));
            }
        });*/
    }


    /*DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
DatabaseReference animalsRef = rootRef.child("Animals");
ValueEventListener eventListener = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Boolean found;
        String search = "Animals";
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String movieName = ds.child("movieName").getValue(String.class);
            found = movieName.contains(search);
            Log.d("TAG", movieName + " / " + found);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
};
animalsRef.addListenerForSingleValueEvent(eventListener);

*/

   /* public void onLoginClick (View v) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("/users/test");

        if (1 == 1) {
            myRef1.

        } else {
            startActivity(new Intent(MainActivity.this, Dashboard.class));
        }
    }
    public void onCreateAccountClick (View v) {
        startActivity(new Intent(MainActivity.this, CreateAccount.class));
    }
    */

}
