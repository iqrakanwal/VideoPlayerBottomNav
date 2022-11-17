package com.videoplayer.mediaplaye.privatevideo.player.listeners;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;

import java.util.List;

@Dao
public interface playlistDao {

    @Query("SELECT * FROM playlist ORDER BY id DESC")
    LiveData<List<Playlist>> getAllNotes();

    @Query("SELECT * FROM playlist WHERE id=:id")
    Playlist getNoteById(int id);

    @Query("SELECT * FROM playlist WHERE playlistname=:id")
    int getIdByText(String id);

    @Query("SELECT * FROM playlist WHERE id=:id")
    LiveData<Playlist> getNote(int id);

    @Insert
    long insert(Playlist note);

    @Update
    void update(Playlist note);

    @Query("DELETE FROM playlist")
    void deleteAll();

    @Query("SELECT * FROM playlist")
    List<Playlist> getAll();

    @Delete
    void delete(Playlist playlist);

    @Query("DELETE FROM playlist WHERE id = :playlistid")
    void deleteBycustomId(long playlistid);


}

