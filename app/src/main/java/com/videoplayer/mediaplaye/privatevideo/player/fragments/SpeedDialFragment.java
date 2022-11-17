package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapterVideo;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoader;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderLoader;

import java.util.ArrayList;

public class SpeedDialFragment extends MyFragment  {
    RecyclerView recyclerView;
    FolderAdapterVideo folderRecyclarAdaptor;
    ArrayList<FolderItem> folderItems;
    public static SpeedDialFragment newInstance() {
        return new SpeedDialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.speed_dial_fragment, container, false);
        recyclerView = v.findViewById(R.id.recylarview);
        setLayoutManager();
        folderRecyclarAdaptor = new FolderAdapterVideo(getActivity());
        recyclerView.setAdapter(folderRecyclarAdaptor);
       // loadEveryThing();
        return v;
    }




    private void setLayoutManager() {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

//    private void loadEveryThing(){
//        VideoLoader videoLoader = new VideoLoader(getActivity());
//        videoLoader.loadDeviceVideos(new VideoLoadListener() {
//            @Override
//            public void onVideoLoaded(ArrayList<FileItem> videoItems) {
//                Mainutils.getInstance().allfileItems = videoItems;
//                folderItems = FolderLoader.getFolderListVideo(videoItems);
//                folderRecyclarAdaptor.updateData(folderItems);
//
//
//            }
//
//            public void onFailed(Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onResume() {
        super.onResume();
      //  loadEveryThing();



    }





}