package edu.neu.madcourse.numad21fa_a7team21days;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity {
    private TextView v;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        v = findViewById(R.id.HistoryView);
        userName = (String) getIntent().getSerializableExtra("userName");
        getUserHistory();
        //v.setText(userName);
       // getUserHistory();
    }


    private void getUserHistory() {
        StringBuilder sb = new StringBuilder();
        tools.mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
//                    sb.append(user.getUserName()).append("\n");
                    if (user.getUserName().equals(userName)) {
                        sb.append(user.toString());
                    }
                    //sb.append(user.getUserName());
                    Log.i("Get Data", user.getUserName());
                }
                v.setText(sb.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //return sb.toString();
    }
}