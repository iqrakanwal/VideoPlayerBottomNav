package com.videoplayer.mediaplaye.privatevideo.player.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.videoplayer.mediaplaye.privatevideo.player.listeners.AdsDao;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.SongDao;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.favoriteDao;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.playlistDao;
import com.videoplayer.mediaplaye.privatevideo.player.models.AdsModel;
import com.videoplayer.mediaplaye.privatevideo.player.models.Favorite;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;


@Database(entities = {Playlist.class, Favorite.class, Songs.class, AdsModel.class}, version = 1, exportSchema = false)

public abstract class PlaylistDatabase extends RoomDatabase {
    public static Context context;
      private static PlaylistDatabase playlistDatabase;
    public abstract playlistDao getplaylist();
    public abstract favoriteDao getFavorite();
    public abstract SongDao getSongs();
    public abstract AdsDao getads();

    public static PlaylistDatabase getInstance(Context context) {
        if (null == playlistDatabase) {
            playlistDatabase = buildDatabaseInstance(context);
        }
        return playlistDatabase;
    }
    private static PlaylistDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                PlaylistDatabase.class,
                "Videoplayer")
                .allowMainThreadQueries().build();
    }



}
