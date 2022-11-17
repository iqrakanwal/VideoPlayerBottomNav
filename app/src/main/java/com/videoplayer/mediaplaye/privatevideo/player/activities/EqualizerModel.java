package com.videoplayer.mediaplaye.privatevideo.player.activities;

import java.io.Serializable;


public class EqualizerModel implements Serializable {
    private boolean isEqualizerEnabled;
    private short[] seekbarpos = new short[5];
    private int presetPos;
    private short reverbPreset;
    private short bassStrength;
    private int numOfBand;

    public int getNumOfBand() {
        return numOfBand;
    }

    public void setNumOfBand(int numOfBand) {
        this.numOfBand = numOfBand;
    }

    public EqualizerModel() {
        isEqualizerEnabled = true;
        reverbPreset = -1;
        bassStrength = -1;
    }

    public boolean isEqualizerEnabled() {
        return isEqualizerEnabled;
    }

    public void setEqualizerEnabled(boolean equalizerEnabled) {
        isEqualizerEnabled = equalizerEnabled;
    }

    public short[] getSeekbarpos() {
        return seekbarpos;
    }

    public void setSeekbarpos(short[] seekbarpos) {
        this.seekbarpos = seekbarpos;
    }

    public int getPresetPos() {
        return presetPos;
    }

    public void setPresetPos(int presetPos) {
        this.presetPos = presetPos;
    }

    public short getReverbPreset() {
        return reverbPreset;
    }

    public void setReverbPreset(short reverbPreset) {
        this.reverbPreset = reverbPreset;
    }

    public short getBassStrength() {
        return bassStrength;
    }

    public void setBassStrength(short bassStrength) {
        this.bassStrength = bassStrength;
    }
}
