package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.SettingsActivity;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ViewPagerAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.AllFolderList;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.MainFragment;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.MainMusicFragment;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistFragment;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.serviceCallBack;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.buy_panel;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.permissions.PermissionCallback;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.res.ColorStateList.*;


public class MainClass extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, serviceCallBack {
    private Toolbar toolbar;
    private DrawerLayout mNavDrawer;
    NavigationView navigationView;
    Handler handler_1;
    InterstitialAd mInterstitialAd;
    AllFolderList speedDialFragment;
    MainMusicFragment mainMusicFragment;
    MainFragment mainFragment;
    PlaylistFragment playlistFragment;
    Activity context;
//    static ImageView thumbnail;
//    static TextView songtitle;
//    static TextView totalDuration;
//    static TextView currentPosition;
//    static RelativeLayout backgroundplay;
    private int[] tabIcons = {
            R.drawable.video_check,
            R.drawable.music_check,
            R.drawable.playlist_check
    };

    private String[] tabname = {
            "Video", "Music", "Playlist"
    };

    public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drwaer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainFragment = new MainFragment();
//        backgroundplay = findViewById(R.id.backgroundplay);
//        thumbnail = findViewById(R.id.thumbnail);
//        songtitle = findViewById(R.id.songtitle);
//        totalDuration = findViewById(R.id.totalduration);
//        currentPosition = findViewById(R.id.currentposition);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        boolean name = sharedPreferences.getBoolean("backgroundplaycheck", false);
//        // Toast.makeText(context, "" + name, Toast.LENGTH_SHORT).show();
//        if (name) {
//
//           }
        //   tabLayout.setSelectedTabIndicatorColor((Color.WHITE));//start_handler_network_connection();
        context = getParent();
        speedDialFragment = new AllFolderList();
        mainMusicFragment = new MainMusicFragment();
        playlistFragment = new PlaylistFragment();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mNavDrawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mNavDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mNavDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
//            tabLayout.setupWithViewPager(viewPager);
//            setupTabView();

        //check for permission
//        if (AppsUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && AppsUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            setupViewPager(viewPager);
//            tabLayout.setupWithViewPager(viewPager);
//            setupTabView();
//        } else {
//            if (flag == 0) {
//                CustomDialogClass cdd = new CustomDialogClass(MainClass.this);
//                cdd.show();
//                flag = 1;
//            } else if (flag == 1) {
//                AppsUtils.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);
//
//            }
//
//
//        }
        if (AppsUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && AppsUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadEverything();
        } else {
            CustomDialogClass cdd = new CustomDialogClass(MainClass.this);
            cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            cdd.show();
        }
        String action = getIntent().getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Intent receivedIntent = getIntent();
            Uri receivedUri = receivedIntent.getData();
            assert receivedUri != null;
            String _file = receivedUri.toString();
            mMainutils.playingVideo = new FileItem();
            mMainutils.playingVideo.setPath(_file);
            mMainutils.playingVideo.setFileTitle(AppsUtils.getFileNameFromPath(_file));
            mMainutils.videoItemsPlaylist = new ArrayList<>();
            mMainutils.videoItemsPlaylist.add(mMainutils.playingVideo);
            if (mMainutils.videoBackendService == null) {
                mMainutils.isOpenFromIntent = true;
            } else {
                mMainutils.videoBackendService.playVideo(0, false);
                showFloatingView(MainClass.this, true);
                finish();
            }
        }

    }


    protected final PermissionCallback permissionReadStorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            if (AppsUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && AppsUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                loadEverything();
            } else if (!AppsUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                AppsUtils.askForPermission(MainClass.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionReadStorageCallback);
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(speedDialFragment, "Videos");
        adapter.addFrag(speedDialFragment, "Music");
        adapter.addFrag(playlistFragment, "Playlist");
        viewPager.setAdapter(adapter);
    }


//    public void setupTabView() {
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab);
//            TextView tab_name = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab);
//            tab_name.setText("" + tabname[i]);
//            ImageView imageView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.imageview);
//            imageView.setImageResource(tabIcons[i]);
//        }
//    }
//
//    private void setupTabIcons() {
//
//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabOne.setText("Videos");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_videos, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(tabOne);
//
//        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabTwo.setText("Music");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_music, 0, 0);
//        tabLayout.getTabAt(1).setCustomView(tabTwo);
//
//        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabThree.setText("Playlist");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_playlist, 0, 0);
//        tabLayout.getTabAt(2).setCustomView(tabThree);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.privatefolder) {
            PreferencesUtility preferencesUtility = new PreferencesUtility(MainClass.this);
            if (preferencesUtility.getPassword() == null) {
                startActivity(new Intent(MainClass.this, PinScreenClass.class));
            } else if (preferencesUtility.getPassword() != null) {
                startActivity(new Intent(MainClass.this, PasswordCheck.class));

            }

            //   startActivity(new Intent(MainClass.this, PrivateFolder.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.equlizer:
                mInterstitialAd = DataProvider.getInstance().get_interstitial();
                if ((mInterstitialAd.isLoaded() && DataProvider.show_ad)) {
                    ((RelativeLayout) findViewById(R.id.loading_adlayout)).setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            show_ad();
                        }
                    }, 800);
                }
                startActivity(new Intent(MainClass.this, Equalizeracitity.class));
                break;
            case R.id.removeads:
                startActivity(new Intent(MainClass.this, buy_panel.class));
                break;
            case R.id.hidden:
                PreferencesUtility preferencesUtility = new PreferencesUtility(MainClass.this);
                if (preferencesUtility.getPassword() == null) {
                    startActivity(new Intent(MainClass.this, PinScreenClass.class));
                } else if (preferencesUtility.getPassword() != null) {
                    startActivity(new Intent(MainClass.this, PasswordCheck.class));

                }


                break;
            case R.id.settings:
                mInterstitialAd = DataProvider.getInstance().get_interstitial();
                if ((mInterstitialAd.isLoaded() && DataProvider.show_ad)) {
                    ((RelativeLayout) findViewById(R.id.loading_adlayout)).setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            show_ad();
                        }
                    }, 800);
                } else {
                    finish();
                    DataProvider.toggle_ad_check();
                }
                startActivity(new Intent(MainClass.this, SettingsActivity.class));
                break;

        }

        mNavDrawer.closeDrawer(GravityCompat.START);


        return true;

    }

    @Override
    protected void loadEverything() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
    }

    @Override
    public void reloadData() {
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (Mainutils.getInstance().isbackgroundplay(MainClass.this)) {
//            if (Mainutils.getInstance().getPlayer() != null) {
//
//                addLayout();
//
//            }
//
//
//        }


        //   reloadData();
    }

    @Override
    public void doSomething() {
      //  addLayout();


    }


    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        Button yes, no;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog);
            yes = findViewById(R.id.allow);
            no = findViewById(R.id.close);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.allow:
                    AppsUtils.askForPermission(MainClass.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);
                    break;
                case R.id.close:
                    finishAffinity();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    //    public void start_handler_network_connection()
//    {
//
//        handler_1 = new Handler();
//        handler_1.postDelayed(new Runnable() {
//            @Override
//            public void run()
//            {
//
//
//                if (isNetworkConnected()) {
//
//                    if(handler_1!=null)
//                        handler_1.removeCallbacksAndMessages(null);
//
//                    if(DataProvider.getInstance().read_data_remainig && !DataProvider.getInstance().load_request_send)
//                    {
//                        DataProvider.getInstance().on_complete();
//                        handler_1.postDelayed(this, 5000);
//                    }
//                    if(DataProvider.getInstance().get_Ads()!=null && DataProvider.getInstance().get_Ads().size()>0)
//                    {
//                        start_handler_native();
//                        load_ad_back_dialog();
//                    }
//
//
//                }
//                else if(!DataProvider.getInstance().load_request_send)
//                    handler_1.postDelayed(this, 5000);
//
//
////Do something after 100ms
//            }
//        }, 4000);
//
//    }
    public void show_ad() {

        ((RelativeLayout) findViewById(R.id.loading_adlayout)).setVisibility(View.GONE);
        if ((mInterstitialAd.isLoaded() && DataProvider.show_ad)) {

            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                                              @Override
                                              public void onAdClosed() {
                                                  super.onAdClosed();
                                                  finish();
                                                  DataProvider.getInstance().reload_admob();


                                              }
                                          }
            );
        } else {
            finish();
        }

        DataProvider.toggle_ad_check();

    }

//    public static void addLayout() {
//
//        if (Mainutils.getInstance().playingVideo != null) {
//            backgroundplay.setVisibility(View.VISIBLE);
//
//            Glide.with(VideopUtils.context)
//                    .load(Mainutils.getInstance().playingVideo.getPath())
//                    .into(thumbnail);
//            songtitle.setText(Mainutils.getInstance().playingVideo.getFileTitle());
//            totalDuration.setText(Mainutils.getInstance().playingVideo.getDuration());
//            updateTime();
//        }
//
//
//    }


//    static void updateTime() {
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                currentPosition.setText("" + Mainutils.getInstance().getPlayer().getCurrentPosition());
//
//            }
//        }, 0, 1000);//put here time 1000 milliseconds=1 second
//
//        Runnable updater = null;
//        final Handler timerHandler = new Handler();
//
//        Runnable finalUpdater = updater;
//        updater = new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(VideopUtils.context, "" + Mainutils.getInstance().getPlayer().getCurrentPosition(), Toast.LENGTH_SHORT).show();
//                currentPosition.setText("" + Mainutils.getInstance().getPlayer().getCurrentPosition());
//                timerHandler.postDelayed(finalUpdater, 1000);
//            }
//        };
//        timerHandler.post(updater);
//    }

    @Override
    public void onBackPressed() {
//    mainFragment.mainframgnetnisuue();
//        if(viewPager.getCurrentItem()!=0){
//        }else{
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Exit");
//        builder.setMessage("Do you want to Exit this? ");
//        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                finishAffinity();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                dialog.dismiss();
//            }
//        });
//        builder.show();

    }


}
