package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import java.util.ArrayList;

public class VideosFragment extends Fragment {
    RecyclerView recyclerView;
    FolderAdapter folderAdapter;
    ArrayList<FolderItem> folderItems;
    public static VideosFragment newInstance() {

        return new VideosFragment();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.videos_fragment, container, false);

        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
