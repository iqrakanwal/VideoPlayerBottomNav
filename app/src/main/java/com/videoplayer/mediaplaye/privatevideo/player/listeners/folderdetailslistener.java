package com.videoplayer.mediaplaye.privatevideo.player.listeners;

import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;

import java.util.ArrayList;

public interface folderdetailslistener {

     void getfolder(ArrayList<FileItem> arrayList);
     void privatefolder(ArrayList<FileItem>arrayList);
     void selectall();
     void shareall(ArrayList<FileItem> arrayList);
     void deleteall(ArrayList<FileItem> arrayList);

}
