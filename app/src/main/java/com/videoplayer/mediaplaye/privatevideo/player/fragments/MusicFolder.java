package com.videoplayer.mediaplaye.privatevideo.player.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.AudioLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.AudioLoader;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapterAudio;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderLoader;

import java.io.File;
import java.util.ArrayList;

public class MusicFolder extends Fragment {
    RecyclerView recyclerView;
    FolderAdapterAudio folderRecyclarAdaptor;
    ArrayList<File> videoItems = new ArrayList<>();
    ArrayList<File> fileList = new ArrayList<>();
    ArrayList<FolderItem> folderItems;

    public MusicFolder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_music_folder, container, false);
        recyclerView = rootView.findViewById(R.id.foldermusic);
        setLayoutManager();
        folderRecyclarAdaptor = new FolderAdapterAudio(getActivity());
        recyclerView.setAdapter(folderRecyclarAdaptor);
        loadEveryThing();
        return rootView;
    }


    private void setLayoutManager() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
    private void loadEveryThing(){
        AudioLoader videoLoader = new AudioLoader(getActivity());
        videoLoader.loadDeviceAudios(new AudioLoadListener() {


            @Override
            public void onAudioLoaded(ArrayList<FileItem> videoItems) {
                Mainutils.getInstance().allfileItems=null;
                Mainutils.getInstance().allfileItems = videoItems;
                folderItems = FolderLoader.getFolderListVideo(videoItems);
                folderRecyclarAdaptor.updateData(folderItems);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onResume() {
        super.onResume();
        loadEveryThing();



    }

}
