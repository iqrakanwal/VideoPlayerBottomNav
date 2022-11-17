package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.AudioAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentGridLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.musiocoptionlistener;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

public class RecentsFragment extends MyFragment implements musiocoptionlistener {

    RecyclerView recyclerView;
    AudioAdapter videoAdapter;
    LinearLayout frameLayout;
    boolean check=false;
    LinearLayout shuffleall;

    Inflater inflater;
    ArrayList<FileItem> videoItems = new ArrayList<>();
    public static RecentsFragment newInstance() {
        return new RecentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflater=inflater;
        View v = inflater.inflate(R.layout.recents_fragment, container, false);
        recyclerView = v.findViewById(R.id.music_recyclar_view);
        shuffleall= v.findViewById(R.id.linear);

        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()){
          //  recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


            GridLayoutManager gridManager=new GridLayoutManager(getActivity(),3);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position)
                {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if(videoAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(gridManager);



        } else {
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        frameLayout= (LinearLayout) v.findViewById(R.id.banner_container);
        //videoAdapter = new AudioAdapter(getActivity());
        recyclerView.setAdapter(videoAdapter);
        videoItems= loadEveryThing();
        videoAdapter.updateData(videoItems);
        shuffleall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffleArray();
            }
        });
        return v;
    }
    public void shuffleArray(){

        Collections.shuffle(videoItems);
        videoAdapter.updateData(videoItems);
    }



  /*  public void setLayoutManager() {


        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();

        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
*/


    public void setLayoutManager() {


        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
          //  recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


            GridLayoutManager gridManager=new GridLayoutManager(getActivity(),3);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position)
                {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if(videoAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(gridManager);
        }
        else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();

        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

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
//    public void change_view()
//    {
//        speedDialFragment.setLayoutManager();
//    }
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
    if(inflater!=null){
    if(!DataProvider.getbuy()&&check==false)
     {
        UnifiedNativeAd native_admob= DataProvider.getInstance().get_native_admob();
        if(native_admob!=null)
        {

            UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                    .inflate(R.layout.native_back_button, null);
            populateUnifiedNativeAdView(native_admob, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);

            DataProvider.getInstance().load_native_admob();
            check=true;

        }

    }


}


        }

    }


    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.

                super.onVideoEnd();
            }
        });

        com.google.android.gms.ads.formats.MediaView mediaView = (com.google.android.gms.ads.formats.MediaView) adView.findViewById(R.id.ad_media);
        ImageView mainImageView = (ImageView) adView.findViewById(R.id.ad_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);

        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            try
            {
                List<NativeAd.Image> images = nativeAd.getImages();
                if(images.size()>0)
                    mainImageView.setImageDrawable(images.get(0).getDrawable());

            }catch (Exception e)
            {

            }
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        ((Button) adView.getCallToActionView()).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
    @Override
    public void sortZA(){
        if(videoItems != null && videoItems.size() > 0){
            videoItems = sortVideoZA(videoItems);
            videoAdapter.updateData(videoItems);
        }
    }
    private ArrayList<FileItem> loadEveryThing(){

        final String[] projection = new String[]{
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM
        };

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<FileItem> Items = new ArrayList<>(cursor.getCount());
        if (cursor.moveToLast()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                if (path == null) continue;

                String duration= convertDuration(cursor.getLong(cursor.getColumnIndex(projection[1])));
                // Log.d(TAG, "pick video from device duration = " + duration);

                String title = cursor.getString(cursor.getColumnIndex(projection[2]));

                String resolution = cursor.getString(cursor.getColumnIndex(projection[3]));
//                  String date_added = cursor.getString(cursor.getColumnIndex(projection[3]));
//                date_added = convertDate(date_added,"dd/MM/yyyy hh:mm:ss");
                File file = new File(path);

                if (file.exists()) {
                    long fileSize = file.length();
                    String folderName = "Unknow Folder";
                    File _parentFile = file.getParentFile();
                    if (_parentFile.exists()) {
                        folderName = _parentFile.getName();
                    }
                    Items.add(new FileItem(
                            title,
                            path,duration,
                            folderName,resolution,"", false));
                }

            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return Items;
    }
    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static String convertDuration(long duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

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
//
//        videoItems = GlobalVar.getInstance().folderItem.getVideoItems();
//        videoAdapter.updateData(videoItems);
//
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

    @Override
    public void addtoqueue(String url) {

    }

    @Override
    public void playnext(String url) {

    }

    @Override
    public void addtoplaylist(String url) {

    }

    @Override
    public void edti(String url) {

    }

    @Override
    public void delete(String url) {

    }

    @Override
    public void share(String url) {

    }

    @Override
    public void properties(String url) {

    }
}