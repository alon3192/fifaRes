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

public class ChampoinshipTableActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageRef;

    DatabaseReference myRef;
    FirebaseDatabase database;

    ImageButton general_table, tournament;

    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private ArrayList<Integer> mChampinships = new ArrayList<Integer>();
    private ArrayList<String> mClubs = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champoinship_table);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        general_table = (ImageButton) findViewById(R.id.general_table);
        tournament = (ImageButton) findViewById(R.id.tournament);

        general_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChampoinshipTableActivity.this, GeneralTableActivity.class);
                startActivity(intent);
            }
        });

        tournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChampoinshipTableActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        initImageBitmaps();
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

    private void sortByScore()
    {
        Player playerTmp;
        for(int i=0 ; i<players.size() ; i++)
        {
            for(int j=i ; j<players.size() ; j++)
            {
                if(players.get(i).getChampionship()<players.get(j).getChampionship())
                {
                    playerTmp = players.get(i);
                    players.set(i,players.get(j));
                    players.set(j, playerTmp);
                }

            }
        }
    }
    private void placement()
    {
        for(int i=0 ; i<players.size() ; i++)
        {
            mNames.add(players.get(i).getName());
            mChampinships.add(players.get(i).getChampionship());
            mClubs.add(players.get(i).getClubPath());
            mImageUrls.add(players.get(i).getImagePath());
        }
    }

    private void initRecyclerView()
    {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapterChampionships adapter = new RecyclerViewAdapterChampionships(this, mNames, mImageUrls, mChampinships, mClubs);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.height = getHeight() / 6;
                return true;
            }
        });
    }

}
