package com.videoplayer.mediaplaye.privatevideo.player.models;

import java.util.ArrayList;

public class FolderItem {
    private String folderName;
    private String folderPath;
    private boolean isSelected =false;

    private ArrayList<FileItem> videoItems = new ArrayList<>();

    public FolderItem(String folderName, String folderPath, boolean isSelected){
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.isSelected= isSelected;

    }
    public FolderItem(String folderName, String folderPath, ArrayList<FileItem> videoItems){
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.videoItems = videoItems;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public ArrayList<FileItem> getVideoItems() {
        return videoItems;
    }

    public void setVideoItems(ArrayList<FileItem> videoItems) {
        this.videoItems = videoItems;
    }


}
