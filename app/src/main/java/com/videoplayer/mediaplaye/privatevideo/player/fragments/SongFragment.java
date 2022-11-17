package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FileselectionAdaptorfors;
import com.videoplayer.mediaplaye.privatevideo.player.models.MusicSelection;
import java.util.ArrayList;
import java.util.List;


public class SongFragment extends Fragment {
    RecyclerView recyclerView;
    List<MusicSelection> audioModels = new ArrayList<>();
    FileselectionAdaptorfors fileselectionAdaptor;
    RelativeLayout selectall;
    public SongFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_song, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_music);
        String[] proj = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME , MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST};// Can include more data for more details and check it.
        selectall= view.findViewById(R.id.selectalllayout);
        Cursor audioCursor =getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
                null,
                null,
                null);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    String fullPath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    MusicSelection a = new MusicSelection(fullPath, false,artist);

                    audioModels.add(a);
                } while (audioCursor.moveToNext());
            }
        }
        audioCursor.close();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fileselectionAdaptor = new FileselectionAdaptorfors(getActivity(), audioModels);
        recyclerView.setAdapter(fileselectionAdaptor);
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor audioCursor =getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
                        null,
                        null,
                        null);
                if (audioCursor != null) {
                    if (audioCursor.moveToFirst()) {
                        do {
                            int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                            String fullPath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                            String artist = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                            MusicSelection a = new MusicSelection(fullPath, true, artist);

                            audioModels.add(a);
                        } while (audioCursor.moveToNext());
                    }
                }
                audioCursor.close();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                fileselectionAdaptor = new FileselectionAdaptorfors(getActivity(), audioModels);
                recyclerView.setAdapter(fileselectionAdaptor);

            }
        });
        return view;
    }







}
