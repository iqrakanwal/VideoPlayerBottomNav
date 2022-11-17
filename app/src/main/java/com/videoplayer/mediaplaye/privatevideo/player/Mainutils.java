package com.videoplayer.mediaplaye.privatevideo.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;

import androidx.preference.PreferenceManager;

import com.google.android.exoplayer2.PlaybackParameters;
import com.google.gson.Gson;
import com.videoplayer.mediaplaye.privatevideo.player.activities.EqualizerSettings;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MainClass;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;

import static com.videoplayer.mediaplaye.privatevideo.player.activities.Equalizerclass.PREF_KEY;

public class Mainutils {
    private static final Mainutils ourInstance = new Mainutils();
    public Equalizer.Settings getSettings1() {
        return settings1;
    }
    public void setSettings1(Equalizer.Settings settings1) {
        this.settings1 = settings1;
    }

    private Equalizer.Settings settings1;
    public static Mainutils getInstance() {
        return ourInstance;
    }

    private Mainutils() {
    }


    public FileItem playingVideo;
    public int audiosession;
    public VideoBackendService videoBackendService;
    public long seekPosition = 0;
    public boolean isPlaying = true;
    public boolean isNeedRefreshFolder = false;
    public boolean isOpenFromIntent = false;
    public ArrayList<FileItem> videoItemsPlaylist = new ArrayList<>();
    public ArrayList<FileItem> allfileItems = new ArrayList<>();
    public FolderItem folderItem;
    public boolean isSeekBarProcessing = false;

    public String getPath(){
        if(playingVideo != null)
            return playingVideo.getPath();
        return "77777777777";
    }
    public void loadEquilizerSettings(Context context) {
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        EqualizerSettings settings = gson.fromJson(preferences.getString(PREF_KEY, "{}"), EqualizerSettings.class);
        settings1 = new Equalizer.Settings();
        settings1.bandLevels = settings.seekbarpos;
        settings1.curPreset = (short) settings.presetPos;
        settings1.numBands = (short) settings.seekbarpos.length;

    }

    public SimpleExoPlayer getPlayer(){
        if(videoBackendService == null)
            return  null;
        return videoBackendService.getVideoPlayer();
    }
    public boolean isPlayingAsPopup(){
        if(videoBackendService == null) return false;
        return videoBackendService.isPlayingAsPopup();
    }
    public void playNext(){
        if(videoBackendService == null) return;
        videoBackendService.handleAction(VideoBackendService.NEXT_ACTION);
    }
    public void playPrevious(){
        if(videoBackendService == null) return;
        videoBackendService.handleAction(VideoBackendService.PREVIOUS_ACTION);
    }
    public void closeNotification(){
        if(videoBackendService == null) return;
        videoBackendService.closeBackgroundMode();
    }
    public void openNotification(){
        if(videoBackendService == null) return;
        videoBackendService.openBackgroundMode();
    }
    public void pausePlay(){
        if(videoBackendService == null) return;
        videoBackendService.handleAction(VideoBackendService.TOGGLEPAUSE_ACTION);
    }


    public void chnage(PlaybackParameters param){
        if(videoBackendService == null) return;
        videoBackendService.change(param);
    }


    public void sessionid(){
        if(videoBackendService == null) return;
        videoBackendService.session();

    }
    public void createShuffle(){
        if(videoBackendService == null) return;
        videoBackendService.createShuffleArray();
    }
    public boolean isbackgroundplay(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean name = sharedPreferences.getBoolean("backgroundplaycheck", false);
       if(name){
          return true;
       }else
           return false;
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void speedchange(){
//        if(videoService == null) return;
//        videoService.getPlayerManager().setplaybackspeed();
//    }
    public int getCurrentPosition(){
        if(videoBackendService == null) return  - 1;
        return videoBackendService.getCurrentPosition();
    }



}
