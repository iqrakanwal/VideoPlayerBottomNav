package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.GetFavoriteAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.models.Favorite;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetFavorite extends BaseActivity {
RecyclerView recyclarview;
PlaylistDatabase playlistDatabase;
List<Favorite> allList;
GetFavoriteAdapter getFavoriteAdapter;
Toolbar toolbar;
ImageView add;
TextView shuffle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_favorite);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favirotes songs");
        setSupportActionBar(toolbar);
        playlistDatabase = PlaylistDatabase.getInstance(GetFavorite.this);
        recyclarview= findViewById(R.id.favorite);
        shuffle=findViewById(R.id.shuffle);
        add= findViewById(R.id.addimage);
        allList = new ArrayList<Favorite>();
        allList = playlistDatabase.getFavorite().getAll();
        ArrayList<Favorite> arrlistofOptions = new ArrayList<Favorite>(allList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GetFavorite.this);
        getFavoriteAdapter= new GetFavoriteAdapter(GetFavorite.this,arrlistofOptions);
        recyclarview.setLayoutManager(layoutManager);
        recyclarview.setAdapter(getFavoriteAdapter);
        add.setOnClickListener(view -> startActivity(new Intent(GetFavorite.this, FavoriteActivity.class)));

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.shuffle(arrlistofOptions); // shuffling the list
                recyclarview.setAdapter(getFavoriteAdapter);

            }
        });
    }

}