package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FavoriteActivity;
import com.videoplayer.mediaplaye.privatevideo.player.activities.GetFavorite;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Playlistfiles;
import com.videoplayer.mediaplaye.privatevideo.player.activities.SelectVideoAudio;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.PlaylistRecyclarView;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.Playlistitem;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class PlaylistFragment extends Fragment implements Playlistitem {
    ImageView add;
    PlaylistDatabase playlistDatabase;
    RecyclerView recyclerView;
    List<Playlist> allList;
    LinearLayout frameLayout;
    boolean check = false;
    Inflater inflater;
    PlaylistRecyclarView playlistRecyclarView;
    RelativeLayout linearLayout;
    PlaylistListViewModel playlistListViewModel;
    TextView numoffavirotesongs;

    public PlaylistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = inflater;
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlistDatabase = PlaylistDatabase.getInstance(getContext());
        playlistListViewModel = ViewModelProviders.of(getActivity()).get(PlaylistListViewModel.class);
        recyclerView = v.findViewById(R.id.recylarforplaylist);
        frameLayout = (LinearLayout) v.findViewById(R.id.banner_container);
        linearLayout = v.findViewById(R.id.favirote);
        numoffavirotesongs = v.findViewById(R.id.numofsongs);
        allList = new ArrayList<Playlist>();
        allList = playlistDatabase.getplaylist().getAll();
        ArrayList<Playlist> arrlistofOptions = new ArrayList<Playlist>(allList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        playlistRecyclarView = new PlaylistRecyclarView(getActivity(), arrlistofOptions, this);
        recyclerView.setAdapter(playlistRecyclarView);
        playlistDatabase = PlaylistDatabase.getInstance(getContext());
        numoffavirotesongs.setText("Songs " + playlistDatabase.getFavorite().getAll().size());
        add = v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogClass customDialogClass= new CustomDialogClass(getActivity());
                customDialogClass.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialogClass.show();




//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
//                alertDialogBuilder.setTitle("PlaylistName");
//                final EditText input = new EditText(getContext());
//                input.setTextColor(Color.BLACK);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                input.setLayoutParams(lp);
//                alertDialogBuilder.setView(input);
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("add", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                Playlist playlist = new Playlist(input.getText().toString());
//                                playlistDatabase.getplaylist().insert(playlist);
//                                Toast.makeText(getContext(), "inserted", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getActivity(), SelectVideoAudio.class);
//                                intent.putExtra("playlistname", input.getText().toString());
//                                startActivity(intent);
//                                recyclerView.setAdapter(playlistRecyclarView);
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GetFavorite.class));
            }
        });
        return v;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//
//if(inflater!=null){
//
//    if(!DataProvider.getbuy()&&check==false)
//    {
//        UnifiedNativeAd native_admob= DataProvider.getInstance().get_native_admob();
//        if(native_admob!=null)
//        {
//
//            UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
//                    .inflate(R.layout.native_back_button, null);
//            populateUnifiedNativeAdView(native_admob, adView);
//            frameLayout.removeAllViews();
//            frameLayout.addView(adView);
//
//            DataProvider.getInstance().load_native_admob();
//            check=true;
//        }
//
//
//    }
//}
//
//        }
//
//    }
//

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

            try {
                List<NativeAd.Image> images = nativeAd.getImages();
                if (images.size() > 0)
                    mainImageView.setImageDrawable(images.get(0).getDrawable());

            } catch (Exception e) {

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
    public void getplaylistitem(String url) {
        Toast.makeText(getContext(), "" + url, Toast.LENGTH_SHORT).show();
        //  playlistListViewModel.getidbytext(url);
        // Toast.makeText(getContext(), ""+ playlistListViewModel.getidbytext(url), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), Playlistfiles.class);
        i.putExtra("playlistnameforplaylist", url);

        startActivity(i);


    }

    @Override
    public void onResume() {
        super.onResume();
        numoffavirotesongs.setText("Songs " + playlistDatabase.getFavorite().getAll().size());


    }
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes, no;
        EditText editText;

        public CustomDialogClass(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_playlist_dialog);
            yes =  findViewById(R.id.ok);
            no =  findViewById(R.id.cancel);
            editText = findViewById(R.id.edittext);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ok:
                    if (editText.getText() != null) {

                        Playlist playlist = new Playlist(editText.getText().toString());
                                playlistDatabase.getplaylist().insert(playlist);
                                Toast.makeText(getContext(), "inserted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), SelectVideoAudio.class);
                                intent.putExtra("playlistname", editText.getText().toString());
                                startActivity(intent);

                    } else {
                        Toast.makeText(c, "Enter Name", Toast.LENGTH_SHORT).show();

                    }

                    dismiss();
                  //  notifyDataSetChanged();
                    break;
                case R.id.cancel:


                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }


}
