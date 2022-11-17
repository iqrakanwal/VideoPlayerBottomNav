package com.videoplayer.mediaplaye.privatevideo.player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MainClass;
import com.videoplayer.mediaplaye.privatevideo.player.activities.StartingScreen;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;


public class SplashScreen extends BaseActivity {
    int count_iteration = 0;
    Handler handler, handler_1;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);
        start_handler();

    }

    private void switch_acc() {
        PreferencesUtility  preferencesUtility = new PreferencesUtility(SplashScreen.this);


        if(preferencesUtility.getFirsttime()!=null){
            Intent intent = new Intent(SplashScreen.this, MainClass.class);
            startActivity(intent);
            finish();

        }
        else if(preferencesUtility.getFirsttime()==null){

            Intent intent = new Intent(SplashScreen.this, StartingScreen.class);
            startActivity(intent);
            finish();
        }
    }

    public void start_handler() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                count_iteration++;
                if (DataProvider.getInstance().read_data_remainig && !DataProvider.getInstance().load_request_send) {
                    DataProvider.getInstance().on_complete();
                }
                if (!DataProvider.getInstance().buy) {
                    if (DataProvider.getInstance().get_interstitial().isLoaded()) {

                        mInterstitialAd = DataProvider.getInstance().get_interstitial();
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded() ) {
                            ((RelativeLayout) findViewById(R.id.loading_adlayout)).setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    show_ad();
                                }
                            }, 800);

                        } else {

                            switch_acc();
                        }

                    } else if (DataProvider.getInstance().failed) {
                        switch_acc();
                        DataProvider.getInstance().reload_admob();
                    } else if (count_iteration > 2)
                        switch_acc();

                    else {
                        handler.postDelayed(this, 3000);
                    }
                } else
                    switch_acc();


                //Do something after 100ms
            }
        }, 3000);

    }





    public void show_ad()
    {

        ((RelativeLayout)  findViewById(R.id.loading_adlayout)).setVisibility(View.GONE);
        if ((mInterstitialAd.isLoaded() && DataProvider.show_ad) )
        {

            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener()
                                          {
                                              @Override
                                              public void onAdClosed()
                                              {
                                                  super.onAdClosed();
                                                  switch_acc();
                                                  DataProvider.getInstance().reload_admob();


                                              }
                                          }
            );
        }

        else {
            switch_acc();
        }

        DataProvider.toggle_ad_check();

    }
}
