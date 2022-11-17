package com.videoplayer.mediaplaye.privatevideo.player.models;

import java.io.File;

public class FileModel {
    private String fileDisplayName;
    private String filePath;
    private String type;
    private int thumbnail;
    private File file;
    private String size;
    private boolean isSelected;
    private String date;
    private String fileApsolutePath;

    private String fileCreationdate;


    public FileModel() {
    }

    public String getFileCreationdate() {
        return fileCreationdate;
    }

    public void setFileCreationdate(String fileCreationdate) {
        this.fileCreationdate = fileCreationdate;
    }

    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public void setFileDisplayName(String fileDisplayName) {
        this.fileDisplayName = fileDisplayName;
    }

    public String getFileApsolutePath() {
        return fileApsolutePath;
    }

    public void setFileApsolutePath(String fileApsolutePath) {
        this.fileApsolutePath = fileApsolutePath;
    }

    public String getType() {
        return type;
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
