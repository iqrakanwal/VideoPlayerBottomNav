package com.videoplayer.mediaplaye.privatevideo.player.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Gestures.GestureListener;
import com.videoplayer.mediaplaye.privatevideo.player.Gestures.IGestureListener;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.PlayPauseDrawable;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.VideoBackendService;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.VideoPlayingListAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import com.github.rubensousa.previewseekbar.PreviewView;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import net.cachapa.expandablelayout.ExpandableLayout;
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PlayVideoActivity extends BaseActivity implements PreviewView.OnPreviewChangeListener, GestureDetector.OnDoubleTapListener {
    private FileItem currentVideo = new FileItem();
    private int currentVideoPosition = -999;
    Mainutils mMainutils = Mainutils.getInstance();
    private PlayerView mPlayerView;
    Runnable r;
    int clickCount = 0;
    long startTime;
    //variable for calculating the total time
    long duration;
    //constant for defining the time duration between the click that can be considered as double-tap
    static final int MAX_DURATION = 500;
    private boolean isVideoPlaying = false;
    private static final int CONTROL_HIDE_TIMEOUT = 30000000;
    private long lastTouchTime;
    PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();
    FloatingActionButton btnPausePlay;
    private static long mDeBounce = 0;

    FrameLayout frameLayout_preview;
    RelativeLayout layout_all_control_container, layout_region_volume, layout_region_brightness, layoutspeed;
    RelativeLayout layout_control_top, layout_btn_bottom, layout_skip_next_10, layout_skip_back_10;
    LinearLayout frameLayout;
    private ExpandableLayout expandableLayout, expandableRecyclerView;
    MaterialIconView btnExpandLayout, btnClosePlaylist;
    ImageView btnLockControl;
    public static int flagforbackground = 0, flagforpopup = 0, autorotate = 0, volumn = 0, brightness = 0, flagnightmode = 0, flagscreenshot = 0, flagtimer = 0, flagmute = 0, flagspeed = 0;
    MaterialIconView btn_fwb, btn_fwn, btn_next_video, btn_pre_video, btnEnableAllControl, btn_repeatMode;

    ImageView btnChangeMode;
    LinearLayout btnBackgroundAudio, btnPopupMode, btnRotation, btnVolume, btnBrightness, screenshot, timer, nightmode, speed, imagefornight, mute;
    int fullMode, currentMode;
    boolean isControlLocked = false;
    SeekBar seekBarVolume, seekBarBrightness, simpleSeekBar;
    SeekBar seekBar_gesture_volume, seekBar_gesture_brightness;
    RequestOptions options;
    SurfaceView surfaceView;
    PreviewTimeBar previewTimeBar;
    PlayerControlView playerControlView;
    public static int flag = 0;
    public static int valoumeflag = 0;
    ImageView preViewImage;
    //    ImageView playlist, option;
    ImageView imgaeclose;
    float val = (float) 0.0;
    float valbrightness = (float) 0.0;

    private boolean intLeft, intRight;
    private int sWidth, sHeight;
    private long diffX, diffY;
    private Display display;
    private Point size;
    private float downX, downY;
    private AudioManager mAudioManager;
    Point screenSize;
    Toolbar toolbar;
    TextView timertext;
    LinearLayout timertextlayout;
    VideoPlayingListAdapter videoPlayingListAdapter;
    RecyclerView recyclerView;
    TextView mutetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_play_video);
        surfaceView = findViewById(R.id.surface_view);
//        String newString;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                newString= null;
//            } else {
//                newString= extras.getString("activity");
//            }
//        } else {
//            newString= (String) savedInstanceState.getSerializable("activity");
//        }
        Intent intent= getIntent();
        if(intent.hasExtra("activity")){

            if(intent.getStringExtra("activity").equals("v")){
                surfaceView.setVisibility(View.VISIBLE);
            }else {
                surfaceView.setVisibility(View.GONE);



            }

        }

        getScreenSizeagain();
        imagefornight = findViewById(R.id.imagefornight);
        frameLayout = findViewById(R.id.banner_container);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PlayVideoActivity.this);
        boolean name = sharedPreferences.getBoolean("backgroundplaycheck", false);


        Toast.makeText(this, "" + PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "" + !PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio(), Toast.LENGTH_SHORT).show();
        if (name) {
            if (PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio())
                mMainutils.openNotification();
//            else mMainutils.closeNotification();
//            PreferencesUtility.getInstance(PlayVideoActivity.this).setAllowBackgroundAudio(!PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio());

            //  mMainutils.openNotification();


        }

        imgaeclose = findViewById(R.id.imageclose);
        seekBar_gesture_volume = findViewById(R.id.seekBar_gesture_volume);
        seekBar_gesture_volume.setEnabled(false);
        seekBar_gesture_brightness = findViewById(R.id.seekBar_gesture_brightness);
        seekBar_gesture_brightness.setEnabled(false);

        timertext = findViewById(R.id.timertext);
//        playlist = findViewById(R.id.playlist);
//        option = findViewById(R.id.option);
        mutetext = findViewById(R.id.mutetext);

        // setUpGestureControls();
        if (valoumeflag == 0) {
            mutetext.setText("Mute");
        }
        timertextlayout = findViewById(R.id.timershow);
        speed = findViewById(R.id.speed);
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        mute = findViewById(R.id.mute);
        //  final int sessionId = Mainutils.getInstance().getPlayer().getAudioSessionId();

//        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
//                .setAccentColor(Color.parseColor("#555555"))
//                .setAudioSessionId(sessionId)
//                .build();
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int intCurrentY = Math.round(event.getY());
                int intCurrentX = Math.round(event.getX());
                int intStartY = event.getHistorySize() > 0 ? Math.round(event.getHistoricalY(0)) : intCurrentY;
                int intStartX = event.getHistorySize() > 0 ? Math.round(event.getHistoricalX(0)) : intCurrentX;

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        //touch is start
                        downX = event.getX();
                        downY = event.getY();
                        if (event.getX() < (sWidth / 2)) {

                            //here check touch is screen left or right side
                            intLeft = true;
                            intRight = false;

                        } else if (event.getX() > (sWidth / 2)) {

                            //here check touch is screen left or right side
                            intLeft = false;
                            intRight = true;
                        }

                        startTime = System.currentTimeMillis();
                        clickCount++;
                        break;


                    case MotionEvent.ACTION_UP:
                        seekBar_gesture_volume.setVisibility(View.GONE);
                        seekBar_gesture_brightness.setVisibility(View.GONE);
                        long time = System.currentTimeMillis() - startTime;
                        if (intLeft) {


                            duration = duration + time;
                            if (clickCount == 2) {
                                if (duration <= MAX_DURATION) {
                                    long currentPosition = mMainutils.getPlayer().getCurrentPosition();
                                    if (currentPosition + 5 * 1000 < mMainutils.getPlayer().getDuration())
                                        mMainutils.getPlayer().seekTo(currentPosition + 10000);
                                }
                                clickCount = 0;
                                duration = 0;
                            } else if (intRight) {
                                duration = duration + time;
                                if (clickCount == 2) {
                                    if (duration <= MAX_DURATION) {
                                        long currentPosition = mMainutils.getPlayer().getCurrentPosition();
                                        if (currentPosition + 5 * 1000 < mMainutils.getPlayer().getDuration())
                                            mMainutils.getPlayer().seekTo(currentPosition + 10000);
                                    }
                                    clickCount = 0;
                                    duration = 0;


                                }
                            }

                            break;
                        }

                    case MotionEvent.ACTION_MOVE:
                        //finger move to screen
                        float x2 = event.getX();
                        float y2 = event.getY();

                        diffX = (long) (Math.ceil(event.getX() - downX));
                        diffY = (long) (Math.ceil(event.getY() - downY));

                        if (Math.abs(diffY) > Math.abs(diffX)) {
                            if (intLeft) {
                                //if left its for brightness
                                seekBar_gesture_brightness.setVisibility(View.VISIBLE);
                                seekBar_gesture_brightness.setMax(100);

                                if (downY < y2) {
                                    seekBar_gesture_brightness.setVisibility(View.VISIBLE);
                                    seekBar_gesture_brightness.setProgress((int) (valbrightness - 0.5));
                                    changeBrightness((int) (valbrightness - 0.5));
                                    valbrightness = (float) (valbrightness - 0.5);
                                } else if (downY > y2) {


                                    seekBar_gesture_brightness.setVisibility(View.VISIBLE);
                                    //   seekBar_gesture_volume.setEnabled(false);
                                    seekBar_gesture_brightness.setProgress((int) (valbrightness + 0.5));
                                    // seekBar_gesture_volume.setProgress(getStreamVolume());
                                    changeBrightness((int) (valbrightness + 0.5));
                                    valbrightness = (float) (valbrightness + 0.5);
                                    // seekBar_gesture_brightness.setVisibility(View.GONE);


                                    //up  swipe brightness increase
                                }

                            } else if (intRight) {
                                seekBar_gesture_volume.setVisibility(View.VISIBLE);

                                seekBar_gesture_volume.setMax(getMaxVolume());

                                //if right its for audio
                                if (downY < y2) {
                                    seekBar_gesture_volume.setVisibility(View.VISIBLE);
                                    seekBar_gesture_volume.setProgress((int) (val - 0.5));
                                    setVolume((int) (val + 0.5));
                                    val = (float) (val - 0.5);
                                    //down swipe volume decrease
                                    //  mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

                                } else if (downY > y2) {


                                    seekBar_gesture_volume.setVisibility(View.VISIBLE);
                                    //   seekBar_gesture_volume.setEnabled(false);
                                    seekBar_gesture_volume.setProgress((int) (val + 0.5));
                                    // seekBar_gesture_volume.setProgress(getStreamVolume());
                                    setVolume((int) (val + 0.5));
                                    val = (float) (val + 0.5);
                                    //up  swipe volume increase
                                    //  mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                                }
                            }
                        }
                }

                return true;
            }

        });
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutspeed != null)
                    layoutspeed.setVisibility(View.VISIBLE);
                if (layout_region_volume != null && layoutspeed != null) {
                    layout_region_volume.setVisibility(View.GONE);
                    layout_region_brightness.setVisibility(View.GONE);
                }


                delayHideControl();
            }
//                PlaybackParameters param = new PlaybackParameters(1.5f);
//
//
//                mGlobalVar.chnage(param);
//            }
        });
//        speed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GlobalVar.getInstance().videoService.change(2f);
//            }
//        });


        imgaeclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.GONE);
                imgaeclose.setVisibility(View.GONE);
            }
        });
        imagefornight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });


        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AudioManager audioManager = (AudioManager)PlayVideoActivity.this.getSystemService(Context.AUDIO_SERVICE);

                if (valoumeflag == 0) {
                    mMainutils.getPlayer().setVolume(0);
                    mutetext.setText("Unmute");
                    //  audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    valoumeflag = 1;
                } else if (valoumeflag == 1) {
                    mutetext.setText("Mute");


                    mMainutils.getPlayer().setVolume(10);

                    valoumeflag = 0;
                }

            }
        });
        // Toast.makeText(this, "" + Mainutils.getInstance().playingVideo.getFileTitle(), Toast.LENGTH_SHORT).show();
        fullMode = AspectRatioFrameLayout.RESIZE_MODE_FILL;
        currentMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        screenSize =

                getScreenSize();

        toolbar =

                findViewById(R.id.toolbarforplayer);

        setSupportActionBar(toolbar);
        if (

                getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initializePlayer();

        initControlView();

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {

        long currentPosition = mMainutils.getPlayer().getCurrentPosition();
        if (currentPosition + 10 * 1000 < mMainutils.getPlayer().getDuration())
            mMainutils.getPlayer().seekTo(currentPosition + 10000);


        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(distanceX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                Toast.makeText(PlayVideoActivity.this, "" + getCurrentBrightness(), Toast.LENGTH_SHORT).show();

                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(distanceY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {

                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
//                try {
//                    float diffY = e2.getY() - e1.getY();
//                    float diffX = e2.getX() - e1.getX();
//                    if (Math.abs(diffX) > Math.abs(diffY)) {
//                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffX > 0) {
//                                onSwipeRight();
//                            } else {
//                                onSwipeLeft();
//                            }
//                        }
//                    } else {
//                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffY > 0) {
//                                onSwipeBottom();
//                            } else {
//                                onSwipeTop();
//                            }
//                        }
//                    }
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
                return result;
            }
        }

        public void onSwipeRight() {
            //     Toast.makeText(PlayVideoActivity.this, "right", Toast.LENGTH_SHORT).show();
            Toast.makeText(PlayVideoActivity.this, "" + getCurrentBrightness(), Toast.LENGTH_SHORT).show();


            /// changeBrightness();
            changeBrightness(getCurrentBrightness() + 1);

        }

        public void onSwipeLeft() {


        }

        public void onSwipeTop() {
            //Toast.makeText(PlayVideoActivity.this, "top", Toast.LENGTH_SHORT).show();
            //Toast.makeText(PlayVideoActivity.this, ""+getCurrentBrightness(), Toast.LENGTH_SHORT).show();


        }

    }

    public void onSwipeBottom() {
        Toast.makeText(PlayVideoActivity.this, "" + getCurrentBrightness(), Toast.LENGTH_SHORT).show();

        changeBrightness(getCurrentBrightness() - 1);

        //Toast.makeText(PlayVideoActivity.this, "bottom", Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        if (!PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio())
            mMainutils.closeNotification();
        else mMainutils.openNotification();
        PreferencesUtility.getInstance(PlayVideoActivity.this).setAllowBackgroundAudio(PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio());

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.more_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getScreenSizeagain() {
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        sWidth = size.x;
        sHeight = size.y;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_menu:
                if (expandableRecyclerView != null) {
                    expandableRecyclerView.toggle();
                    hideSystemUi();
                }
                return true;
//            case R.id.delete:
//                CustomDeltedialog cdelete = new CustomDeltedialog(PlayVideoActivity.this);
//                cdelete.show();
            // return true;
            case R.id.properties:
                FileItem file = new FileItem();
                CustominfoClass custominfoClass = new CustominfoClass(PlayVideoActivity.this, Mainutils.getInstance().playingVideo);
                custominfoClass.show();
                return true;
            case R.id.equalizer:
                startActivity(new Intent(PlayVideoActivity.this, Equalizeracitity.class));
                return true;
            case R.id.share:
                if (mMainutils.playingVideo != null)
                    startActivity(Intent.createChooser(AppsUtils.shareVideo(PlayVideoActivity.this, mMainutils.playingVideo),
                            getString(R.string.action_share)));
                return true;
            case R.id.backgroundplay:
                if (PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio())
                    mMainutils.closeNotification();
                else mMainutils.openNotification();
                PreferencesUtility.getInstance(PlayVideoActivity.this).setAllowBackgroundAudio(!PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio());

                return true;
            case R.id.popupplay:
                showFloatingView(PlayVideoActivity.this, true);

                return true;
            case R.id.shuffle:
                mMainutils.getPlayer().setShuffleModeEnabled(true);

                return true;
            case R.id.repeatcurrent:
                mMainutils.getPlayer().setRepeatMode(Player.REPEAT_MODE_ONE);
                return true;
            case R.id.loopall:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PlayVideoActivity.this);
                boolean name = sharedPreferences.getBoolean("autoplay", false);

                if (!name) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PlayVideoActivity.this);
                    prefs.edit().putBoolean("autoplay", true).apply();
                }

//                mMainutils.getPlayer().setShuffleModeEnabled(false);
//                mMainutils.getPlayer().setRepeatMode(Player.REPEAT_MODE_ALL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        else if(id == R.id.action_delete){
//            if(videoPlayingListAdapter != null && mGlobalVar.playingVideo != null)
//                videoPlayingListAdapter.deleteVideo(mGlobalVar.playingVideo);
//        }
    }


    public class CustominfoClass extends Dialog {

        public Activity c;
        public Dialog d;
        TextView foldername;

        TextView folderpath;


        TextView foldersize;


        TextView foderdate;


        TextView ok;

        FileItem folderItem;

        public CustominfoClass(Activity a, FileItem folderItem) {
            super(a);
            // TODO Auto-generated constructor stub
            this.folderItem = folderItem;
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.content_folder_info);
            foldername = findViewById(R.id.foldername);
            folderpath = findViewById(R.id.folderlocation);

            foldersize = findViewById(R.id.foldersize);


            foderdate = findViewById(R.id.folderdate);
            ok = findViewById(R.id.ok);

            foldername.setText(folderItem.getFolderName());
            folderpath.setText(folderItem.getPath());
            File file = new File(folderItem.getPath());

            //     foldersize.setText("" + humanReadableByteCountSI(folderSize(file)));
            Date lastModDate = new Date(file.lastModified());
            foderdate.setText("" + lastModDate);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }


    }

    public class CustomDeltedialog extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        int position;

        public CustomDeltedialog(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_delete_dialog);
            yes = (Button) findViewById(R.id.yes);
            no = (Button) findViewById(R.id.no);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    Mainutils.getInstance().videoBackendService.stopService(new Intent(PlayVideoActivity.this, VideoBackendService.class));
                    finish();
                    dismiss();
                    break;
                case R.id.no:


                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    private void initializePlayer() {
        playerControlView = findViewById(R.id.player_control_view);
        playerControlView.setVisibilityListener(visibility -> {
            if (visibility == PlayerControlView.GONE) {
                toolbar.setVisibility(View.GONE);
                hideSystemUi();
                if (layout_region_volume != null) layout_region_volume.setVisibility(View.GONE);
                if (layout_region_brightness != null)
                    layout_region_brightness.setVisibility(View.GONE);
                if (layoutspeed != null) {

                    layoutspeed.setVisibility(View.GONE);
                }
//                if(expandableLayout != null && expandableTitleLayout != null)
//                    if(expandableLayout.isExpanded()){
//                        expandableTitleLayout.toggle();
//                        expandableLayout.toggle();
//                    }
            }
            if (visibility == PlayerControlView.VISIBLE) {
                toolbar.setVisibility(View.VISIBLE);
                delayHideControl();
                showSystemUI();
            }
        });
        playerControlView.setOnClickListener(v -> playerControlView.hide());
        playerControlView.setPlayer(mMainutils.getPlayer());
        mPlayerView = findViewById(R.id.player_view);
//        BitmapDrawable d = (BitmapDrawable) getDrawable(R.drawable.image);
//        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//        mPlayerView.setDefaultArtwork(bitmap);
        mPlayerView.requestFocus();
        if (mMainutils.getPlayer() == null)
            return;

        mPlayerView.setPlayer(mMainutils.getPlayer());
        mPlayerView.setResizeMode(currentMode);


    }

    private void initControlView() {
        btn_repeatMode = findViewById(R.id.btn_repeatMode);
        if (mMainutils.getPlayer() == null) return;
        setRepeatModeIcon();
        btn_repeatMode.setOnClickListener(v -> {
            if (mMainutils.getPlayer().getShuffleModeEnabled()) {
                mMainutils.getPlayer().setShuffleModeEnabled(false);
                mMainutils.getPlayer().setRepeatMode(Player.REPEAT_MODE_ALL);
                btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
            } else if (mMainutils.getPlayer().getRepeatMode() == Player.REPEAT_MODE_ALL) {
                mMainutils.getPlayer().setRepeatMode(Player.REPEAT_MODE_ONE);
                btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
            } else if (mMainutils.getPlayer().getRepeatMode() == Player.REPEAT_MODE_ONE) {
                mMainutils.getPlayer().setRepeatMode(Player.REPEAT_MODE_OFF);
                btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_OFF);
            } else {
                mMainutils.getPlayer().setShuffleModeEnabled(true);
                btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE);
            }
        });

        btnBackgroundAudio = findViewById(R.id.btn_bgAudio);
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean name = sharedPreferences.getBoolean("backgroundplaycheck", false);
        //  Toast.makeText(context, "" + name, Toast.LENGTH_SHORT).show();
        //   btnBackgroundAudio.setBackgroundColor(PreferencesUtility.getInstance(this).isAllowBackgroundAudio() && sharedPreferences.getBoolean("backgroundplaycheck", false) ? Color.GREEN : Color.WHITE);
        btnBackgroundAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagforbackground == 0) {
                    btnBackgroundAudio.setBackgroundResource(R.drawable.rectangle);

                    PreferencesUtility.getInstance(PlayVideoActivity.this).setAllowBackgroundAudio(!PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio());
                    btnBackgroundAudio.setBackgroundColor(PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio() && sharedPreferences.getBoolean("backgroundplaycheck", false) ? Color.GREEN : Color.WHITE);
                    flagforbackground = 1;
                } else if (flagforbackground == 1) {
                    btnBackgroundAudio.setBackgroundResource(R.drawable.grey);

                    flagforbackground = 0;
                }
//                //  sharedPreferences.getBoolean("backgroundplaycheck", true);
//                  PreferencesUtility.getInstance(PlayVideoActivity.this).setAllowBackgroundAudio(!PreferencesUtility.getInstance(this).isAllowBackgroundAudio());
//                   btnBackgroundAudio.setBackgroundColor(PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio() && sharedPreferences.getBoolean("backgroundplaycheck", false) ? Color.GREEN : Color.WHITE);

            }
        });
        btnRotation = findViewById(R.id.btn_btnRotation);
        btnRotation.setOnClickListener(v -> {
            btnRotation.setBackgroundResource(R.drawable.rectangle);
            int requestScreenInfo = getRequestedOrientation();
            if (requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else if (requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            } else if (requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
            updateScreenOrientationIco(getRequestedOrientation());

        });

        nightmode = findViewById(R.id.nightmode);
        nightmode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //       Toast.makeText(PlayVideoActivity.this, "sdfg", Toast.LENGTH_SHORT).show();
                if (flag == 0) {

                    //  Toast.makeText(PlayVideoActivity.this, "" + flag, Toast.LENGTH_SHORT).show();

                    imagefornight.setVisibility(View.VISIBLE);
                    flag = 1;
                } else if (flag == 1) {

                    //   Toast.makeText(PlayVideoActivity.this, "" + flag, Toast.LENGTH_SHORT).show();

                    imagefornight.setVisibility(View.GONE);
                    flag = 0;

                }
            }


        });
//            int requestScreenInfo = getRequestedOrientation();
//            if(requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||  requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT){
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//            }else if(requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE){
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//            }else if(requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_SENSOR || requestScreenInfo == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR){
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//            }
//            updateScreenOrientationIco(getRequestedOrientation());


        screenshot = findViewById(R.id.screenshot);
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Toast.makeText(context, "ss", Toast.LENGTH_SHORT).show();

                takeScreenshot();


            }
        });
        timer = findViewById(R.id.timer);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CharSequence options[] = new CharSequence[]{"off", "15 minutes", "30 minuts", "45 minuts"};

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayVideoActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Select your option:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CountDownTimer countDownTimer = null;
                        if (which == 0) {
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                timertextlayout.setVisibility(View.GONE);

                            }
                        } else if (which == 1) {
                            timertextlayout.setVisibility(View.VISIBLE);
                            countDownTimer = new CountDownTimer(900000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    timertext.setText("" + new SimpleDateFormat("mm:ss:SS").format(new Date(millisUntilFinished)));
                                }

                                public void onFinish() {
                                    timertext.setVisibility(View.GONE);
                                    finishAffinity();
                                }
                            }.start();
                            countDownTimer.start();

                        } else if (which == 2) {
                            timertextlayout.setVisibility(View.VISIBLE);

                            countDownTimer = new CountDownTimer(900000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    timertext.setText("" + new SimpleDateFormat("mm:ss:SS").format(new Date(millisUntilFinished)));
                                }


                                public void onFinish() {
                                    finishAffinity();

                                    timertext.setVisibility(View.GONE);
                                }
                            };

                            countDownTimer.start();
                        } else if (which == 3) {
                            timertextlayout.setVisibility(View.VISIBLE);
                            countDownTimer = new CountDownTimer(900000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    timertext.setText("" + new SimpleDateFormat("mm:ss:SS").format(new Date(millisUntilFinished)));
                                }

                                public void onFinish() {
                                    finishAffinity();

                                    timertext.setVisibility(View.GONE);
                                }
                            }.start();
                            countDownTimer.start();


                        }


                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();
              /*  timertextlayout.setVisibility(View.VISIBLE);
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //Toast.makeText(context, "ghj"+millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
                     //   Toast.makeText(context, "" + new SimpleDateFormat("mm:ss:SS").format(new Date(millisUntilFinished)), Toast.LENGTH_SHORT).show();
                        timertext.setText("" + new SimpleDateFormat("mm:ss:SS").format(new Date(millisUntilFinished)));
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        timertext.setText("done!");

                                        }

                }.start();
*/

            }
        });
        int defaultOrientation = PreferencesUtility.getInstance(this).getScreenOrientation();
        setScreenOrientation(defaultOrientation);
        seekBarBrightness = findViewById(R.id.seekBar_brightness);
        seekBarBrightness.setMax(100);
        seekBarBrightness.setProgress(getCurrentBrightness());
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    delayHideControl();
                    changeBrightness(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarVolume = findViewById(R.id.seekBar_volume);
        if (getMaxVolume() >= -1) {
            seekBarVolume.setMax(getMaxVolume());
            seekBarVolume.setProgress(getStreamVolume());
        }
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setVolume(progress);
                    delayHideControl();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        simpleSeekBar = (SeekBar) findViewById(R.id.seekBar_speed);
        simpleSeekBar.setMax(5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            simpleSeekBar.setMin(1);
        }
        simpleSeekBar.setHovered(true);
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if (fromUser) {

                    PlaybackParameters param = new PlaybackParameters(progress);
//
//
                    mMainutils.chnage(param);


                }

                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(PlayVideoActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnBrightness = findViewById(R.id.btnBrightness);
        btnBrightness.setOnClickListener(v -> {
            if (layout_region_brightness != null)
                layout_region_brightness.setVisibility(View.VISIBLE);
            if (layout_region_volume != null) layout_region_volume.setVisibility(View.GONE);
            delayHideControl();
        });
        btnVolume = findViewById(R.id.btnVolumes);
        btnVolume.setOnClickListener(v -> {
            if (layout_region_volume != null) layout_region_volume.setVisibility(View.VISIBLE);
            if (layout_region_brightness != null)
                layout_region_brightness.setVisibility(View.GONE);
            delayHideControl();
        });

        layout_control_top = findViewById(R.id.layout_title_top);
        btnPopupMode = findViewById(R.id.btn_popup);
        btnPopupMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flagforpopup == 0) {
                    btnPopupMode.setBackgroundResource(R.drawable.rectangle);

                    PlayVideoActivity.this.showFloatingView(PlayVideoActivity.this, true);
                    flagforpopup = 1;
                } else if (flagforpopup == 1) {
                    btnPopupMode.setBackgroundResource(R.drawable.grey);

                    flagforpopup = 0;
                }
            }
        });

        btnExpandLayout = findViewById(R.id.btn_btnExpandControl);
        btnExpandLayout.setOnClickListener(v -> {
            delayHideControl();
            if (expandableLayout != null) expandableLayout.toggle();
        });
        expandableLayout = findViewById(R.id.expandable_layout);
        expandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> btnExpandLayout.setRotation(expansionFraction * 180));
        layout_region_volume = findViewById(R.id.region_volume);
        layout_region_brightness = findViewById(R.id.region_brightness);
        layoutspeed = findViewById(R.id.speed_brightness);

        btnEnableAllControl = findViewById(R.id.btnEnableAllControl);
        btnEnableAllControl.setOnClickListener(v -> {
            btnEnableAllControl.setVisibility(View.GONE);
            isControlLocked = false;
            showControl();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            updateScreenOrientationIco(getRequestedOrientation());
        });

        /// bot control
        layout_skip_back_10 = findViewById(R.id.layout_skip_pre_10s);
        layout_skip_next_10 = findViewById(R.id.layout_skip_next_10s);
        frameLayout_preview = findViewById(R.id.previewFrameLayout);
        preViewImage = findViewById(R.id.preImageView);
        previewTimeBar = findViewById(R.id.previewTimebar);
        previewTimeBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                mMainutils.isSeekBarProcessing = true;
                delayHideControl();
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                delayHideControl();
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                mMainutils.getPlayer().seekTo(position);
                previewTimeBar.setPosition(position);
                delayHideControl();

            }
        });
        previewTimeBar.setPreviewLoader((currentPosition, max) -> {
            if (preViewImage != null)
                options = new RequestOptions().frame(currentPosition * 1000);
            Glide.with(PlayVideoActivity.this)
                    .load(mMainutils.playingVideo.getPath())
                    .apply(options)
                    .into(preViewImage);
        });
        previewTimeBar.addOnPreviewChangeListener(this);

        layout_btn_bottom = findViewById(R.id.layout_btn_bot);
        btnPausePlay = findViewById(R.id.btnPlayPause);
        btnPausePlay.setImageDrawable(playPauseDrawable);

        if (mMainutils.getPlayer().getPlayWhenReady()) {
            playPauseDrawable.transformToPause(true);
        } else {
            playPauseDrawable.transformToPlay(true);
        }

        btnPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataProvider.getbuy()) {
                    UnifiedNativeAd native_admob = DataProvider.getInstance().get_native_admob();
                    if (native_admob != null) {
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_back_button, null);
                        populateUnifiedNativeAdView(native_admob, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        DataProvider.getInstance().load_native_admob();
                    }

                }

                PlayVideoActivity.this.delayHideControl();
                if (mMainutils.getPlayer() != null) {
                    isVideoPlaying = !mMainutils.getPlayer().getPlayWhenReady();

                    mMainutils.pausePlay();
                    playPauseDrawable.transformToPlay(true);
                    playPauseDrawable.transformToPause(true);
                }

            }
        });


        btnChangeMode = findViewById(R.id.btnResize);
        btnChangeMode.setOnClickListener(v -> {
            delayHideControl();
            currentMode += 1;
            if (currentMode > 4)
                currentMode = 0;
            mPlayerView.setResizeMode(currentMode);
        });

        btn_fwb = findViewById(R.id.btn_skip_pre_10s);
        btn_fwb.setOnClickListener(v -> {
            long currentPostion = mMainutils.getPlayer().getCurrentPosition();
            if (currentPostion - 10 * 1000 < 0) currentPostion = 0;
            mMainutils.getPlayer().seekTo(currentPostion - 10000);
        });
        btn_fwn = findViewById(R.id.btn_skip_next_10s);
        btn_fwn.setOnClickListener(v -> {
            long currentPosition = mMainutils.getPlayer().getCurrentPosition();
            if (currentPosition + 10 * 1000 < mMainutils.getPlayer().getDuration())
                mMainutils.getPlayer().seekTo(currentPosition + 10000);
        });
        btn_next_video = findViewById(R.id.btn_skip_next);
        btn_next_video.setOnClickListener(v -> {
            mMainutils.playNext();
        });
        btn_pre_video = findViewById(R.id.btn_skip_pre);
        btn_pre_video.setOnClickListener(v -> {
            mMainutils.playPrevious();
        });
        btnLockControl = findViewById(R.id.btnLock);
        btnLockControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_all_control_container != null && btnEnableAllControl != null) {
                    isControlLocked = true;
                    if (playerControlView != null) playerControlView.hide();
                    btnEnableAllControl.setVisibility(View.VISIBLE);
                    int currentOrientation = PlayVideoActivity.this.getCurrentOrientation();
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        PlayVideoActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                        PlayVideoActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    }

                    PlayVideoActivity.this.updateScreenOrientationIco(PlayVideoActivity.this.getRequestedOrientation());
                }
            }
        });

        /// container control
        layout_all_control_container = findViewById(R.id.layout_all_control_container);

        r = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastTouchTime > CONTROL_HIDE_TIMEOUT) {
                    hideControlContainer();
                }
                layout_all_control_container.postDelayed(this, 500);
                if (mMainutils.getPlayer() != null) {
                    if (mMainutils.getPlayer().getPlayWhenReady() != isVideoPlaying) {
                        isVideoPlaying = mMainutils.getPlayer().getPlayWhenReady();
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(mMainutils.playingVideo.getFileTitle());
                        if (isVideoPlaying)
                            playPauseDrawable.transformToPause(false);
                        else playPauseDrawable.transformToPlay(false);
                    }
                    if (previewTimeBar != null) {
                        if (currentVideo != mMainutils.playingVideo && mMainutils.getPlayer().getPlaybackState() == Player.STATE_READY) {
                            if (getSupportActionBar() != null)
                                getSupportActionBar().setTitle(mMainutils.playingVideo.getFileTitle());
                            currentVideo = mMainutils.playingVideo;
                            previewTimeBar.setDuration(mMainutils.getPlayer().getDuration());
                        } else if (!mMainutils.isSeekBarProcessing) {
                            previewTimeBar.setPosition(mMainutils.getPlayer().getCurrentPosition());
                            //Toast.makeText(PlayVideoActivity.this,"update seek",Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (currentVideoPosition != mMainutils.getCurrentPosition()) {
                        currentVideoPosition = mMainutils.getCurrentPosition();
                        if (currentVideoPosition >= 0 && currentVideoPosition < mMainutils.videoItemsPlaylist.size())
                            recyclerView.smoothScrollToPosition(currentVideoPosition);
                    }
                }
            }
        };
        layout_all_control_container.postDelayed(r, 500);
        layout_all_control_container.setOnClickListener(v ->
                showControl()
        );

        recyclerView = findViewById(R.id.recyclerView_playlist);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videoPlayingListAdapter = new VideoPlayingListAdapter(this);
        recyclerView.setAdapter(videoPlayingListAdapter);
        videoPlayingListAdapter.updateData(mMainutils.videoItemsPlaylist);
        if (currentVideoPosition != mMainutils.getCurrentPosition()) {
            currentVideoPosition = mMainutils.getCurrentPosition();
            if (currentVideoPosition >= 0 && currentVideoPosition < mMainutils.videoItemsPlaylist.size())
                recyclerView.smoothScrollToPosition(currentVideoPosition);
        }


        expandableRecyclerView = findViewById(R.id.expandable_recyclerView_layout);
        btnClosePlaylist = findViewById(R.id.btn_CloseList);
        btnClosePlaylist.setOnClickListener(v -> {
            if (expandableRecyclerView != null)
                expandableRecyclerView.toggle();
        });


        doLayoutChange(getCurrentOrientation());
    }

    private void setRepeatModeIcon() {
        if (mMainutils.getPlayer() == null) return;
        if (mMainutils.getPlayer().getShuffleModeEnabled()) {
            btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE);
        } else if (mMainutils.getPlayer().getRepeatMode() == Player.REPEAT_MODE_OFF) {
            btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_OFF);
        } else if (mMainutils.getPlayer().getRepeatMode() == Player.REPEAT_MODE_ONE) {
            btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
        } else {
            btn_repeatMode.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
        }
    }

    private void changeBrightness(int value) {
        if (value <= 5) value = 5;
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = (float) value / 100;
        getWindow().setAttributes(layoutParams);
    }

    private int getCurrentBrightness() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 70;
        }
    }

    boolean isVolumeVisible = false;

    private int getMaxVolume() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volumeStreamMax = 0;
        try {
            if (audioManager != null)
                volumeStreamMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            if (volumeStreamMax < 1)
                volumeStreamMax = 1;
            isVolumeVisible = true;
        } catch (Throwable ex) {
            isVolumeVisible = false;
            ex.printStackTrace();
            return -1;
        }
        return volumeStreamMax;
    }

    public void setVolume(int volume) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int oldVolume = getStreamVolume();
        try {
            if (audioManager != null)
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, (volume >= oldVolume) ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER, 0);
        } catch (Throwable ex) {
            //too bad...
        }
        //apparently a few devices don't like to have the streamVolume changed from another thread....
        //maybe there is another reason for why it fails... I just haven't found yet :(
        try {
            if (audioManager != null)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        } catch (Throwable ex) {
            //too bad...
        }
    }

    private int getStreamVolume() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null)
            return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return 0;
    }

    private void setScreenOrientation(int value) {
        setRequestedOrientation(value);
        updateScreenOrientationIco(value);

    }

    private void updateScreenOrientationIco(int value) {
        if (btnRotation != null) {
            if (value == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || value == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                //   btnRotation.(MaterialDrawableBuilder.IconValue.PHONE_ROTATE_LANDSCAPE);
            } else if (value == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || value == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                //  btnRotation.setIcon(MaterialDrawableBuilder.IconValue.PHONE_ROTATE_PORTRAIT);
            } else if (value == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR || value == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                //  btnRotation.setIcon(MaterialDrawableBuilder.IconValue.SCREEN_ROTATION);
            }
        }
        PreferencesUtility.getInstance(this).setScreenOrientation(value);
    }

    private void hideControlContainer() {
        if (!isControlLocked) {
            if (playerControlView != null)
                playerControlView.hide();
            if (layout_region_volume != null) layout_region_volume.setVisibility(View.GONE);
            if (layout_region_brightness != null)
                layout_region_brightness.setVisibility(View.GONE);
        } else if (btnEnableAllControl != null)
            btnEnableAllControl.setVisibility(View.GONE);
    }

    private void delayHideControl() {
        lastTouchTime = System.currentTimeMillis();
    }

    private void showControl() {
        delayHideControl();
        if (!isControlLocked) {
            if (playerControlView != null) playerControlView.show();
        } else if (btnEnableAllControl != null)
            btnEnableAllControl.setVisibility(View.VISIBLE);

    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "" + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        doLayoutChange(newConfig.orientation);
    }

    private void doLayoutChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (layout_skip_back_10 != null) layout_skip_back_10.setVisibility(View.GONE);
            if (layout_skip_next_10 != null) layout_skip_next_10.setVisibility(View.GONE);
            if (toolbar != null) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
                params.setMarginEnd(10 * (int) getDensity());
            }
            if (btnExpandLayout != null) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btnExpandLayout.getLayoutParams();
                params.setMarginEnd(10 * (int) getDensity());
            }


        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (toolbar != null) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
                params.setMarginEnd(50 * (int) getDensity());
            }
            if (btnExpandLayout != null) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btnExpandLayout.getLayoutParams();
                params.setMarginEnd(50 * (int) getDensity());
            }
            if (layout_skip_back_10 != null) layout_skip_back_10.setVisibility(View.VISIBLE);
            if (layout_skip_next_10 != null) layout_skip_next_10.setVisibility(View.VISIBLE);
        }

    }

    private float getDensity() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (metrics.density);
    }

    private int getCurrentOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void releasePlayer() {

    }

    private Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        if (Mainutils.getInstance().getPlayer() != null) {


        }

        // if()

    }

    @Override
    public void onStop() {
        super.onStop();
        if (!PreferencesUtility.getInstance(this).isAllowBackgroundAudio() && !isPopupModeEnalbe) {
            if (mMainutils.getPlayer() != null && mMainutils.getPlayer().getPlayWhenReady()) {
                mMainutils.pausePlay();
            }
        }
        isPopupModeEnalbe = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (layout_all_control_container != null)
            layout_all_control_container.postDelayed(r, 500);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio())
            mMainutils.closeNotification();
        else mMainutils.openNotification();
        PreferencesUtility.getInstance(PlayVideoActivity.this).setAllowBackgroundAudio(PreferencesUtility.getInstance(PlayVideoActivity.this).isAllowBackgroundAudio());


    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        if (layout_all_control_container != null)
            layout_all_control_container.removeCallbacks(r);

    }


    boolean isPopupModeEnalbe = false;

    public void startPopupMode() {
        Mainutils.getInstance().videoBackendService.playVideo(mMainutils.seekPosition, true);
        isPopupModeEnalbe = true;
        finish();
    }

    int requestCode = 1;

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == resultCode) {
            startPopupMode();
        }
    }


    @SuppressLint("NewApi")
    public void showFloatingView(Context context, boolean isShowOverlayPermission) {
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

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    //    public static void setAutoOrientationEnabled(Context context, boolean enabled)
//    {
//        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
//    }
//    public boolean isAutoRotationState(){
//        if (android.provider.Settings.System.getInt(getContentResolver(),
//                Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
//            return true;
//
//        return false;
//    }
    @Override
    public void onStartPreview(PreviewView previewView, int progress) {

    }

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {

    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {

    }

//    public static void add_equalizer(int id) {
//
//
//        if (EqSettings.equalizerModel == null) {
//            EqSettings.equalizerModel = new EqualizerModel();
//            EqSettings.equalizerModel.setReverbPreset(PresetReverb.PRESET_NONE);
//            EqSettings.equalizerModel.setBassStrength((short) (0 / 19));
//        }
//        Equalizer mEqualizer = new Equalizer(0, id);
//        BassBoost bassBoost = new BassBoost(0, id);
//        bassBoost.setEnabled(EqSettings.isEqualizerEnabled);
//        BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
//        BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
//        bassBoostSetting.strength = EqSettings.equalizerModel.getBassStrength();
//        bassBoost.setProperties(bassBoostSetting);
//        PresetReverb presetReverb = new PresetReverb(0, id);
//        presetReverb.setPreset(EqSettings.equalizerModel.getReverbPreset());
//        presetReverb.setEnabled(EqSettings.isEqualizerEnabled);
//        mEqualizer.setEnabled(EqSettings.isEqualizerEnabled);
//        if (EqSettings.presetPos == 0) {
//            for (short bandIdx = 0; bandIdx < mEqualizer.getNumberOfBands(); bandIdx++) {
//                mEqualizer.setBandLevel(bandIdx, (short) EqSettings.seekbarpos[bandIdx]);
//            }
//        } else {
//            mEqualizer.usePreset((short) EqSettings.presetPos);
//        }
//    }

    private void createDialog(FileItem videoItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.content_video_info, null);

        TextView txtVideoTitle = view.findViewById(R.id.txtVideoTitle);
        txtVideoTitle.setText(videoItem.getFileTitle());

        TextView txtLocation = view.findViewById(R.id.txtLocation_value);
        txtLocation.setText(videoItem.getPath());

        TextView txtVideoFormat = view.findViewById(R.id.txtFormat_value);
        txtVideoFormat.setText(AppsUtils.getFileExtension(videoItem.getPath()));

        TextView txtDuration = view.findViewById(R.id.txtDuration_value);
        txtDuration.setText(videoItem.getDuration());

        TextView txtVideoResolution = view.findViewById(R.id.txResolution_value);
        txtVideoResolution.setText(videoItem.getResolution());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView
            adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.

                super.onVideoEnd();
            }
        });

        com.google.android.gms.ads.formats.MediaView mediaView = (com.google.android.gms.ads.formats.MediaView) adView.findViewById(R.id.ad_media);
        ImageView mainImageView = (ImageView) adView.findViewById(R.id.ad_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);

        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            try {
                List<NativeAd.Image> images = nativeAd.getImages();
                if (images.size() > 0)
                    mainImageView.setImageDrawable(images.get(0).getDrawable());

            } catch (Exception e) {

            }
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        ((Button) adView.getCallToActionView()).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }

//

}
