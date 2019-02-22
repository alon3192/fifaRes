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
import android.widget.TextView;
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
import java.util.List;

public class GeneralResultsActivity extends AppCompatActivity {


    FirebaseStorage storage;
    StorageReference storageRef;

    DatabaseReference myRef;
    FirebaseDatabase database;

    ImageButton matchup;
    Spinner player1, player2;

    TextView wins1, wins2, draws, goals1, goals2;

    private ArrayList<String> namesPlayer1 = new ArrayList<String>();
    private ArrayList<String> namesPlayer2 = new ArrayList<String>();
    private ArrayList<Integer> players1Scored = new ArrayList<Integer>();
    private ArrayList<Integer> players2Scored = new ArrayList<Integer>();

    private ArrayList<Result> results = new ArrayList<Result>();



    List<String> playersName1;
    List<String> playersName2;

    String playerName1;
    String playerName2;
    int countWins1, countWins2, countDraws, sumGoals1, sumGoals2;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_results);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        playersName1 = new ArrayList<String>();
        playersName2 = new ArrayList<String>();

        matchup = (ImageButton)findViewById(R.id.matchup);
        player1 = (Spinner)findViewById(R.id.player1);
        player2 = (Spinner)findViewById(R.id.player2);


        wins1 = (TextView) findViewById(R.id.wins1);
        wins2 = (TextView) findViewById(R.id.wins2);
        draws = (TextView) findViewById(R.id.draws);
        goals1 = (TextView) findViewById(R.id.goals1);
        goals2 = (TextView) findViewById(R.id.goals2);



        Intent i = getIntent();
       playerName1 = i.getStringExtra("playerName1");
        playerName2 = i.getStringExtra("playerName2");


        if(playerName1.equals("none") && playerName2.equals("none"))
            initImageBitmaps();
        else
            displayMatchUp();

        matchup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name1 = player1.getSelectedItem().toString();
                String name2 = player2.getSelectedItem().toString();

                if(name1.equals(name2))
                {
                    Toast.makeText(GeneralResultsActivity.this,"You selected the same name. Please try again!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(GeneralResultsActivity.this, GeneralResultsActivity.class);
                    intent.putExtra("playerName1", name1);
                    intent.putExtra("playerName2", name2);
                    startActivity(intent);
                }
            }
        });


        if(!playerName1.equals("none"))
        {
            playersName1.add(playerName1);
            playersName2.add(playerName2);
        }


        myRef.child("players_general").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int i=0;
                for(DataSnapshot child : children)
                {
                    Player player = child.getValue(Player.class);

                    if(!player.getName().equals(playerName1))
                    {
                        playersName1.add(player.getName());
                    }
                    if(!player.getName().equals(playerName2))
                    {
                        playersName2.add(player.getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(GeneralResultsActivity.this, android.R.layout.simple_spinner_item, playersName1);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                player1.setAdapter(adapter);

                adapter = new ArrayAdapter<String>(GeneralResultsActivity.this, android.R.layout.simple_spinner_item, playersName2);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                player2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayMatchUp()
    {
        results = new ArrayList<Result>();
        myRef.child("general_results").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children)
                {

                    Result result = child.getValue(Result.class);

                    if((result.getNamePlayer1().equals(playersName1.get(0)) && result.getNamePlayer2().equals(playersName2.get(0))))
                    {
                        results.add(result);
                        if(result.getPlayer1Scored()>result.getPlayer2Scored())
                        {
                            countWins1++;
                        }
                        else if(result.getPlayer1Scored()<result.getPlayer2Scored())
                        {
                            countWins2++;
                        }
                        else
                        {
                            countDraws++;
                        }
                        sumGoals1+=result.getPlayer1Scored();
                        sumGoals2+=result.getPlayer2Scored();
                        i++;
                    }
                    if(result.getNamePlayer2().equals(playersName1.get(0)) && result.getNamePlayer1().equals(playersName2.get(0)))
                    {
                        results.add(result);
                        if(result.getPlayer1Scored()>result.getPlayer2Scored())
                        {
                            countWins2++;
                        }
                        else if(result.getPlayer1Scored()<result.getPlayer2Scored())
                        {
                            countWins1++;
                        }
                        else
                        {
                            countDraws++;
                        }
                        sumGoals2+=result.getPlayer1Scored();
                        sumGoals1+=result.getPlayer2Scored();
                        i++;
                    }
                }

                wins1.setText("" + countWins1);
                wins2.setText("" + countWins2);
                draws.setText("" + countDraws);
                goals1.setText("" + sumGoals1);
                goals2.setText("" + sumGoals2);

                Collections.reverse(results);
                placement();
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initImageBitmaps()
    {

        myRef.child("general_results").addListenerForSingleValueEvent(new ValueEventListener() {
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
