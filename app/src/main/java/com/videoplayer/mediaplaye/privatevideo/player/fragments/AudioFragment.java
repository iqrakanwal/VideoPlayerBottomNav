package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FileselectionAdaptor;
import com.videoplayer.mediaplaye.privatevideo.player.models.MusicSelection;

import java.util.ArrayList;

public class AudioFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<MusicSelection> audioModels = new ArrayList<>();
    FileselectionAdaptor fileselectionAdaptor;
    public AudioFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_audio, container, false);
        recyclerView = v.findViewById(R.id.recyclarviewVidoe);
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME ,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.ARTIST };// Can include more data for more details and check it.
        Cursor audioCursor =getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
                null,
                null,
                null);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                    String fullPath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String artist = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));

                    MusicSelection a = new MusicSelection(audioCursor.getString(audioIndex) , false,artist );
a.setSelected(false);
                    audioModels.add(a);
                } while (audioCursor.moveToNext());
            }
        }
        audioCursor.close();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fileselectionAdaptor = new FileselectionAdaptor(getActivity(), audioModels);
        recyclerView.setAdapter(fileselectionAdaptor);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(fileselectionAdaptor);

    }
}
