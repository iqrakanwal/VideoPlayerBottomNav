package com.videoplayer.mediaplaye.privatevideo.player.models;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class AdsModel {
    @PrimaryKey(autoGenerate = true)
    public  int id;
    public String ad_id;
    public String name;

    public AdsModel(String ad_id, String name) {
        this.ad_id = ad_id;
        this.name = name;
    }


    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}