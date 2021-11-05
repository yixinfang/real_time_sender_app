package edu.neu.madcourse.numad21fa_a7team21days;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText userName;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.userNameText);
        login = findViewById(R.id.loginButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                // get the userName
                String userGivenName = userName.getText().toString().trim();

                // make sure not empty user name
                if (userGivenName.length() == 0 || userGivenName.equals(null)){
                    Toast.makeText(getApplicationContext(),"not accept empty user name",Toast.LENGTH_SHORT).show();
                    break;
                }
                // start login
                startLogin(userGivenName);
                Toast.makeText(getApplicationContext(),"start.login",Toast.LENGTH_SHORT).show();

                // Go to send sticker activity
                startSendSticker(view, userGivenName);
                break;
            default:
                break;
        }
    }

    public void startLogin(String userGivenName) {
        DatabaseReference mUser= mDatabase.child("users");
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            // check if the user exist in the database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if "user" has a child of userGivenName
                if(snapshot.hasChild(userGivenName)){
                    Toast.makeText(getApplicationContext(),"user exist",Toast.LENGTH_SHORT).show();
                // if not create the user
                }else{
                    signUpUser(userGivenName);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void signUpUser(String userGivenName){
        // create new user
        User user = new User(userGivenName);
        mDatabase.child("users").child(userGivenName).setValue(user);
    }

    public void startSendSticker(View view, String userGivenName){
        Intent intent = new Intent(this, SendStickerActivity.class);
        intent.putExtra("userName", userGivenName);
        startActivity(intent);
    }
}