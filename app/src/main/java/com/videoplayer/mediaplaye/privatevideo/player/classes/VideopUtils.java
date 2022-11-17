package com.videoplayer.mediaplaye.privatevideo.player.classes;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

public class VideopUtils extends Application {



   public static Context context;
    private static VideopUtils ourInstance = new VideopUtils();

    public static VideopUtils getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        context=  getApplicationContext();
        AppsUtils.init(this);
        DataProvider.getInstance();
        Toast.makeText(context, ""+DataProvider.getInstance().adsModels, Toast.LENGTH_SHORT).show();

    }

    public static Context getcontext(){

        return context;
    }


}