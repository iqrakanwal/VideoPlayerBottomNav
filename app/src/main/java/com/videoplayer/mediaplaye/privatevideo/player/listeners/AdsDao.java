package com.videoplayer.mediaplaye.privatevideo.player.listeners;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.videoplayer.mediaplaye.privatevideo.player.models.AdsModel;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Dao
public interface AdsDao {


    @Insert
    long insert(AdsModel adsModel);


    @Update
    void update(AdsModel... adsModels);

    @Delete
    void delete(AdsModel... adsModels);

    @Query("DELETE FROM songs")
    void deleteAll();


    //    @Query("DELETE FROM songs")
//    void insert_Ads ( list);
//
    @Query("SELECT * FROM AdsModel")
    List<AdsModel> getads();


}