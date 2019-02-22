package com.example.user.recyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class ResultsActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageRef;

    DatabaseReference myRef;
    FirebaseDatabase database;

    private ArrayList<String> namesPlayer1 = new ArrayList<String>();
    private ArrayList<String> namesPlayer2 = new ArrayList<String>();
    private ArrayList<Integer> players1Scored = new ArrayList<Integer>();
    private ArrayList<Integer> players2Scored = new ArrayList<Integer>();

    private ArrayList<Result> results = new ArrayList<Result>();

    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        reset = findViewById(R.id.reset);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        initImageBitmaps();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ResultsActivity.this);
                final String message = "Are you sure you want to reset\n" +
                        "all the results of the tournament?";

                builder.setMessage(message)
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {

                                        myRef.child("results").removeValue();
                                        Toast.makeText(ResultsActivity.this, "All the results removed", Toast.LENGTH_LONG).show();
                                        onBackPressed();

                                        d.dismiss();
                                    }

                                })
                        .setNegativeButton("No, sorry",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        d.cancel();
                                    }
                                });
                builder.create().show();


            }
        });
    }

    private void initImageBitmaps()
    {
        myRef.child("results").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children)
                {
                    Result result = child.getValue(Result.class);
                    results.add(result);
                }
                Collections.reverse(results);
                placement();
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void placement()
    {
        for(int i=0 ; i<results.size() ; i++)
        {
            namesPlayer1.add(results.get(i).getNamePlayer1());
            namesPlayer2.add(results.get(i).getNamePlayer2());
            players1Scored.add(results.get(i).getPlayer1Scored());
            players2Scored.add(results.get(i).getPlayer2Scored());
        }
    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapterResults adapter = new RecyclerViewAdapterResults(this, namesPlayer1, namesPlayer2, players1Scored, players2Scored);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
