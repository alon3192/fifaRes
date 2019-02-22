package com.example.user.recyclerview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapterPersonal extends RecyclerView.Adapter<RecyclerViewAdapterPersonal.ViewHolder>{

    private ArrayList<String> namesPlayer1 = new ArrayList<String>();
    private ArrayList<String> namesPlayer2 = new ArrayList<String>();
    private ArrayList<Integer> players1Scored = new ArrayList<Integer>();
    private ArrayList<Integer> players2Scored = new ArrayList<Integer>();
    private ArrayList<String> states = new ArrayList<String>();
    private Context mContext;


    public RecyclerViewAdapterPersonal(Context mContext, ArrayList<String> namesPlayer1, ArrayList<String> namesPlayer2, ArrayList<Integer> players1Scored, ArrayList<Integer> players2Scored, ArrayList<String> states) {
        this.namesPlayer1 = namesPlayer1;
        this.namesPlayer2 = namesPlayer2;
        this.players1Scored = players1Scored;
        this.players2Scored = players2Scored;
        this.states = states;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_personal, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterPersonal.ViewHolder holder, int position) {
        holder.playerName1.setText(namesPlayer1.get(position));
        holder.playerName2.setText(namesPlayer2.get(position));
        holder.result.setText("" + players1Scored.get(position) + ":" + players2Scored.get(position));


        if(players1Scored.get(position)>players2Scored.get(position))
        {
            holder.playerName1.setTypeface(null, Typeface.BOLD);
        }
        if(players1Scored.get(position)<players2Scored.get(position))
        {
            holder.playerName2.setTypeface(null, Typeface.BOLD);
        }

        if(states.get(position).equals("win"))
        {
            holder.state.setImageResource(R.drawable.win);
        }
        else if(states.get(position).equals("lose"))
        {
            holder.state.setImageResource(R.drawable.lose);
        }
        else
        {
            holder.state.setImageResource(R.drawable.draw);
        }

    }

    @Override
    public int getItemCount() {
        return namesPlayer1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView playerName1;
        TextView result;
        TextView playerName2;
        ImageView state;

        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            playerName1 = itemView.findViewById(R.id.playerName1);
            result = itemView.findViewById(R.id.result);
            playerName2 = itemView.findViewById(R.id.playerName2);
            state = itemView.findViewById(R.id.state);

        }
    }
}
