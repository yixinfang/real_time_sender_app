package edu.neu.madcourse.numad21fa_a7team21days;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.numad21fa_a7team21days.cookbook.Sticker;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.User;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.tools;

public class HistoryActivity extends AppCompatActivity {
    private TextView v;
    private String userName;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rLayoutManger;
    private RviewAdaptor rviewAdaptor;
    private ArrayList<Sticker> resList = new ArrayList<>();

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //init(savedInstanceState);
        v = findViewById(R.id.HistoryView);
        userName = (String) getIntent().getSerializableExtra("userName");
        getUserHistory();
        //v.setText(userName);
       // getUserHistory();
    }

    private void init(Bundle savedInstanceState) {
        initialItemData(savedInstanceState);
        createRecyclerView();
    }
//    private void setRes(int i, String title) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                resList.add(i, new Sticker(title));
//                rviewAdaptor.notifyItemInserted(i);
//            }
//        });
//    }

    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (resList == null || resList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {

                    String id = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String sender = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String sentTime = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                    Sticker item = new Sticker(Integer.parseInt(id), sender, sentTime);

                    resList.add(item);
                }
            }
        }

    }

    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rviewAdaptor = new RviewAdaptor(resList, this::onItemClick);
        rviewAdaptor.setOnItemClickListener(this::onItemClick);
        recyclerView.setAdapter(rviewAdaptor);
        recyclerView.setLayoutManager(rLayoutManger);


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
                        //resList = user.getReceivedSticker();
                    }
                    //sb.append(user.getUserName());
                    Log.i("Get Data", user.getUserName());
                }
                v.setText(sb.toString());
                //v.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //return sb.toString();
    }

    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = resList == null ? 0 : resList.size();
        outState.putInt(NUMBER_OF_ITEMS, size );
        for (int i = 0; i < size; i++) {
            // put image information id into instance
//            outState.putString(KEY_OF_INSTANCE + i + "0", resList.get(i).getUrl());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "0", valueOf(resList.get(i).getStickerId()));
            outState.putString(KEY_OF_INSTANCE + i + "1", resList.get(i).getSender());
            outState.putString(KEY_OF_INSTANCE + i + "2", resList.get(i).getSendTime());
        }
        super.onSaveInstanceState(outState);

    }

    public void onItemClick(int position) {
//        resList.get(position).onItemClick(position);

        rviewAdaptor.notifyItemChanged(position);
    }
}