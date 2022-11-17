package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class MainFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.video_check,
            R.drawable.music_check,
            R.drawable.playlist_check
    };

    private String[] tabname = {
            "Video", "Music", "Playlist"
    };
    AllFolderList speedDialFragment;
    FragmentFolderAudioList fragmentFolderAudioList;
    PlaylistFragment playlistFragment;
    FragmentVideoList fragmentVideoList;
    public MainFragment()
    {
        speedDialFragment= new AllFolderList();
        fragmentFolderAudioList= new FragmentFolderAudioList();
        playlistFragment= new PlaylistFragment();
     //   fragmentVideoList= new FragmentVideoList();


    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        viewPager =  v.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout =  v.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabView();
        return v;
    }
    public void setupTabView() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab);
            TextView tab_name = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab);
            tab_name.setText("" + tabname[i]);
            ImageView imageView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.imageview);
            imageView.setImageResource(tabIcons[i]);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        Toast.makeText(getContext(), "viewpager", Toast.LENGTH_SHORT).show();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(speedDialFragment, "Videos");
        adapter.addFrag(fragmentFolderAudioList, "Music");
        adapter.addFrag(playlistFragment, "Playlist");
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Videos");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_videos, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Music");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_music, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Playlist");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_playlist, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }
    public void change_view()
    {
        speedDialFragment.setLayoutManager();
        fragmentFolderAudioList.setLayoutManager();
    //    fragmentVideoList.setLayoutManager();
    }

    @Override
    public void onResume() {
        super.onResume();
       // setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabView();
       //setupTabView();
      //  tabLayout.setupWithViewPager(viewPager);
      //    setupViewPager(viewPager);
      //  setupTabIcons();
      //  MainFragment mainFragment= new MainFragment();
      //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mainFragment).commit();

        //setupViewPager(viewPager);
      //  setupTabIcons();
    }

//    private void setupTabIcons() {
//
//        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
//        tabOne.setText("music");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_playlist, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(tabOne);
//
//        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
//        tabTwo.setText("TWO");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_playlist, 0, 0);
//        tabLayout.getTabAt(1).setCustomView(tabTwo);
//
//        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
//        tabThree.setText("THREE");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.musicicon, 0, 0);
//        tabLayout.getTabAt(2).setCustomView(tabThree);
//    }

    public void mainframgnetnisuue(){

    //    speedDialFragment.changfeadaptor();


    }



}
