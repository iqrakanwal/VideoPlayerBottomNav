package com.videoplayer.mediaplaye.privatevideo.player.models;

public class Folder {

    int no_of_file;
    String folderName;
    Long folderSize;

    public Folder() {
    }

    public int getNo_of_file() {
        return no_of_file;
    }

    public void setNo_of_file(int no_of_file) {
        this.no_of_file = no_of_file;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getFolderSize() {
        return folderSize;
    }

    public void setFolderSize(Long folderSize) {
        this.folderSize = folderSize;
    }
}
