package com.videoplayer.mediaplaye.privatevideo.player.models;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Playlist.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = CASCADE))
public class Songs {
    @PrimaryKey(autoGenerate = true)
    public  int id;
    public String name;
    public String url;
    public  int userId;

    public Songs(String name, String url, final int userId) {

        this.name = name;
        this.url = url;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}