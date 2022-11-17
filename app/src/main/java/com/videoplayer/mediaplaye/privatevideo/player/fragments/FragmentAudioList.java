package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.AudioAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentGridLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoader;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentAudioList extends MyFragment{

    RecyclerView recyclerView;
    AudioAdapter videoAdapter;
    VideoLoader videoLoader;
    LinearLayout shuffleall;
    ArrayList<FileItem> videoItems = new ArrayList<>();
    public FragmentAudioList() {



    }

    public static FragmentAudioList newInstance(String param1, String param2) {
        FragmentAudioList fragment = new FragmentAudioList();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_list, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        shuffleall= rootView.findViewById(R.id.linear);

        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
            GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if (videoAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(gridManager);
        } else {
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }

       // videoAdapter = new AudioAdapter(getActivity());
        recyclerView.setAdapter(videoAdapter);
        videoItems = Mainutils.getInstance().folderItem.getVideoItems();
        videoAdapter.updateData(videoItems);


        shuffleall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffleArray();
            }
        });
        return rootView;
    }
    @Override
    public void reloadFragment(int orientation){
        doLayoutChange(orientation);
    }
    @Override
    public int getTotalSelected(){
        if(videoAdapter == null)
            return 0;
        return videoAdapter.getTotalVideoSelected();
    }
    public void shuffleArray(){

        Collections.shuffle(videoItems);
        videoAdapter.updateData(videoItems);
    }
    @Override
    public void playItemSelected(){
        ArrayList<FileItem> videoItems = videoAdapter.getVideoItemsSelected();
        if(videoItems.size() > 0 && getActivity() != null){
            Mainutils.getInstance().videoItemsPlaylist = videoItems;
            Mainutils.getInstance().playingVideo = videoItems.get(0);
            if(!Mainutils.getInstance().isPlayingAsPopup()){
                Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition,false);
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                getActivity().startActivity(intent);
                if(Mainutils.getInstance().videoBackendService != null)
                    Mainutils.getInstance().videoBackendService.releasePlayerView();
            }else {
                ((BaseActivity) getActivity()).showFloatingView(getActivity(),true);
            }
        }
    }
    @Override
    public void sortAZ(){
        if(videoItems != null && videoItems.size() > 0){
            videoItems = sortVideoAZ(videoItems);
            videoAdapter.updateData(videoItems);
        }


    }

    @Override
    public void sortZA(){
        if(videoItems != null && videoItems.size() > 0){
            videoItems = sortVideoZA(videoItems);
            videoAdapter.updateData(videoItems);
        }
    }

    @Override
    public void sortSize(){
        if(videoItems != null && videoItems.size() > 0){
            videoItems = sortSongSize();
            videoAdapter.updateData(videoItems);
        }
    }
    @Override
    public void shareSelected(){
        if(videoAdapter == null || getActivity() == null) return;
        ArrayList<FileItem> videoItems = videoAdapter.getVideoItemsSelected();
        AppsUtils.shareMultiVideo(getActivity(),videoItems);
    }

    @Override
    public void updateVideoList(ArrayList<FileItem> videoItems){
        if(videoItems == null) return;
        this.videoItems = videoItems;
        Mainutils.getInstance().folderItem.setVideoItems(videoItems);
        Mainutils.getInstance().isNeedRefreshFolder = true;
    }
    @Override
    public void releaseUI(){
        for(FileItem videoItem:videoItems){
            videoItem.setSelected(false);
        }
        videoAdapter.updateData(videoItems);
    }
    private void doLayoutChange(int orientation){


            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                    recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 4));
                } else {
                    recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                    recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 2));
                } else {
                    recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            }
            videoAdapter.updateData(videoItems);
        }
    public void setLayoutManager() {


        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {


            GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if (videoAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(gridManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();

        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    private int getCurrentOrientation(){
        return getResources().getConfiguration().orientation;
    }
    private void setLayoutManager(int orientation) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                    recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 4));
                } else {
                    recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
                    recyclerView.setLayoutManager(new WrapContentGridLayoutManager(getActivity(), 2));
                } else {
                    recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            }
        }


//    private void loadEveryThing(){
//        Fragment f=getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentvideo);
//        if (f instanceof FragmentAudioList) {
//            videoItems = Mainutils.getInstance().folderItem.getVideoItems();
//            videoAdapter.updateData(videoItems);
//
//                }
//
//
//       else if(f instanceof MusicFolder){
//
//               videoLoader = new VideoLoader(getActivity());
//               videoLoader.loadDeviceVideos(new VideoLoadListener() {
//@Override
//public void onVideoLoaded(final ArrayList<FileItem> items) {
//    videoItems = items;
//    Mainutils.getInstance().allfileItems = videoItems;
//    videoAdapter.updateData(items);
//}
//    public void onFailed(Exception e){
//        e.printStackTrace();
//    }
//});
//
//
//        }
//    }

    private ArrayList<FileItem> sortVideoAZ(ArrayList<FileItem> videoItems){
        ArrayList<FileItem> m_videos = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < videoItems.size();i++){
            names.add(videoItems.get(i).getFolderName() + "_" + videoItems.get(i).getPath());
        }
        Collections.sort(names, String::compareToIgnoreCase);

        for(int i = 0; i < names.size(); i ++){
            String path = names.get(i);
            for (int j = 0; j < videoItems.size();j++){
                if(path.equals(videoItems.get(j).getFolderName() + "_" + videoItems.get(j).getPath())){
                    m_videos.add(videoItems.get(j));
                }
            }
        }


        return m_videos;
    }
    private ArrayList<FileItem> sortVideoZA(ArrayList<FileItem> videoItems){
        ArrayList<FileItem> m_videos = sortVideoAZ(videoItems);
        Collections.reverse(m_videos);

        return m_videos;

    }

    private ArrayList<FileItem> sortSongSize() throws NumberFormatException{
        ArrayList<FileItem> m_videos = videoItems;
        for (int i = 0; i < m_videos.size() -1;i++) {
            for(int j = 0; j < m_videos.size() - 1 - i; j++){

                if(m_videos.get(j).getFileSizeAsFloat() < m_videos.get(j+1).getFileSizeAsFloat()){
                    Collections.swap(m_videos,j,j+1);
                }
            }
        }

        return m_videos;

    }
}
