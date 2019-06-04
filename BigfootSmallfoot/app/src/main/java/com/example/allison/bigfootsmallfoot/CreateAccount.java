package com.example.allison.bigfootsmallfoot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void onCreateClick (View v) {
        EditText username = (EditText)findViewById(R.id.createdUsername);;
        EditText password = (EditText)findViewById(R.id.createdPassword);
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        TextView passwordError = (TextView)findViewById(R.id.passwordErrorTextView);

        if (passwordString.length() < 7 ) {
            passwordError.setText("Password must be longer than 6 characters.");
        } else if (passwordString.equals(passwordString.toLowerCase())) {
            passwordError.setText("Password must contain at least one upper case letter.");
        } else if (!passwordString.matches(".*\\d.*")) {
            passwordError.setText("Password must contain at least one number.");
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef1 = database.getReference("/users/test/user1/username");
            myRef1.setValue(usernameString);
            DatabaseReference myRef2 = database.getReference("/users/test/user1/password");
            myRef2.setValue(passwordString);

            startActivity(new Intent(CreateAccount.this, Dashboard.class));
        }
    }

    public void onBackButtonClick (View v) {
        startActivity(new Intent(CreateAccount.this, MainActivity.class));
    }
}
