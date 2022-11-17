package com.videoplayer.mediaplaye.privatevideo.player.activities;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.FavoriteFragment;
import com.videoplayer.mediaplaye.privatevideo.player.models.Favorite;

import java.util.ArrayList;
public class FavoriteActivity extends BaseActivity {
    PlaylistDatabase playlistDatabase;
    Toolbar toolbar;
    FavoriteFragment favoriteFragment;
    public static ArrayList<String> favoritesongsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        favoritesongsList.clear();
        playlistDatabase = PlaylistDatabase.getInstance(FavoriteActivity.this);
        toolbar = findViewById(R.id.toolbar);
        favoriteFragment = new FavoriteFragment();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, favoriteFragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuforfavorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {


            return true;
        } else if (itemId == R.id.mark) {
            if (favoritesongsList.size() <= 0) {
                Toast.makeText(this, "SelectSong", Toast.LENGTH_SHORT).show();
            } else {

                for (int i = 0; i < favoritesongsList.size(); i++) {
                    Favorite f = new Favorite();
                    f.setSongPath(favoritesongsList.get(i).toString());
                    playlistDatabase.getFavorite().insert(f);
                    Toast.makeText(this, "SongInserted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FavoriteActivity.this, MainClass.class));
                    finish();
                }
            }


            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, favoriteFragment).commit();

    }
}