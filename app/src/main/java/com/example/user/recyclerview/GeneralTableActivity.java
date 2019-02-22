package com.example.user.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GeneralTableActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageRef;

    DatabaseReference myRef;
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



    ImageButton res, tournament, personal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_table);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        res = (ImageButton) findViewById(R.id.results);
        tournament = (ImageButton)findViewById(R.id.tournament);
        personal = (ImageButton) findViewById(R.id.personal);

        initImageBitmaps();

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneralTableActivity.this, PersonalResultsActivity.class);
                intent.putExtra("playerToSearch", "none");
                startActivity(intent);
            }
        });


        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GeneralTableActivity.this, GeneralResultsActivity.class);
                intent.putExtra("playerName1", "none");
                intent.putExtra("playerName2", "none");
                startActivity(intent);
            }
        });

        tournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GeneralTableActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initImageBitmaps()
    {

        myRef.child("players_general").addListenerForSingleValueEvent(new ValueEventListener() {
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
            mClubs.add(players.get(i).getClubPath());


            mImageUrls.add(players.get(i).getImagePath());
        }
    }
}
