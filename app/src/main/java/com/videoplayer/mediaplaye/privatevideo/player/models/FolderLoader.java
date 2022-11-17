package com.videoplayer.mediaplaye.privatevideo.player.models;

import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FolderLoader {
    public static ArrayList<FolderItem>  getFolderList(ArrayList<FileItem> videoItems, boolean value){
        File file;
        ArrayList<FolderItem> folderItems = new ArrayList<>();
        if(videoItems != null && videoItems.size() > 0){
            for(int i = 0; i < videoItems.size();i ++){
                file = new File(videoItems.get(i).getPath());
                String filePath = file.getParent();
                String fileName = "Unknow Folder";
                File _parentFile = file.getParentFile();
                Log.d("iii", _parentFile.getParentFile().toString());
                if (_parentFile.exists()) {
                    fileName = _parentFile.getName();
                }
                if(!isFileExits(folderItems,filePath)){
                    folderItems.add(new FolderItem(fileName,filePath,value));
                }
                for (FolderItem item :folderItems) {
                    if(item.getFolderPath().contains(filePath)){
                        item.getVideoItems().add(videoItems.get(i));
                    }
                }
            }
        }

        return folderItems;
    }


    public static ArrayList<FolderItem> getFolderListVideo(ArrayList<FileItem> videoItems){
        File file;
        ArrayList<FolderItem> folderItems = new ArrayList<>();
        if(videoItems != null && videoItems.size() > 0){
            for(int i = 0; i < videoItems.size();i ++){
                file = new File(videoItems.get(i).getPath());
                String filePath = file.getParent();
                String fileName = "Unknow Folder";
                File _parentFile = file.getParentFile();
                if (_parentFile.exists()) {
                    fileName = _parentFile.getName();
                }
                if(!isFileExitsVideo(folderItems,filePath)){
                    folderItems.add(new FolderItem(fileName,filePath, false));
                }
                for (FolderItem item :folderItems) {
                    if(item.getFolderPath().equals(filePath)){
                        item.getVideoItems().add(videoItems.get(i));
                    }

                }

            }
        }

        return folderItems;
    }
    private static boolean isFileExits(ArrayList<FolderItem> folderItems, String path){
        for (FolderItem item :folderItems) {
            if(item.getFolderPath().equals(path))
                return true;
        }
        return false;
    }
    private static boolean isFileExitsVideo(ArrayList<FolderItem> folderItems, String path){
        for (FolderItem item :folderItems) {
            if(item.getFolderPath().equals(path))
                return true;
        }
        return false;
    }

}
