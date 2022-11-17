package com.videoplayer.mediaplaye.privatevideo.player;

import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;



public interface AudioLoadListener {

    void onAudioLoaded(ArrayList<FileItem> videoItems);

    void onFailed(Exception e);
}
