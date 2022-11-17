
package com.videoplayer.mediaplaye.privatevideo.player.activities;

import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;

import java.io.File;

public class Method {

    public static void load_Directory_Files(File directory) {
        File[] fileList = directory.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    load_Directory_Files(fileList[i]);
                } else {
                    String name = fileList[i].getName().toLowerCase();
                    String filepath = fileList[i].getPath();
                    String getparent = fileList[i].getParent();
                    String duaration = convertDuration(fileList[i].length());

                    for (String extension : Constant.videoExtensions) {
                        //check the type of file
                        if (name.endsWith(extension)) {
                            FileItem fileItem = new FileItem(name, filepath, duaration, getparent, "", "", false);
//                            fileItem.setFileTitle(name);
//                            fileItem.setPath(filepath);
//                            fileItem.set(duaration);
                            Constant.allMediaList.add(fileItem);
                            //when we found file
                            break;
                        }
                    }
                }
            }
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
}
