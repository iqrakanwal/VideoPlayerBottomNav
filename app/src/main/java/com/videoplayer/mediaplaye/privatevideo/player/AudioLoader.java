package com.videoplayer.mediaplaye.privatevideo.player;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AudioLoader {

    private final static String TAG = "AudioLoader";
    private final Context context;
    private ExecutorService executorService;
    public AudioLoader(Context context) {
        this.context = context;
    }
    public void loadDeviceAudios(final AudioLoadListener listener) {
        getExecutorService().execute(new AudioLoadRunnable(listener, context));
    }

    public void abortLoadAudios() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    private static class AudioLoadRunnable implements Runnable {
        private final AudioLoadListener listener;
        private final Context context;
        private final Handler handler = new Handler(Looper.getMainLooper());
        private final String[] projection = new String[]{
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                        };
        public AudioLoadRunnable(AudioLoadListener listener, Context context) {
            this.listener = listener;
            this.context = context;
        }

        @Override
        public void run() {
            String selection=MediaStore.Video.Media.DATA +" like?";
            String[] selectionArgs=new String[]{"%0%"};
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                    selection, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (cursor == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailed(new NullPointerException());
                    }
                });
                return;
            }

            final ArrayList<FileItem> videoItems = new ArrayList<>(cursor.getCount());
            if (cursor.moveToLast()) {
                do {
                    String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                    if (path == null) continue;
                    Log.d(TAG, "pick video from device path = " + path);
                    String duration = AppsUtils.makeShortTimeString(context,cursor.getLong(cursor.getColumnIndex(projection[1]))/1000);
                   // Log.d(TAG, "pick video from device duration = " + duration);
                    String title = cursor.getString(cursor.getColumnIndex(projection[2]));
//                    String resolution = cursor.getString(cursor.getColumnIndex(projection[3]));
//                   String date_added = cursor.getString(cursor.getColumnIndex(projection[4]));
//                    date_added = convertDate(date_added,"dd/MM/yyyy hh:mm:ss");
                    File file = new File(path);
                    if (file.exists()) {
                        long fileSize = file.length();
                        String folderName = "Unknow Folder";
                        File _parentFile = file.getParentFile();
                        if (_parentFile.exists()) {
                            folderName = _parentFile.getName();
                        }
                        videoItems.add(new FileItem(
                                title,
                                path,
                                duration,
                                folderName,"", "", false));
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onAudioLoaded(videoItems);
                }
            });
        }
    }
    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}
