package com.videoplayer.mediaplaye.privatevideo.player.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;


public class MainMusicFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //RecentsFragment recentsFragment;
    FragmentFolderAudioList fragmentFolderAudioList;



    private int[] tabIcons = {
            R.drawable.musicicon,
    };
    public MainMusicFragment() {
       // recentsFragment= new RecentsFragment();
        fragmentFolderAudioList= new FragmentFolderAudioList();
    }
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        tabLayout = view.findViewById(R.id.tabmusic);
//        viewPager = view.findViewById(R.id.view_pagermusic);
//        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//     //   setupViewPager(viewPager);
//      //  tabLayout.setupWithViewPager(viewPager);
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_music, container, false);
        tabLayout = v.findViewById(R.id.tabmusic);
        viewPager = v.findViewById(R.id.view_pagermusic);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return v;


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
     //   adapter.addFrag(recentsFragment, "Songs");
        adapter.addFrag(fragmentFolderAudioList, "Folders");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            }



public void loadeverything(){

    tabLayout.setupWithViewPager(viewPager);
    viewPager.getCurrentItem();


}

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.setupWithViewPager(viewPager);
      //  Toast.makeText(getContext(), "ghgtr", Toast.LENGTH_SHORT).show();
    }

    public void setLayoutManager() {


     // recentsFragment.setLayoutManager();
      fragmentFolderAudioList.setLayoutManager();
        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
}