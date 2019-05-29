package com.example.user.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapterResults extends RecyclerView.Adapter<RecyclerViewAdapterResults.ViewHolder>{


    private ArrayList<String> namesPlayer1 = new ArrayList<String>();
    private ArrayList<String> namesPlayer2 = new ArrayList<String>();
    private ArrayList<Integer> players1Scored = new ArrayList<Integer>();
    private ArrayList<Integer> players2Scored = new ArrayList<Integer>();
    private Context mContext;


    public RecyclerViewAdapterResults( Context mContext, ArrayList<String> namesPlayer1, ArrayList<String> namesPlayer2, ArrayList<Integer> players1Scored, ArrayList<Integer> players2Scored) {
        this.namesPlayer1 = namesPlayer1;
        this.namesPlayer2 = namesPlayer2;
        this.players1Scored = players1Scored;
        this.players2Scored = players2Scored;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_results, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.playerName1.setText(namesPlayer1.get(position));
        holder.playerName2.setText(namesPlayer2.get(position));
        holder.result.setText("" + players1Scored.get(position) + ":" + players2Scored.get(position));

        if(players1Scored.get(position)>players2Scored.get(position))
        {
            holder.playerName1.setTypeface(null, Typeface.BOLD);
            holder.playerName1.setTextColor(Color.parseColor("#22B14C"));
            holder.playerName2.setTextColor(Color.parseColor("#D70000"));

        }
        if(players1Scored.get(position)<players2Scored.get(position))
        {
            holder.playerName2.setTypeface(null, Typeface.BOLD);
            holder.playerName1.setTextColor(Color.parseColor("#D70000"));
            holder.playerName2.setTextColor(Color.parseColor("#22B14C"));
        }

        if(players1Scored.get(position) == players2Scored.get(position))
        {
            holder.playerName1.setTextColor(Color.parseColor("#D2D200"));
            holder.playerName2.setTextColor(Color.parseColor("#D2D200"));
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

        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            playerName1 = itemView.findViewById(R.id.playerName1);
            result = itemView.findViewById(R.id.result);
            playerName2 = itemView.findViewById(R.id.playerName2);

        }
    }



}
