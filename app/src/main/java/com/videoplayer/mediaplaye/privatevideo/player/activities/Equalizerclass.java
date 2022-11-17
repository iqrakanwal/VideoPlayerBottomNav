package com.videoplayer.mediaplaye.privatevideo.player.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.google.gson.Gson;

public class Equalizerclass extends BaseActivity {
  // private MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizerclass);
        loadEqualizerSettings(this);
//        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
//                .setAccentColor(Color.parseColor("#ff0000")
//)
             //   .setAudioSessionId(10)
              //  .build();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.eqFrame, equalizerFragment)
//                .commit();

//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//       int audioSessionId = audioManager.generateAudioSessionId();
      //  mediaPlayer.setLooping(true);
//        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
//                .setAccentColor(Color.parseColor("#555555"))
//                .setAudioSessionId(audioSessionId)
//                .build();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.eqFrame, equalizerFragment)
//                .commit();
//

    }
//    private void showInDialog() {
//      //  int sessionId = mediaPlayer.getAudioSessionId();
//        if (sessionId > 0) {
//            DialogEqualizerFragment fragment = DialogEqualizerFragment.newBuilder()
//                    .setAudioSessionId(sessionId)
//                    .title(R.string.app_name)
//                    .themeColor(ContextCompat.getColor(this, R.color.primaryColor))
//                    .textColor(ContextCompat.getColor(this, R.color.textColor))
//                    .accentAlpha(ContextCompat.getColor(this, R.color.playingCardColor))
//                    .darkColor(ContextCompat.getColor(this, R.color.primaryDarkColor))
//                    .setAccentColor(ContextCompat.getColor(this, R.color.secondaryColor))
//                    .build();
//            fragment.show(getSupportFragmentManager(), "eq");
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();

    }



    @Override
    protected void onResume() {
        super.onResume();
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   //mediaPlayer.start();
                }
            }, 2000);
        } catch (Exception ex) {
            //ignore
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.itemEqDialog) {
//            showInDialog();
//            return true;
//        }
       return super.onOptionsItemSelected(item);
    }

    private void saveEqualizerSettings(){
        if (EqSettings.equalizerModel != null){
            EqualizerSettings settings = new EqualizerSettings();
            settings.bassStrength = EqSettings.equalizerModel.getBassStrength();
            settings.presetPos = EqSettings.equalizerModel.getPresetPos();
            settings.reverbPreset = EqSettings.equalizerModel.getReverbPreset();
            settings.seekbarpos = EqSettings.equalizerModel.getSeekbarpos();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            preferences.edit().putString(PREF_KEY, gson.toJson(settings)).apply();
        }
    }

    public void loadEqualizerSettings(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        EqualizerSettings settings = gson.fromJson(preferences.getString(PREF_KEY, "{}"), EqualizerSettings.class);
        EqualizerModel model = new EqualizerModel();
        model.setBassStrength(settings.bassStrength);
        model.setPresetPos(settings.presetPos);
        model.setReverbPreset(settings.reverbPreset);
        model.setSeekbarpos(settings.seekbarpos);
        EqSettings.isEqualizerEnabled = true;
        EqSettings.isEqualizerReloaded = true;
        EqSettings.bassStrength = settings.bassStrength;
        EqSettings.presetPos = settings.presetPos;
        EqSettings.reverbPreset = settings.reverbPreset;
        EqSettings.seekbarpos = settings.seekbarpos;
        EqSettings.equalizerModel = model;
    }

    public static final String PREF_KEY = "equalizer";
    }
