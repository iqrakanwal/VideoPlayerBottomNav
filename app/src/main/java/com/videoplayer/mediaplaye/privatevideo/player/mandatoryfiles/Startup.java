package com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MainClass;


public class Startup extends AppCompatActivity
{

    boolean mActive;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        DataProvider.getInstance().log_event("startup_activity","open");



    }



    @Override
    protected void onResume() {
        super.onResume();
        mActive=true;
       // startActivity(new Intent(Launcher.this, com.global.gps.free.livemaps.navigation.streetview.Activity.MainActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActive=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActive=false;
    }


    public void go_to_pp(View view)
    {
        startActivity(new Intent(this,PrivacyPolicy.class));
    }

    public void next(View view)
    {
        view.setEnabled(false);
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean(
                "get_started", true);
        sharedPreferencesEditor.apply();
        startActivity(new Intent(this, MainClass.class));
        finish();
    }
}
