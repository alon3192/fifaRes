package com.example.user.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterChampionships extends RecyclerView.Adapter<RecyclerViewAdapterChampionships.ViewHolder>{

    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private ArrayList<Integer> mChampinships = new ArrayList<Integer>();
    private ArrayList<String> mClubs = new ArrayList<String>();
    private Context mContext;

    FirebaseStorage storage;
    StorageReference storageRef;

    public RecyclerViewAdapterChampionships(Context mContext, ArrayList<String> mNames, ArrayList<String> mImageUrls, ArrayList<Integer> mChampinships, ArrayList<String> mClubs) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mChampinships = mChampinships;
        this.mClubs = mClubs;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_championship, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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

        holder.name.setText(mNames.get(position));
        holder.championships.setText("" + mChampinships.get(position));

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView image;
        CircleImageView club;
        TextView name;
        TextView championships;

        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            club = itemView.findViewById(R.id.club);
            name = itemView.findViewById(R.id.name);
            championships = itemView.findViewById(R.id.championships);
            parentLayout = itemView.findViewById(R.id.parent_layout_championship);

        }
    }
}
