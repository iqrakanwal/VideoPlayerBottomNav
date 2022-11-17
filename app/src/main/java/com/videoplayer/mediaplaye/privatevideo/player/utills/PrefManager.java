package com.videoplayer.mediaplaye.privatevideo.player.utills;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void savepassword( String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setpin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Password", password);
        editor.commit();
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setpin", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Password",null);

    }


}
