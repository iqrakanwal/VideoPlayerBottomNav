package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Decryptor;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Encryptor;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.PrivateFolderAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentGridLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoader;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class PrivateFragment extends MyFragment {
    RecyclerView recyclerView;
    PrivateFolderAdapter videoAdapter;
    VideoLoader videoLoader;
    ArrayList<FileItem> videoItems = new ArrayList<>();
    ArrayList<File> fileList = new ArrayList<>();
    private Handler handler;

    public static PrivateFragment newInstance() {
        return new PrivateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.private_fragment, container, false);
        recyclerView = v.findViewById(R.id.music_recyclar_view);
        //   Timer timer = new Timer();
        // handler = new Handler();
//        new Thread(new Task()).start();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            videoAdapter.notifyDataSetChanged();
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//            }
//        };
        // timer.schedule(doAsynchronousTask, 0, 1000);
        new decryption(getActivity()).execute();
        setLayoutManager(getCurrentOrientation());
        videoAdapter = new PrivateFolderAdapter(getActivity(), videoItems);
        recyclerView.setAdapter(videoAdapter);
        loadEveryThing();
        videoAdapter.updateData(videoItems);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                videoAdapter.notifyDataSetChanged();

            }
        }, 4000);


        return v;
    }

    class Task implements Runnable {
        @Override
        public void run() {

            try {
                File src = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
                File des = new File(Environment.getExternalStorageDirectory().getPath() + "/VideoPlayer/");
                Decryptor.getDecrypter(true).decrypt(src, des);
                Thread.sleep(600);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private static class decryption extends AsyncTask<Void, Void, Void> {
        Context context;
        File src = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/VideoPlayer/");

        decryption(Context context1) {
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

    @Override
    public void reloadFragment(int orientation) {
        doLayoutChange(orientation);
    }


    @Override
    public void sortAZ() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortVideoAZ(videoItems);
            videoAdapter.updateData(videoItems);
        }


    }

    @Override
    public void sortZA() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortVideoZA(videoItems);
            videoAdapter.updateData(videoItems);
        }
    }


    private void loadEveryThing() {
        ArrayList<File> files = new ArrayList<>();
        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/VideoPlayer/");
        getfile(des);
        for (int i = 0; i < fileList.size(); i++) {
            FileItem fileItem = new FileItem();
            fileItem.setPath(fileList.get(i).getAbsolutePath());
            fileItem.setFileTitle(fileList.get(i).getName());
            videoItems.add(fileItem);
        }
    }

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




    @Override
    public void sortSize() {
        if (videoItems != null && videoItems.size() > 0) {
            videoItems = sortSongSize();
            videoAdapter.updateData(videoItems);
        }
    }

    @Override
    public void shareSelected() {
        if (videoAdapter == null || getActivity() == null) return;
        ArrayList<FileItem> videoItems = videoAdapter.getVideoItemsSelected();
        AppsUtils.shareMultiVideo(getActivity(), videoItems);
    }

    @Override
    public void deleteSelected() {
        videoAdapter.deleteListVideoSelected();
    }

    @Override
    public void updateVideoList(ArrayList<FileItem> videoItems) {
        if (videoItems == null) return;
        this.videoItems = videoItems;
        Mainutils.getInstance().folderItem.setVideoItems(videoItems);
        Mainutils.getInstance().isNeedRefreshFolder = true;
    }

    @Override
    public void releaseUI() {
        for (FileItem videoItem : videoItems) {
            videoItem.setSelected(false);
        }
        videoAdapter.updateData(videoItems);
    }

    private void doLayoutChange(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 4));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 2));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            }
        }
        videoAdapter.updateData(videoItems);
    }

    private int getCurrentOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void setLayoutManager(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 4));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 2));
            } else {
                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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

    @Override
    public void onPause() {
        super.onPause();

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
                progress = ProgressDialog.show(getContext(), null,
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

}
