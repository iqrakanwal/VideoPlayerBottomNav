package com.videoplayer.mediaplaye.privatevideo.player.models;

public class ThemeChoiceItem {
    int color;
    int id;
    String text;

    public ThemeChoiceItem(int color, int id, String text){
        this.color = color;
        this.id = id;
        this.text= text;
    }
    public int getColor(){
        return color;
    }
    public int getId(){
        return id;
    }
    public void setColor(int value){
        this.color = value;
    }
    public void setId(int value){
        this.id = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
