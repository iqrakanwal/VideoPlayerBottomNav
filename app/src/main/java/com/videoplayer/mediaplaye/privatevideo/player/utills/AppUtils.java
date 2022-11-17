package com.videoplayer.mediaplaye.privatevideo.player.utills;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;

import java.io.File;

public class AppUtils {


    public static Intent shareVideo(final Context context, FileItem videoItem) {


        if (videoItem == null) return new Intent();

        String filePath = videoItem.getPath();
        if (filePath == null) return new Intent();
        File shareFile = new File(filePath);
        if (!shareFile.exists()) return new Intent();
        String fileType = getFileExtension(filePath);

        if (shareFile.exists()) {
            try {
                if(fileType.equals("Mp4") || filePath.equals("mp4") || filePath.equals("MP4"))
                    return new Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, VideoPlayerProvider.getUri(context, shareFile))
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .setType("video/mp4");
                else {
                    return new Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, VideoPlayerProvider.getUri(context, shareFile))
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .setType("file/*");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                return new Intent();
            }
        }
        //Toast.makeText(context, "File cannot share, (:", Toast.LENGTH_SHORT).show();
        return new Intent();
    }
    public static String getFileExtension(String fileName) {
        String fileNameArray[] = fileName.split("\\.");
        return fileNameArray[fileNameArray.length-1];


    }
}
