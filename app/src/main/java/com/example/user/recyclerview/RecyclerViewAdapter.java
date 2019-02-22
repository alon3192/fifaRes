package com.example.user.recyclerview;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.recyclerview.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

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
    private Context mContext;

    FirebaseStorage storage;
    StorageReference storageRef;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mClubs, ArrayList<String> mNames, ArrayList<String> mImageUrls, ArrayList<Integer> mGames, ArrayList<Integer> mWins, ArrayList<Integer> mDraws, ArrayList<Integer> mLosses, ArrayList<Integer> mGoals_scored, ArrayList<Integer> mGoals_got, ArrayList<Integer> mDifference, ArrayList<Integer> mScoring) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mGames = mGames;
        this.mWins = mWins;
        this.mDraws = mDraws;
        this.mLosses = mLosses;
        this.mGoals_scored = mGoals_scored;
        this.mGoals_got = mGoals_got;
        this.mDifference = mDifference;
        this.mScoring = mScoring;
        this.mContext = mContext;
        this.mClubs = mClubs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        Log.d(TAG, "onBindViewHolder : called.");



        if(position==0)
        {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#D5FFAA"));
        }
        if(position==mNames.size()-1)
        {
            holder.parentLayout.setBackgroundColor(Color.parseColor("#FFBFBF"));
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        String s = storageRef.child(mImageUrls.get(position)).toString();


        storageRef.child(mImageUrls.get(position)).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mContext).load(uri).into(holder.image);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        holder.image.setImageResource(android.R.drawable.stat_notify_error);
                    }
                });

        storageRef.child(mClubs.get(position)).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mContext).load(uri).into(holder.club);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        holder.club.setImageResource(android.R.drawable.stat_notify_error);
                    }
                });




        //Glide.with(mContext).asBitmap().load(mImageUrls.get(position)).into(holder.image);
        holder.imageName.setText(mNames.get(position));
        holder.games.setText("" + mGames.get(position));
        holder.wins.setText("" + mWins.get(position));
        holder.draws.setText("" + mDraws.get(position));
        holder.losses.setText("" + mLosses.get(position));
        holder.goals.setText("" + mGoals_scored.get(position) + "-" + mGoals_got.get(position));

        if(mDifference.get(position)>0)
        {
            holder.difference.setText("+" + mDifference.get(position));
            holder.difference.setTextColor(Color.parseColor("#80CA00"));
        }

        else if(mDifference.get(position)==0)
        {
            holder.difference.setText("" + mDifference.get(position));
            holder.difference.setTextColor(Color.parseColor("#000000"));
        }


        else if(mDifference.get(position)<0)
        {
            holder.difference.setText("" + mDifference.get(position));
            holder.difference.setTextColor(Color.parseColor("#FF0000"));
        }


        holder.scoring.setText("" + mScoring.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onclick: clicked on: " + mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView image;
        CircleImageView club;
        TextView imageName;
        TextView games;
        TextView wins;
        TextView draws;
        TextView losses;
        TextView goals;
        TextView difference;
        TextView scoring;
        RelativeLayout parentLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            club = itemView.findViewById(R.id.club);
            imageName = itemView.findViewById(R.id.image_name);
            games = itemView.findViewById(R.id.games);
            wins = itemView.findViewById(R.id.wins);
            draws = itemView.findViewById(R.id.draws);
            losses = itemView.findViewById(R.id.losses);
            goals = itemView.findViewById(R.id.goals);
            difference = itemView.findViewById(R.id.difference);
            scoring = itemView.findViewById(R.id.scoring);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
