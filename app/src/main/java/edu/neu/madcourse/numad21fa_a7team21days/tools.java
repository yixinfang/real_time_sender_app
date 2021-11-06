package edu.neu.madcourse.numad21fa_a7team21days;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class tools {
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
}
