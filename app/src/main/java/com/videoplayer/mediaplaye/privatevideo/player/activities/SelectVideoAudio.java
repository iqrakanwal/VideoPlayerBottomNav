package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ViewPagerAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistListViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.SongFragment;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.VideoFragments;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

public class SelectVideoAudio extends BaseActivity {
    TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    public static ArrayList<String> songsList = new ArrayList<>();
    private PlaylistListViewModel viewModelNotes;
    private SongsViewModel viewModelSong;
    public static ArrayList<String> selectedplaylist = new ArrayList<>();
    int playlistid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video_audio);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        songsList.clear();
        viewModelNotes = ViewModelProviders.of(SelectVideoAudio.this).get(PlaylistListViewModel.class);
        viewModelSong = ViewModelProviders.of(SelectVideoAudio.this).get(SongsViewModel.class);
        String intent = getIntent().getStringExtra("playlistname");
        Toast.makeText(this, ""+intent, Toast.LENGTH_SHORT).show();
         playlistid = viewModelNotes.getidbytext(intent);
        Toast.makeText(this, ""+playlistid, Toast.LENGTH_SHORT).show();
        tabLayout = findViewById(R.id.tablayoutselection);
        viewPager = findViewById(R.id.viewpagerselection);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SongFragment(), "Music");
        adapter.addFrag(new VideoFragments(), "Videos");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selection_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_insert) {

            insertsong(playlistid);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void insertsong(int playlistid) {

        Toast.makeText(this, ""+ songsList.toString(), Toast.LENGTH_SHORT).show();

            for (int i = 0; i < songsList.size(); i++) {
                songsList.get(i);
                Toast.makeText(this, ""+ songsList.get(i).toString(), Toast.LENGTH_SHORT).show();
                File file = new File(songsList.get(i));
                Songs song = new Songs(songsList.get(i), file.getAbsolutePath(), playlistid);
                viewModelSong.insertNote(song);
                Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SelectVideoAudio.this, MainClass.class));

            }


        }





}
