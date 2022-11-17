package com.videoplayer.mediaplaye.privatevideo.player.activities;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.VideoBackendService;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.PrivateFolderAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentGridLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PrivateFragment;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoader;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class PrivateFolder extends BaseActivity implements PrivateFolderAdapter.ContactsAdapterListener {
    RecyclerView recyclerView;
    PrivateFolderAdapter videoAdapter;
    VideoLoader videoLoader;
    private SearchView searchView;

    ArrayList<FileItem> videoItems = new ArrayList<>();
    ArrayList<File> fileList = new ArrayList<>();
    private Handler handler;
    public int resumeflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_folder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.music_recyclar_view);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(PrivateFolder.this, LinearLayoutManager.VERTICAL, false));
        //setLayoutManager(getCurrentOrientation());
        new decryption(PrivateFolder.this).execute();
        loadEveryThing();
        Toast.makeText(this, ""+videoItems.toString(), Toast.LENGTH_SHORT).show();
        videoAdapter = new PrivateFolderAdapter(PrivateFolder.this,videoItems );
        recyclerView.setAdapter(videoAdapter);
       // videoAdapter.updateData(videoItems);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(PrivateFolder.this, "dfgfkdc,", Toast.LENGTH_SHORT).show();
                videoAdapter.notifyDataSetChanged();
                //    videoAdapter = new PrivateFolderAdapter(MainActivity.this);
                // recyclerView.setAdapter(videoAdapter);
                //  loadEveryThing();
                //    videoAdapter.updateData(videoItems);
            }
        }, 5000);


    }

    @Override
    public void onContactSelected(FileItem contact) {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.getFileTitle() + ", " + contact.getPath(), Toast.LENGTH_LONG).show();


    }

    class Task implements Runnable {
        @Override
        public void run() {

            try {
                File src = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
                File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.VideoPlayer/");
                Decryptor.getDecrypter(true).decrypt(src, des);
                Thread.sleep(600);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private static class decryption extends AsyncTask<Void, Void, Void> {
        Activity context;
        File src = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.VideoPlayer/");

        decryption(Activity context1) {
            this.context = context1;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Decryptor.getDecrypter(true).decrypt(src, des);

            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //videoAdapter.notifyDataSetChanged();
        }
    }


    public void sortAZ() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortVideoAZ(videoItems);
            videoAdapter.updateData(videoItems);
        }


    }

    public void sortZA() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortVideoZA(videoItems);
            videoAdapter.updateData(videoItems);
        }
    }

    private ArrayList<FileItem> loadEveryThing(){

        final String[] projection = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.ALBUM
        };
        String selection = MediaStore.Video.Media.DATA + " like ? ";
        String[] selectionArgs =new String[] {Environment.getExternalStorageDirectory() + "/.VideoPlayer//"};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        ArrayList<FileItem> Items = new ArrayList<>(cursor.getCount());
        if (cursor.moveToLast()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                if (path == null) continue;

                String duration= convertDuration(cursor.getLong(cursor.getColumnIndex(projection[1])));
                // Log.d(TAG, "pick video from device duration = " + duration);

                String title = cursor.getString(cursor.getColumnIndex(projection[2]));

                String resolution = cursor.getString(cursor.getColumnIndex(projection[3]));
//                  String date_added = cursor.getString(cursor.getColumnIndex(projection[3]));
//                date_added = convertDate(date_added,"dd/MM/yyyy hh:mm:ss");
                File file = new File(path);

                if (file.exists()) {
                    long fileSize = file.length();
                    String folderName = "Unknow Folder";
                    File _parentFile = file.getParentFile();
                    if (_parentFile.exists()) {
                        folderName = _parentFile.getName();
                    }
                    Items.add(new FileItem(
                            title,
                            path,duration,
                            folderName,resolution,"", false));
                }

            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return Items;
    }
//    private void loadEveryThing() {
//        ArrayList<File> files = new ArrayList<>();
//        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.VideoPlayer/");
//        getfile(des);
//        for (int i = 0; i < fileList.size(); i++) {
//            FileItem fileItem = new FileItem();
//            fileItem.setPath(fileList.get(i).getAbsolutePath());
//            fileItem.setFileTitle(fileList.get(i).getName());
//            videoItems.add(fileItem);
//        }
   // }

    public static String convertDuration(long duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }


    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".mp3")
                            || listFile[i].getName().endsWith(".mp4")
                            || listFile[i].getName().endsWith(".m4a")
                    ) {
                        fileList.add(listFile[i]);
                    }
                }

            }
        }
        return fileList;
    }


    public void sortSize() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortSongSize();
            videoAdapter.updateData(videoItems);
        }
    }

    public void shareSelected() {
        if (videoAdapter == null || PrivateFolder.this == null) return;
        ArrayList<FileItem> videoItems = videoAdapter.getVideoItemsSelected();
        AppsUtils.shareMultiVideo(PrivateFolder.this, videoItems);
    }

    public void deleteSelected() {
        videoAdapter.deleteListVideoSelected();
    }

    public void updateVideoList(ArrayList<FileItem> videoItems) {
        if (videoItems == null) return;
        this.videoItems = videoItems;
        Mainutils.getInstance().folderItem.setVideoItems(videoItems);
        Mainutils.getInstance().isNeedRefreshFolder = true;
    }

    public void releaseUI() {
        for (FileItem videoItem : videoItems) {
            videoItem.setSelected(false);
        }
        videoAdapter.updateData(videoItems);
    }

    private void doLayoutChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PreferencesUtility.getInstance(PrivateFolder.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(PrivateFolder.this, 4));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(PrivateFolder.this, LinearLayoutManager.VERTICAL, false));
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (PreferencesUtility.getInstance(PrivateFolder.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(PrivateFolder.this, 2));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(PrivateFolder.this, LinearLayoutManager.VERTICAL, false));
            }
        }
        videoAdapter.updateData(videoItems);
    }

    private int getCurrentOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void setLayoutManager(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PreferencesUtility.getInstance(PrivateFolder.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(PrivateFolder.this, 4));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(PrivateFolder.this, LinearLayoutManager.VERTICAL, false));
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (PreferencesUtility.getInstance(PrivateFolder.this).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(PrivateFolder.this, 2));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(PrivateFolder.this, LinearLayoutManager.VERTICAL, false));
            }
        }
    }


//    private void loadEveryThing(){
//
//        videoItems = GlobalVar.getInstance().folderItem.getVideoItems();
//        videoAdapter.updateData(videoItems);
//
//    }

    private ArrayList<FileItem> sortVideoAZ(ArrayList<FileItem> videoItems) {
        ArrayList<FileItem> m_videos = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < videoItems.size(); i++) {
            names.add(videoItems.get(i).getFolderName() + "_" + videoItems.get(i).getPath());
        }
        Collections.sort(names, String::compareToIgnoreCase);

        for (int i = 0; i < names.size(); i++) {
            String path = names.get(i);
            for (int j = 0; j < videoItems.size(); j++) {
                if (path.equals(videoItems.get(j).getFolderName() + "_" + videoItems.get(j).getPath())) {
                    m_videos.add(videoItems.get(j));
                }
            }
        }


        return m_videos;
    }

    private ArrayList<FileItem> sortVideoZA(ArrayList<FileItem> videoItems) {
        ArrayList<FileItem> m_videos = sortVideoAZ(videoItems);
        Collections.reverse(m_videos);

        return m_videos;

    }

    private ArrayList<FileItem> sortSongSize() throws NumberFormatException {
        ArrayList<FileItem> m_videos = videoItems;
        for (int i = 0; i < m_videos.size() - 1; i++) {
            for (int j = 0; j < m_videos.size() - 1 - i; j++) {

                if (m_videos.get(j).getFileSizeAsFloat() < m_videos.get(j + 1).getFileSizeAsFloat()) {
                    Collections.swap(m_videos, j, j + 1);
                }
            }
        }

        return m_videos;

    }


    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {


            progress.dismiss();

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {

                progress.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }


            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            try {
                progress = ProgressDialog.show(PrivateFolder.this, null,
                        "Loading...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (resumeflag == 0) {
            Toast.makeText(this, "resume" + resumeflag, Toast.LENGTH_SHORT).show();

            resumeflag = 1;
        } else if (resumeflag == 1) {
            resumeflag = 0;

            startActivity(new Intent(PrivateFolder.this, PasswordCheck.class));
            finish();

            Toast.makeText(this, "resume" + resumeflag, Toast.LENGTH_SHORT).show();

        }


        reloadData();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.secound, menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secound, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
               // videoAdapter.getFilter().filter(query);
                filter(query.toString());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                filter(query.toString());
                return true;
            }
        });
        return true;
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<FileItem> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (FileItem s : videoItems) {
            //if the existing elements contains the search input
            if (s.getFolderName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        videoAdapter.filterList(filterdNames);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        Handler handler = new Handler();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

        }
        if (id == R.id.changepassword) {

            startActivity(new Intent(PrivateFolder.this, PinScreenClass.class));


        } else if (id == R.id.sort) {
            sortAZ();


        } else if (id == R.id.changesecuritycode) {


            startActivity(new Intent(PrivateFolder.this, Hiddencabinet.class));


        }
        return false;
    }

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);

    }


    private static class Encryption extends AsyncTask<Void, Void, Void> {
        Context context;
        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
        File src = new File(Environment.getExternalStorageDirectory().getPath() + "/.VideoPlayer/");

        Encryption(Context context1) {
            this.context = context1;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Encryptor.getEncrypter(true).encrypt(src, des);
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Encryption(PrivateFolder.this).execute();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
//        mMainutils.playingVideo=null;
//        mMainutils.videoItemsPlaylist=null;
//        mMainutils.videoBackendService.stopService(new Intent(PrivateFolder.this, VideoBackendService.class));
        startActivity(new Intent(PrivateFolder.this, MainClass.class));
        finish();
    }
}