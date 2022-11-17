package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Playlistfiles;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistListViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.Playlistitem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRecyclarView extends RecyclerView.Adapter<PlaylistRecyclarView.MyViewHolder> {
    ArrayList<Playlist> arrayList= new ArrayList<Playlist>();
    Context c;

    Playlistitem playlistitem;
    public PlaylistRecyclarView(Context c1, ArrayList<Playlist> arrayList, Playlistitem playlistitem1) {

        this.c= c1;
        this.arrayList = arrayList;
        this.playlistitem= playlistitem1;
    }

    @NonNull
    @Override
    public PlaylistRecyclarView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_model_item, parent, false);
        return new PlaylistRecyclarView.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistRecyclarView.MyViewHolder holder, final int position) {
        holder.fileDate.setText(arrayList.get(position).getPlaylistname());
        holder.fileDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistitem.getplaylistitem(arrayList.get(position).getPlaylistname());
            }
        });
        holder.optiomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });

    }

    @Override
    public long getItemId(final int position) {


        return super.getItemId(position);
    }


    @Override
    public void onViewRecycled(@NonNull PlaylistRecyclarView.MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView fileDate;
        public ImageView image;
        public ImageView optiomenu;
        public MyViewHolder(View view) {
            super(view);
            fileDate =  view.findViewById(R.id.playlistname);
            image= view.findViewById(R.id.image);
            optiomenu= view.findViewById(R.id.optiomenu);
        }

    }
}