package com.example.user.recyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FirebaseStorage storage;
    StorageReference storageRef;

    DatabaseReference myRef;
    DatabaseReference myRef2;
    FirebaseDatabase database;

    private ArrayList<Player> players = new ArrayList<Player>();

    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private ArrayList<Integer> mGames = new ArrayList<Integer>();
    private ArrayList<Integer> mWins = new ArrayList<Integer>();
    private ArrayList<Integer> mDraws = new ArrayList<Integer>();
    private ArrayList<Integer> mLosses = new ArrayList<Integer>();
    private ArrayList<Integer> mGoals_scored = new ArrayList<Integer>();
    private ArrayList<Integer> mGoals_got = new ArrayList<Integer>();
    private ArrayList<Integer> mDifference = new ArrayList<Integer>();
    private ArrayList<Integer> mScoring = new ArrayList<Integer>();
    private ArrayList<String> mClubs = new ArrayList<String>();

    ImageButton reset;
    ImageButton write_result;
    ImageButton res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "oncreate: started");


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef2 = database.getReference();

        reset = (ImageButton) findViewById(R.id.reset);
        write_result = (ImageButton) findViewById(R.id.write_result);
        res = (ImageButton) findViewById(R.id.results);

        write_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String message = "Are you sure you want to reset\n" +
                        "the tournament table and give a champinship to " + mNames.get(0) + "?";

                builder.setMessage(message)
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {

                                        updateChampion();


                                        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                                for(DataSnapshot child : children)
                                                {
                                                    String key = child.getKey();
                                                    Player player = child.getValue(Player.class);
                                                    player.resetData();
                                                    myRef.child("players").child(key).setValue(player);
                                                }
                                                finish();
                                                startActivity(getIntent());
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

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
        initImageBitmaps();
    }

    private void initImageBitmaps()
    {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");


        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children)
                {
                    Player player = child.getValue(Player.class);
                    players.add(player);
                }
                sortByScore();
                placement();
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView()
    {
        Log.d(TAG, "initRecyclerView: init recyclerView.");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mClubs, mNames, mImageUrls, mGames, mWins, mDraws, mLosses, mGoals_scored, mGoals_got, mDifference, mScoring);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



    private void sortByScore()
    {
        Player playerTmp;
        for(int i=0 ; i<players.size() ; i++)
        {
            for(int j=i ; j<players.size() ; j++)
            {
                if(players.get(i).getScoring()<players.get(j).getScoring())
                {
                    playerTmp = players.get(i);
                    players.set(i,players.get(j));
                    players.set(j, playerTmp);
                }
                else if(players.get(i).getScoring() == players.get(j).getScoring())
                {
                    if(players.get(i).getDifference()<players.get(j).getDifference())
                    {
                        playerTmp = players.get(i);
                        players.set(i,players.get(j));
                        players.set(j, playerTmp);
                    }
                    else if(players.get(i).getDifference() == players.get(j).getDifference())
                    {
                        if(players.get(i).getGoals_scored()<players.get(j).getGoals_scored())
                        {
                            playerTmp = players.get(i);
                            players.set(i,players.get(j));
                            players.set(j, playerTmp);
                        }
                    }
                }
            }
        }
    }

    private void placement()
    {
       // mImageUrls.add("http://xeber100.com/wp-content/uploads/2018/07/6-3.jpg");
       // mNames.add("Liverpool");



        for(int i=0 ; i<players.size() ; i++)
        {
           // mImageUrls.add("http://xeber100.com/wp-content/uploads/2018/07/6-3.jpg");
            mNames.add(players.get(i).getName());
            mGames.add(players.get(i).getGames());
            mWins.add(players.get(i).getWins());
            mDraws.add(players.get(i).getDraws());
            mLosses.add(players.get(i).getLosses());
            mGoals_scored.add(players.get(i).getGoals_scored());
            mGoals_got.add(players.get(i).getGoals_got());
            mDifference.add(players.get(i).getDifference());
            mScoring.add(players.get(i).getScoring());
            mImageUrls.add(players.get(i).getImagePath());
            mClubs.add(players.get(i).getClubPath());
        }
    }

    private void updateChampion()
    {


        final String name = mNames.get(0);

        myRef2.child("players_general").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children)
                {
                    String key = child.getKey();
                    Player player = child.getValue(Player.class);
                    if(player.getName().equals(name))
                    {
                        int n = player.getChampionship();
                        n++;
                        player.setChampionship(n);
                        myRef2.child("players_general").child(key).setValue(player);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});
    }



}
