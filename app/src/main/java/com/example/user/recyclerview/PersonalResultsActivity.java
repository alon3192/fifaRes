package com.example.user.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonalResultsActivity extends AppCompatActivity {

    Spinner player;
    DatabaseReference myRef;
    FirebaseDatabase database;
    List<String> playersName;
    ImageButton search;
    private ArrayList<String> namesPlayer1 = new ArrayList<String>();
    private ArrayList<String> namesPlayer2 = new ArrayList<String>();
    private ArrayList<Integer> players1Scored = new ArrayList<Integer>();
    private ArrayList<Integer> players2Scored = new ArrayList<Integer>();
    private ArrayList<String> states = new ArrayList<String>();
    private ArrayList<Result> results = new ArrayList<Result>();
    String playerToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_results);

        player = (Spinner) findViewById(R.id.player);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        playersName = new ArrayList<String>();
        search = (ImageButton) findViewById(R.id.search);

        Intent i = getIntent();
        playerToSearch = i.getStringExtra("playerToSearch");

        if(!playerToSearch.equals("none"))
        {
            displayPersonalResults();
            playersName.add(playerToSearch);
        }

        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int i=0;
                for(DataSnapshot child : children)
                {
                    Player player = child.getValue(Player.class);
                    if(!player.getName().equals(playerToSearch))
                    {
                        playersName.add(player.getName());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PersonalResultsActivity.this, android.R.layout.simple_spinner_item, playersName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                player.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String playerToSearch = player.getSelectedItem().toString();

                Intent intent = new Intent(PersonalResultsActivity.this, PersonalResultsActivity.class);
                intent.putExtra("playerToSearch", playerToSearch);
                startActivity(intent);

                 }});

    }

    private void displayPersonalResults()
    {
        results = new ArrayList<Result>();

        myRef.child("general_results").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int i=0;
                for(DataSnapshot child : children)
                {
                    Result result = child.getValue(Result.class);
                    if(result.getNamePlayer1().equals(playerToSearch) || result.getNamePlayer2().equals(playerToSearch))
                    {
                        results.add(result);
                    }
                }
                Collections.reverse(results);
                placement(results, playerToSearch);
                initRecyclerView();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});
    }

    private void placement(ArrayList<Result> results, String playerToSearch)
    {
        for(int i=0 ; i<results.size() ; i++) {
            namesPlayer1.add(results.get(i).getNamePlayer1());
            namesPlayer2.add(results.get(i).getNamePlayer2());
            players1Scored.add(results.get(i).getPlayer1Scored());
            players2Scored.add(results.get(i).getPlayer2Scored());

            if (results.get(i).getNamePlayer1().equals(playerToSearch) && results.get(i).getPlayer1Scored() > results.get(i).getPlayer2Scored()) {
                states.add("win");
            } else if (results.get(i).getNamePlayer2().equals(playerToSearch) && results.get(i).getPlayer1Scored() < results.get(i).getPlayer2Scored()) {
                states.add("win");
            } else if (results.get(i).getNamePlayer1().equals(playerToSearch) && results.get(i).getPlayer1Scored() < results.get(i).getPlayer2Scored()) {
                states.add("lose");
            } else if (results.get(i).getNamePlayer2().equals(playerToSearch) && results.get(i).getPlayer1Scored() > results.get(i).getPlayer2Scored()) {
                states.add("lose");
            } else if (results.get(i).getNamePlayer1().equals(playerToSearch) && results.get(i).getPlayer1Scored() == results.get(i).getPlayer2Scored()) {
                states.add("draw");
            } else if (results.get(i).getNamePlayer2().equals(playerToSearch) && results.get(i).getPlayer1Scored() == results.get(i).getPlayer2Scored()) {
                states.add("draw");
            }
        }
    }

    private void initRecyclerView()
    {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapterPersonal adapter = new RecyclerViewAdapterPersonal(this, namesPlayer1, namesPlayer2, players1Scored, players2Scored, states);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
