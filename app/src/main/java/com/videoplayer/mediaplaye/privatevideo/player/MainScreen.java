package com.videoplayer.mediaplaye.privatevideo.player;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ViewPagerAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.AllFolderList;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.MainMusicFragment;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistFragment;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainScreen extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout mNavDrawer;
    NavigationView navigationView;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_videos,
            R.drawable.musicicon,
            R.drawable.ic_playlist
    };
    AllFolderList speedDialFragment;
    MainMusicFragment mainMusicFragment;
    PlaylistFragment playlistFragment;
    public static int flagforfragment=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drwaer_layout);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        speedDialFragment= new AllFolderList();
        mainMusicFragment= new MainMusicFragment();
        playlistFragment= new PlaylistFragment();
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
        viewPager =  findViewById(R.id.view_pager);
        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
//        if (AppsUtils.isMarshmallow()) {
//            checkPermissionAndThenLoad();
//
//        }

//        else {
//
//            //setupViewPager(viewPager);
//
//
//
//
//            //checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
//         }

        //loadEverything();




    }
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(speedDialFragment, "Videos");
//
//        adapter.addFrag(mainMusicFragment, "Music");
//
//        adapter.addFrag(playlistFragment, "Playlist");
//        viewPager.setAdapter(adapter);
//    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainScreen.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(MainScreen.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavDrawer.closeDrawer(GravityCompat.START);
        }
        else {

            new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    })

                    .show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);


        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainScreen.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainScreen.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_settings:

                speedDialFragment.setLayoutManager();



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

     @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.select:
                break;
            case R.id.equlizer:
                break;
            case R.id.removeads:
                break;
            case R.id.hidden:
                break;
            case R.id.settings:
                startActivity(new Intent(MainScreen.this, SettingsActivity.class));
                break;
        }
         mNavDrawer.closeDrawer(GravityCompat.START);
        return true;
    }




}