package com.videoplayer.mediaplaye.privatevideo.player.models;

public class MusicSelection {
    String aName;
Boolean isSelected;
String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public MusicSelection(String aName, Boolean isSelected, String artist) {
        this.aName = aName;
        this.isSelected = isSelected;
        this.artist = artist;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
