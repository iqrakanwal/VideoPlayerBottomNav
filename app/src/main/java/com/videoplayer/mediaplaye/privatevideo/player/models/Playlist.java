package com.videoplayer.mediaplaye.privatevideo.player.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Playlist {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="playlistname")
    String playlistname;

    public Playlist(String playlistname) {
        this.playlistname = playlistname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public void setPlaylistname(String playlistname) {
        this.playlistname = playlistname;
    }
}
