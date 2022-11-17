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


public class MusicFragment extends Fragment {

    RecyclerView recyclerView;
    private RecentsViewModel mViewModel;
    ArrayList<MusicSelection> audioModels = new ArrayList<>();
    FileselectionAdaptor fileselectionAdaptor;
    public MusicFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_music);


        String[] proj = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME , MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST};// Can include more data for more details and check it.

        Cursor audioCursor =getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
                null,
               null,
                null);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    String fullPath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String artist = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                    MusicSelection a = new MusicSelection(audioCursor.getString(audioIndex), false, artist);
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
        return view;
    }


}
