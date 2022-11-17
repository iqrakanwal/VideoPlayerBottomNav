package com.videoplayer.mediaplaye.privatevideo.player;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.videoplayer.mediaplaye.privatevideo.player.fragments.AllFolderList;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.MyFragment;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.permissions.PermissionCallback;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.util.ArrayList;


public abstract class BaseActivity extends AppCompatActivity {

   public Mainutils mMainutils = Mainutils.getInstance();
    protected static final int PERMISSION_REQUEST_CODE = 888888888;
    boolean isNeedResumePlay = false;
    RelativeLayout myLayout;
    MyFragment currentFragment  = new AllFolderList();
    //protected CastContext castContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int theme = PreferencesUtility.getInstance(this).getThemeSettings();
        if(theme < 0 || theme > 9) theme = 0;
        if(theme== 0) {
            setTheme(R.style.AppThemeLightBasic0);

        }else if(theme== 1) {
            setTheme(R.style.AppThemeLightBasic1);

        }
        if(theme== 2) {
            setTheme(R.style.AppThemeLightBasic8);

        }
        if(theme== 3) {
            setTheme(R.style.AppThemeLightBasic9);

        }
        if(theme== 4) {
            setTheme(R.style.AppThemeLightBasic4);

        }
        if(theme== 5) {
            setTheme(R.style.AppThemeLightBasic5);

        }

        super.onCreate(savedInstanceState);
        Mainutils.getInstance().loadEquilizerSettings(BaseActivity.this);

        inItService();
        //botomlayout();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Mainutils.getInstance().videoBackendService != null)
            unbindService(videoServiceConnection);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected ServiceConnection videoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            VideoBackendService.VideoBinder binder = (VideoBackendService.VideoBinder) service;
            Mainutils.getInstance().videoBackendService = binder.getService();

            if(isNeedResumePlay) startPopupMode();
            if(mMainutils.isOpenFromIntent){
                mMainutils.isOpenFromIntent = false;
                mMainutils.videoBackendService.playVideo(0,false);
                showFloatingView(BaseActivity.this,true);
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnection = false;
        }
    };
    boolean isConnection = false;
    protected  static Intent _playIntent;

    public void inItService() {
        _playIntent = new Intent(this, VideoBackendService.class);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(_playIntent);
            } else {
                startService(_playIntent);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
            return ;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(_playIntent);
        } else {
            startService(_playIntent);
        }
        startService(_playIntent);
        bindService(_playIntent, videoServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void startPopupMode() {
        if (_playIntent != null) {
            Mainutils.getInstance().videoBackendService.playVideo(mMainutils.seekPosition,true);
        }
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(currentFragment != null){
            currentFragment.reloadFragment(newConfig.orientation);
        }
        // Checking the orientation of the screen
    }
    public void reloadData(){

    }
    int requestCode = 1;
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (this.requestCode == resultCode) {
            isNeedResumePlay = true;
            startPopupMode();

        } else {

        }
    }
    @SuppressLint("NewApi")
    public  void showFloatingView(Context context, boolean isShowOverlayPermission) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startPopupMode();
            return;
        }

        if (Settings.canDrawOverlays(context)) {
            startPopupMode();
            return;
        }

        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, requestCode);
        }
    }

  /*  /// permission
    protected void checkPermissionAndThenLoad() {


        if (AppsUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && AppsUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadEverything();
        } else {
            AppsUtils.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);

        }
    }*/


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AppsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    protected void loadEverything(){

    }
    public void updateMultiSelectedState(){

    }
    public void updateListAfterDelete(ArrayList<FileItem> videoItems){

    }
//    protected void setupCast(){
//        try {
//            castContext = CastContext.getSharedInstance(this);
//        } catch (RuntimeException e) {
//            Throwable cause = e.getCause();
//            while (cause != null) {
//                if (cause instanceof DynamiteModule.LoadingException) {
//                    return;
//                }
//                cause = cause.getCause();
//            }
//            // Unknown error. We propagate it.
//            throw e;
//        }
//    }



//    public void botomlayout(){
//
//
//
//        final RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
//        final TextView tv1 = new TextView(this);
//        tv1.setText("tv1 is here");
//        // Setting an ID is mandatory.
//        tv1.setId(View.generateViewId());
//        relativeLayout.addView(tv1);
//
//
//        final TextView tv2 = new TextView(this);
//        tv2.setText("tv2 is here");
//
//        // We are defining layout params for tv2 which will be added to its  parent relativelayout.
//        // The type of the LayoutParams depends on the parent type.
//        RelativeLayout.LayoutParams tv2LayoutParams = new  RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        tv2LayoutParams.setMargins(25,25,25,25);
//        //Also, we want tv2 to appear below tv1, so we are adding rule to tv2LayoutParams.
//       // tv2LayoutParams.addRule(RelativeLayout.BELOW, tv1.getId());
//
//        //Now, adding the child view tv2 to relativelayout, and setting tv2LayoutParams to be set on view tv2.
//        relativeLayout.addView(tv2);
//        tv2.setLayoutParams(tv2LayoutParams);
//
//
//
//       // this.setContentView(relativeLayout);
//
//    }
}
