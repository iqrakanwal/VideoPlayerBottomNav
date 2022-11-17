package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSongRecyclarView extends RecyclerView.Adapter<PlaylistSongRecyclarView.MyViewHolder> {
    List<Songs> arrayList= new ArrayList<Songs>();

    Context c;
    public PlaylistSongRecyclarView(Context c1, List<Songs> arrayList) {
        this.c= c1;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PlaylistSongRecyclarView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_model_item, parent, false);

        return new PlaylistSongRecyclarView.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistSongRecyclarView.MyViewHolder holder, final int position) {
        holder.fileDate.setText(arrayList.get(position).getName());
        Toast.makeText(c, ""+arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();

        holder.fileDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file= new File(arrayList.get(position).getUrl());
                FileItem fileItem=new FileItem();
                fileItem.setFileTitle(file.getName());
                fileItem.setPath(file.getAbsolutePath());
                Mainutils.getInstance().playingVideo=fileItem;
                Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition,false);
                Intent intent = new Intent(c, PlayVideoActivity.class);
                c.startActivity(intent);

            }
        });
      /*  holder.fileDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mainutils.getInstance().playingVideo=fileItem;
                Mainutils.getInstance().videoService.playVideo(Mainutils.getInstance().seekPosition,false);
                Intent intent = new Intent(c, PlayVideoActivity.class);
                c.startActivity(intent);

            }
        });*/
    }

    @Override
    public long getItemId(final int position) {


        return super.getItemId(position);
    }


    @Override
    public void onViewRecycled(@NonNull PlaylistSongRecyclarView.MyViewHolder holder) {
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

        public MyViewHolder(View view) {
            super(view);
            fileDate =  view.findViewById(R.id.playlistname);
        }

    }
}