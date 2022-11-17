package com.videoplayer.mediaplaye.privatevideo.player.activities;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.PlaylistSongRecyclarView;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistListViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;
import java.util.List;

public class Playlistfiles extends BaseActivity {
    RecyclerView recyclerView;
    LiveData<List<Songs>> songsList;
    SongsViewModel songsViewModel;
    PlaylistListViewModel playlistListViewModel;
    PlaylistSongRecyclarView playlistSongRecyclarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlistfiles);
        songsViewModel = ViewModelProviders.of(Playlistfiles.this).get(SongsViewModel.class);
        playlistListViewModel = ViewModelProviders.of(Playlistfiles.this).get(PlaylistListViewModel.class);
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("playlistnameforplaylist");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("playlistnameforplaylist");
        }
        Toast.makeText(this, ""+newString, Toast.LENGTH_SHORT).show();

        int id = playlistListViewModel.getidbytext(newString);
        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        songsViewModel.getSongsFromPlaylist(id).observe(this, new Observer<List<Songs>>() {
            @Override
            public void onChanged(List<Songs> songs) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(Playlistfiles.this);
                recyclerView.setLayoutManager(layoutManager);
                playlistSongRecyclarView = new PlaylistSongRecyclarView(Playlistfiles.this, songs);
                recyclerView.setAdapter(playlistSongRecyclarView);
            }
        });
        recyclerView = findViewById(R.id.playlistrecyclar);

    }
}