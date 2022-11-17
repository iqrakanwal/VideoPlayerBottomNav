package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.ViewPagerAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    TabLayout tabLayout;
    private ViewPager viewPager;
    public static ArrayList<Songs> arrayLis= new ArrayList<>();
    public FavoriteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_favorite, container, false);
        tabLayout= v.findViewById(R.id.tablayoutselection);
        viewPager= v.findViewById(R.id.viewpagerselection);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        return v;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new SongFragment(), "Music");
        adapter.addFrag(new VideoFragments(), "Videos");
        viewPager.setAdapter(adapter);
    }
}