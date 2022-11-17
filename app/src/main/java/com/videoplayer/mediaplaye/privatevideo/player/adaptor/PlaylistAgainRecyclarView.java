package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.$Gson$Preconditions;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.SelectVideoAudio;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistListViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.Playlistitem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;

import java.util.ArrayList;

import static com.google.gson.reflect.TypeToken.get;

public class PlaylistAgainRecyclarView extends RecyclerView.Adapter<PlaylistAgainRecyclarView.MyViewHolder> {
    ArrayList<Playlist> arrayList = new ArrayList<Playlist>();
    FileItem videoItem;
    Activity c;

    public PlaylistAgainRecyclarView(Activity c1, ArrayList<Playlist> arrayList, FileItem fileItem) {

        this.c = c1;
        this.arrayList = arrayList;
        this.videoItem = fileItem;
    }

    @NonNull
    @Override
    public PlaylistAgainRecyclarView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_model_item, parent, false);

        return new PlaylistAgainRecyclarView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistAgainRecyclarView.MyViewHolder holder, final int position) {
        holder.fileDate.setText(arrayList.get(position).getPlaylistname());


        holder.fileDate.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                SongsViewModel viewModelSong;
                PlaylistListViewModel viewModelNotes;
                viewModelSong = ViewModelProviders.of((FragmentActivity) c).get(SongsViewModel.class);
                viewModelNotes = ViewModelProviders.of((FragmentActivity) c).get(PlaylistListViewModel.class);
                int playlistid = viewModelNotes.getidbytext(arrayList.get(position).getPlaylistname());
                Songs song = new Songs(videoItem.getFileTitle(), videoItem.getPath(), playlistid);
                viewModelSong.insertNote(song);


                Toast.makeText(c, "Inserted", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(c, SelectVideoAudio.class);
//                intent.putExtra("playlistname", arrayList.get(position).getPlaylistname().toString());
//                c.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(final int position) {


        return super.getItemId(position);
    }


    @Override
    public void onViewRecycled(@NonNull PlaylistAgainRecyclarView.MyViewHolder holder) {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fileDate;

        public MyViewHolder(View view) {
            super(view);
            fileDate = view.findViewById(R.id.playlistname);
        }

    }
}