package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

public class StartingScreen extends BaseActivity {
PreferencesUtility preferencesUtility;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
        button= findViewById(R.id.button);
        preferencesUtility = new PreferencesUtility(StartingScreen.this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferencesUtility.getFirsttime()==null){
                    preferencesUtility.setFirsttime("set");
                }
startActivity(new Intent(StartingScreen.this, MainClass.class));
                finish();
            }
        });


    }
}