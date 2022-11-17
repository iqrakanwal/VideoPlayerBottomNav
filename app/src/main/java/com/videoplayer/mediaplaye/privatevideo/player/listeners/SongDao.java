package com.videoplayer.mediaplaye.privatevideo.player.listeners;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;

import java.util.List;


@Dao
public interface SongDao {



    @Insert
    long insert(Songs songs);


    @Update
    void update(Songs... songs);

    @Delete
    void delete(Songs... songs);

    @Query("SELECT * FROM songs ORDER BY id DESC")
    LiveData<List<Songs>> getAllRepos();

    @Query("DELETE FROM songs where id = :id  ")
    void deleteRepoByid(int id);

    @Query("DELETE FROM songs WHERE id = :playlistid")
    void deleteBycustomId(long playlistid);

    @Query("SELECT * FROM songs WHERE userId=:userId")
    LiveData<List<Songs>> findRepositoriesForUser(final int userId);

    @Query("SELECT * FROM songs WHERE id=:id")
    Songs getNoteById(int id);


    @Query("DELETE FROM songs")
    void deleteAll();
}