package com.example.user.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity {

    TextView result1, result2;
    Spinner player1, player2;
    Button send;

    DatabaseReference myRef, myRef2;
    FirebaseDatabase database;

    List <String> playersName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef2 = database.getReference();

        result1 = (TextView)findViewById(R.id.result1);
        result2 = (TextView) findViewById(R.id.result2);
        player1 = (Spinner )findViewById(R.id.player1);
        player2 = (Spinner) findViewById(R.id.player2);
        send = (Button) findViewById(R.id.send);

        playersName = new ArrayList<String>();


        myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int i=0;
                for(DataSnapshot child : children)
                {
                    Player player = child.getValue(Player.class);
                    playersName.add(player.getName());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WriteActivity.this, android.R.layout.simple_spinner_item, playersName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                player1.setAdapter(adapter);
                player2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag=true;

                if(result1.getText().toString().equals("") || result2.getText().toString().equals(""))
                {
                    Toast.makeText(WriteActivity.this,"Wrong Input!", Toast.LENGTH_SHORT).show();
                    flag=false;
                }

                if(player1.getSelectedItem().toString().equals(player2.getSelectedItem().toString()))
                {
                    Toast.makeText(WriteActivity.this,"The same player was chosen!", Toast.LENGTH_SHORT).show();
                    flag=false;
                }
                if(flag)
                {
                    final int r1 = Integer.parseInt(result1.getText().toString());
                    final int r2 = Integer.parseInt(result2.getText().toString());

                    final String name1 = player1.getSelectedItem().toString();
                    final String name2 = player2.getSelectedItem().toString();

                    Result result = new Result(name1, name2, r1, r2);
                    String key = myRef.push().getKey();
                    myRef.child("results").child(key).setValue(result);
                    myRef.child("general_results").child(key).setValue(result);


                    myRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                             Player p1, p2;
                            for(DataSnapshot child : children)
                            {
                                Player player = child.getValue(Player.class);
                                if(player.getName().equals(name1))
                                {
                                    p1 = player;

                                    p1.gamesUpdate();
                                    p1.goalsScoredUpdate(r1);
                                    p1.goalsGotUpdate(r2);
                                    p1.differenceUpdate((r1-r2));

                                    if(r1>r2)
                                    {
                                        p1.winsUpdate();
                                        p1.scoringUpdate(3);
                                    }
                                    else if(r1<r2)
                                    {
                                        p1.lossesUpdate();
                                    }

                                    else
                                    {
                                        p1.drawsUpdate();
                                        p1.scoringUpdate(1);
                                    }

                                    String key = child.getKey();
                                    myRef2.child("players").child(key).setValue(p1);


                                }
                                if(player.getName().equals(name2))
                                {
                                    p2 = player;

                                    p2.gamesUpdate();
                                    p2.goalsScoredUpdate(r2);
                                    p2.goalsGotUpdate(r1);
                                    p2.differenceUpdate(r2-r1);

                                    if(r2>r1)
                                    {
                                        p2.winsUpdate();
                                        p2.scoringUpdate(3);
                                    }

                                    else if(r2<r1)
                                    {
                                        p2.lossesUpdate();
                                    }
                                    else
                                    {
                                        p2.drawsUpdate();
                                        p2.scoringUpdate(1);
                                    }

                                    String key = child.getKey();
                                    myRef2.child("players").child(key).setValue(p2);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }});


                    myRef.child("players_general").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            Player p1, p2;
                            for(DataSnapshot child : children)
                            {
                                Player player = child.getValue(Player.class);
                                if(player.getName().equals(name1))
                                {
                                    p1 = player;

                                    p1.gamesUpdate();
                                    p1.goalsScoredUpdate(r1);
                                    p1.goalsGotUpdate(r2);
                                    p1.differenceUpdate((r1-r2));

                                    if(r1>r2)
                                    {
                                        p1.winsUpdate();
                                        p1.scoringUpdate(3);
                                    }
                                    else if(r1<r2)
                                    {
                                        p1.lossesUpdate();
                                    }

                                    else
                                    {
                                        p1.drawsUpdate();
                                        p1.scoringUpdate(1);
                                    }

                                    String key = child.getKey();
                                    myRef2.child("players_general").child(key).setValue(p1);


                                }
                                if(player.getName().equals(name2))
                                {
                                    p2 = player;

                                    p2.gamesUpdate();
                                    p2.goalsScoredUpdate(r2);
                                    p2.goalsGotUpdate(r1);
                                    p2.differenceUpdate(r2-r1);

                                    if(r2>r1)
                                    {
                                        p2.winsUpdate();
                                        p2.scoringUpdate(3);
                                    }

                                    else if(r2<r1)
                                    {
                                        p2.lossesUpdate();
                                    }
                                    else
                                    {
                                        p2.drawsUpdate();
                                        p2.scoringUpdate(1);
                                    }

                                    String key = child.getKey();
                                    myRef2.child("players_general").child(key).setValue(p2);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }});

                }
                if(flag)
                {
                    Toast.makeText(WriteActivity.this, "The result was recorded", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

            }

        });


    }
}
