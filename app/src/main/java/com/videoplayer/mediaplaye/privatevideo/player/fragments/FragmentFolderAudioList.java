package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.AudioLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.AudioLoader;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.AudioFolderAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoader;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderLoader;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentFolderAudioList extends MyFragment {

    RecyclerView recyclerView;
    AudioFolderAdapter folderAdapter;
    ArrayList<FolderItem> folderItems;
    public FragmentFolderAudioList() {

    }

    public static FragmentFolderAudioList newInstance() {
        FragmentFolderAudioList fragment = new FragmentFolderAudioList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_folder_list_audio, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewaudiofoler);
        folderItems=new ArrayList<>();

            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        folderAdapter = new AudioFolderAdapter(getActivity());
        recyclerView.setAdapter(folderAdapter);
      //  setLayoutManager();
       // recyclerView.setAdapter(folderAdapter);

        loadEveryThing();
       // Toast.makeText(getContext(), ""+folderItems.size(), Toast.LENGTH_SHORT).show();
        return rootView;
    }


//    public void setLayoutManager() {
//
//
//        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
//            GridLayoutManager gridManager=new GridLayoutManager(getActivity(),3);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position)
//                {
//                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
//                    if(folderAdapter.is_add(position))
//                        return 3;
//                    else
//                        return 1;
//                }
//            });
//            recyclerView.setLayoutManager(gridManager);        } else {
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        }
//        recyclerView.setAdapter(folderAdapter);
//        folderAdapter.notifyDataSetChanged();
//
//        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//
//    }
//    private void setLayoutManager() {
//        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//
//    }

    public void setLayoutManager() {


            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    private void loadEveryThing() {
        VideoLoader audioLoader = new VideoLoader(getActivity());
        audioLoader.loadDeviceAudio(new AudioLoadListener() {
            @Override
            public void onAudioLoaded(final ArrayList<FileItem> items) {
             //   Toast.makeText(getContext(), ""+items.toString(), Toast.LENGTH_SHORT).show();
                Mainutils.getInstance().allfileItems = items;
                folderItems = FolderLoader.getFolderList(items, true);
                folderAdapter.updateData(folderItems);

            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });

    }
    @Override
    public void sortAZ(){
        if(folderItems != null && folderItems.size() > 0){
            folderItems = sortFolderAZ(folderItems);
            folderAdapter.updateData(folderItems);
        }

    }
    @Override
    public void sortZA(){
        if(folderItems != null && folderItems.size() > 0){
            folderItems = sortFolderZA(folderItems);
            folderAdapter.updateData(folderItems);
        }

    }
    @Override
    public void sortSize(){
        if(folderItems != null && folderItems.size() > 0){
            folderItems = sortFolderNumberSong();
            folderAdapter.updateData(folderItems);
        }

    }
    private ArrayList<FolderItem> sortFolderAZ(ArrayList<FolderItem> folders){
        ArrayList<FolderItem> m_folders = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < folders.size();i++){
            names.add(folders.get(i).getFolderName() + "_" + folders.get(i).getFolderPath());
        }
        Collections.sort(names, String::compareToIgnoreCase);

        for(int i = 0; i < names.size(); i ++){
            String path = names.get(i);
            for (int j = 0; j < folders.size();j++){
                if(path.equals(folders.get(j).getFolderName() + "_" + folders.get(j).getFolderPath())){
                    m_folders.add(folders.get(j));
                }
            }
        }


        return m_folders;
    }
    private ArrayList<FolderItem> sortFolderZA(ArrayList<FolderItem> folders){
        ArrayList<FolderItem> m_folders = sortFolderAZ(folders);
        Collections.reverse(m_folders);

        return m_folders;

    }

    private ArrayList<FolderItem> sortFolderNumberSong(){
        ArrayList<FolderItem> m_folders = folderItems;
        for (int i = 0; i < m_folders.size() -1;i++) {
            for(int j = 0; j < m_folders.size() - 1 - i; j++){
                if(m_folders.get(j).getVideoItems().size() < m_folders.get(j+1).getVideoItems().size()){
                    Collections.swap(m_folders,j,j+1);
                }
            }
        }

        return m_folders;

    }

    @Override
    public void onResume() {
        super.onResume();
      //  folderAdapter.notifyDataSetChanged();

    }
}
