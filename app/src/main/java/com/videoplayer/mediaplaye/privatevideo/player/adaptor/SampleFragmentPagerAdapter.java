package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.videoplayer.mediaplaye.privatevideo.player.R;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

Context context;
    public SampleFragmentPagerAdapter(FragmentManager manager, Context context1) {
        super(manager);
context = context1;
        manager.popBackStack();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    private String tabTitles[] = new String[] { "Tab1", "Tab2" , "Tab3" };
    private int[] imageResId = { R.drawable.musicicon, R.drawable.musicicon,R.drawable.musicicon };

//    public View getTabView(int position) {
//        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        TextView tv = (TextView) v.findViewById(R.id.textview);
//        tv.setText(tabTitles[position]);
//        ImageView img = (ImageView) v.findViewById(R.id.imageview);
//        img.setImageResource(imageResId[position]);
//        return v;
//    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
