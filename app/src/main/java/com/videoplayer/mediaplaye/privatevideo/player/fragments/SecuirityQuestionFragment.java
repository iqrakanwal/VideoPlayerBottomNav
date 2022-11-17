package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PrivateFolder;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class SecuirityQuestionFragment extends Fragment {
    Button save;
    ArrayList<String> questions;
    EditText editText;
    Spinner spinner;
    LinearLayout frameLayout;
    boolean check = false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public SecuirityQuestionFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_secuirity_question, container, false);
        questions = new ArrayList<>();
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        editText = v.findViewById(R.id.answer);
        frameLayout= (LinearLayout) v.findViewById(R.id.banner_container);
        editor = pref.edit();
        addquestions();
        spinner = v.findViewById(R.id.questionslist);
        save = v.findViewById(R.id.save);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item,
                        questions);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        if (editText.getText() != null) {
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText() != null) {
                    if (spinner.getSelectedItem() != null) {
                        editor.putString("question", spinner.getSelectedItem().toString()); // Storing string
                        editor.putString("answer", editText.getText().toString()); // Storing string
                        editor.commit(); // commit changes

                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), PrivateFolder.class));


                    }

                } else {
                    Toast.makeText(getContext(), "enter answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }


    public void addquestions() {
        questions.add("what is your best teacher name?");
        questions.add("what was your first school's name?");
        questions.add("what was your first pet's name?");
        questions.add("write your own questions?");


    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


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

}

