package com.videoplayer.mediaplaye.privatevideo.player.listeners;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

import com.videoplayer.mediaplaye.privatevideo.player.AudioLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Constant;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Method;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import com.videoplayer.mediaplaye.privatevideo.player.utills.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoLoader {

    private final static String TAG = "VideoLoader";

    private final Context context;
    private ExecutorService executorService;

    public VideoLoader(Context context) {
        this.context = context;
    }

    public void loadDeviceVideos(final VideoLoadListener listener) {
        getExecutorService().execute(new VideoLoadRunnable(listener, context));
    }

    public void loadDeviceAudio(final AudioLoadListener listener) {
        getExecutorService().execute(new AudioLoadRunnable(listener, context));
    }

    public void abortLoadVideos() {
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


    private static class VideoLoadRunnable implements Runnable {

        private final VideoLoadListener listener;
        private final Context context;
        private final Handler handler = new Handler(Looper.getMainLooper());

        private final String[] projection = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_TAKEN
        };


        public VideoLoadRunnable(VideoLoadListener listener, Context context) {
            this.listener = listener;
            this.context = context;
        }

        @Override
        public void run() {
            String selection = MediaStore.Video.Media.DATA + " like?";
            String[] selectionArgs = new String[]{"%0%"};
//            videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                    parameters, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");

            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, selectionArgs, null);


            final ArrayList<FileItem> videoItems = new ArrayList<>(cursor.getCount());

            if (cursor.moveToLast()) {
                do {
                    String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                    if (path == null) continue;

                    Log.d(TAG, "pick video from device path = " + path);


                    //  String duration = AppsUtils.makeShortTimeString(context,cursor.getLong(cursor.getColumnIndex(projection[1]))/1000);
                    String duration = convertDuration(cursor.getLong(cursor.getColumnIndex(projection[1])));

                    Log.d(TAG, "pick video from device duration = " + duration);

                    String title = cursor.getString(cursor.getColumnIndex(projection[2]));

                    String resolution = cursor.getString(cursor.getColumnIndex(projection[3]));
                    String date_added = cursor.getString(cursor.getColumnIndex(projection[4]));
                 //   date_added = convertDate(date_added, "dd/MM/yyyy hh:mm:ss");
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
                                folderName, resolution, "date_added",false));
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onVideoLoaded(videoItems);
                }
            });
//            final ArrayList<FileItem> videoItems = new ArrayList<>();
//            File des = new File(Environment.getExternalStorageDirectory().getPath() + "/");
//            ArrayList<File> files=getfile(des);
//
//            for(int i=0 ; i <files.size(); i++){
//
//                FileItem fileItem= new FileItem(files.get(i).getAbsolutePath(),""+files.get(i).length(), files.get(i).getName(),files.get(i).getParent(), "","");
//                videoItems.add(fileItem);
//
//            }
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(uriOfFile);
//            long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
//            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//            retriever.release();
//            ContentResolver contentResolver = context.getContentResolver();
//            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//
//            Cursor cursor = contentResolver.query(uri, null, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
//
//                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
//                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
//                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//                    File file = new File(data);
//
//                    if (file.exists()) {
//                        long fileSize = file.length();
//                        String folderName = "Unknow Folder";
//                        File _parentFile = file.getParentFile();
//                        if (_parentFile.exists()) {
//                            folderName = _parentFile.getName();
//                        }
//                        videoItems.add(new FileItem(
//                                title, data, duration, folderName, "", ""));
//                    }
//                } while (cursor.moveToNext());
//            }

//            String[] storagePaths;
//            File storage;
//
//            storagePaths = StorageUtil.getStorageDirectories(context);
//
//            for (String path : storagePaths) {
//                storage = new File(path);
//                Method.load_Directory_Files(storage);
//            }
//


            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onVideoLoaded(videoItems);
                }
            });
        }

        public ArrayList<File> getfile(File dir) {
            ArrayList<File> fileList = new ArrayList<>();
            File listFile[] = dir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {

                    if (listFile[i].isDirectory()) {
                        getfile(listFile[i]);

                    }
//                ".mp4",".ts",".mkv",".mov",
//                        ".3gp",".mv2",".m4v",".webm",".mpeg1",".mpeg2",".mts",".ogm",
//                        ".bup", ".dv",".flv",".m1v",".m2ts",".mpeg4",".vlc",".3g2",
//                        ".avi",".mpeg",".mpg",".wmv",".asf"
                    else {
                        if (listFile[i].getName().endsWith(".mp4")

                        ) {
                            fileList.add(listFile[i]);
                        }
                    }

                }
            }
            return fileList;
        }
    }


    private static class AudioLoadRunnable implements Runnable {

        private final AudioLoadListener listener;
        private final Context context;
        private final Handler handler = new Handler(Looper.getMainLooper());

        String[] projection = {MediaStore.Audio.AudioColumns.DATA,

                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.ArtistColumns.ARTIST,};


        public AudioLoadRunnable(AudioLoadListener listener, Context context) {
            this.listener = listener;
            this.context = context;
        }

        @Override
        public void run() {
            String selection =MediaStore.Audio.Media.IS_MUSIC+ " !=0";
            //String[] selectionArgs = new String[]{"%%"};
//            videocursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    parameters, selection, selectionArgs, MediaStore.Audio.Media.DATE_TAKEN + " DESC");
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%0%"}, null);
            final ArrayList<FileItem> videoItems = new ArrayList<>(cursor.getCount());
            if (cursor.moveToLast()) {
                do {
                 //   String songtitle = cursor.getString(0);
               ////     int songArtist = cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ARTIST);
//                    if (path == null) continue;
//
//                    Log.d(TAG, "pick Audio from device path = " + path);
//

                    //        //  String duration = AppsUtils.makeShortTimeString(context,cursor.getLong(cursor.getColumnIndex(projection[1]))/1000);
                    String duration = convertDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
                 //   String currentTitle = cursor.getString(songtitle);
                  //  Log.d(TAG, "pick Audio from device duration = " + duration);

                    //      String title = cursor.getString(cursor.getColumnIndex(projection[2]));

//                    String resolution = cursor.getString(cursor.getColumnIndex(projection[3]));
//                    String date_added = cursor.getString(cursor.getColumnIndex(projection[4]));
//                    date_added = convertDate(date_added,"dd/MM/yyyy hh:mm:ss");

                    String path = cursor.getString(0);   // Retrieve path.
                    String name = cursor.getString(1);
                //    String album = cursor.getString(4);  // Retrieve album name.
                    String artist = cursor.getString(3); // Retrieve artist name.
                    // String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    // String duration = convertDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));

                    File file = new File(path);

                    if (file.exists()) {
                        long fileSize = file.length();
                        String folderName = "Unknow Folder";
                        File _parentFile = file.getParentFile();
                        if (_parentFile.exists()) {
                            folderName = _parentFile.getName();
                        }
                        videoItems.add(new FileItem(
                                name,
                                path,
                                duration,
                                folderName, "", "",false));
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


            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onAudioLoaded(videoItems);
                }
            });

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

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

}
