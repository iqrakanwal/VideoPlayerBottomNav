package com.videoplayer.mediaplaye.privatevideo.player.listeners;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.videoplayer.mediaplaye.privatevideo.player.models.Favorite;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;

import java.util.List;

@Dao
public interface favoriteDao {
    @Query("SELECT * FROM Favorite")
    List<Favorite> getAll();


    @Insert
    void insert(Favorite favorite);


@Delete
void delete(Favorite favorite);

    @Query("DELETE FROM favorite WHERE favoriteId = :favoriteid")
    void deleteBycustomId(long favoriteid);


}

