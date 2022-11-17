package com.videoplayer.mediaplaye.privatevideo.player.models;


public class FileItem {
    private String fileTitle;
    private String path;
    private String duration;
    private String folderName;
    private String fileSize;
    private long fileSizeAsFloat;
    private boolean isLongClick = false;
    private boolean isSelected =false;
    private String resolution;
    private String dateadded;

    public FileItem(String videoTitle, String path, String duration, String folderName, String resolution,String dateadded, Boolean isSelected) {

        this.path = path;
        this.duration = duration;
        this.fileTitle = videoTitle;
        this.folderName = folderName;
        this.resolution = resolution;
        this.dateadded=dateadded;
        this.isSelected=isSelected;

    }

    public FileItem() {
        this.fileTitle = "";
        this.path = "";
        this.duration = "";
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }
    public String getResolution() {
        return resolution;
    }

    public String getDateAded() {
        return dateadded;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String videoTitle) {
        this.fileTitle = videoTitle;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setPath(String value) {
        this.path = value;
    }


    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public float getFileSizeAsFloat() {
        return fileSizeAsFloat;
    }

    public void setFileSizeAsFloat(long fileSizeAsFloat) {
        this.fileSizeAsFloat = fileSizeAsFloat;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

}


