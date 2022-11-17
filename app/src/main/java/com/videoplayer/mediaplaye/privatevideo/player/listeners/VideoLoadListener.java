package com.videoplayer.mediaplaye.privatevideo.player.listeners;

import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;

import java.util.ArrayList;



public interface VideoLoadListener {

    void onVideoLoaded(ArrayList<FileItem> videoItems);

}
