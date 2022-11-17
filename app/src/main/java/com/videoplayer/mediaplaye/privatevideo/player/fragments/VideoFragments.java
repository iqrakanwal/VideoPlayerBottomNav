package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FileselectionAdaptor;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FileselectionAdaptorfors;
import com.videoplayer.mediaplaye.privatevideo.player.models.MusicSelection;

import java.util.ArrayList;
import java.util.List;

public class VideoFragments extends Fragment {
    RecyclerView recyclerView;
    List<MusicSelection> audioModels = new ArrayList<>();
    FileselectionAdaptorfors fileselectionAdaptor;
    public VideoFragments() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_video_fragments, container, false);

        recyclerView = view.findViewById(R.id.recyclarviewVidoe);


        String[] proj = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME , MediaStore.Video.Media.DATA , MediaStore.Video.Media.ARTIST};// Can include more data for more details and check it.

        Cursor audioCursor =getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
                null,
                null,
                null);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    String fullPath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String artist = audioCursor.getString(audioCursor.getColumnIndexOrThrow( MediaStore.Video.Media.ARTIST));

                    MusicSelection a = new MusicSelection(fullPath,false, artist);

                    audioModels.add(a);
                } while (audioCursor.moveToNext());
            }
        }
        audioCursor.close();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fileselectionAdaptor = new FileselectionAdaptorfors(getActivity(), audioModels);
        recyclerView.setAdapter(fileselectionAdaptor);


        return view;
    }

}
