package com.videoplayer.mediaplaye.privatevideo.player.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorite {
    @PrimaryKey(autoGenerate = true)
   public int favoriteId;

    @ColumnInfo(name="songPath")
    public String songPath;

    public Favorite() {
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }
}
