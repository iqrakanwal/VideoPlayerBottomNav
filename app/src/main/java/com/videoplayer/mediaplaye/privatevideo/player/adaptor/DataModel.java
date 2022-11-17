package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

public class DataModel {

    public String text;
    public int drawable;
    public String color;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;

    public DataModel(String text, int drawable, String color, int id) {
        this.text = text;
        this.drawable = drawable;
        this.color = color;
        this.id = id;
    }
}