package com.videoplayer.mediaplaye.privatevideo.player.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.PreferencesSettings;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.SecuirityQuestionFragment;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.SecurityFragment;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PrefManager;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

public class Hiddencabinet extends BaseActivity implements AdapterView.OnItemSelectedListener {

    Button save;
    ArrayList<String> questions;
    EditText editText;
    Spinner spinner;
  //  LinearLayout frameLayout;
    boolean check = false;
    PreferencesUtility preferencesUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiddencabinet);
        preferencesUtility = new PreferencesUtility(Hiddencabinet.this);

        questions = new ArrayList<>();
        editText = findViewById(R.id.answer);
        //frameLayout = (LinearLayout) findViewById(R.id.banner_container);

        addquestions();
        spinner = findViewById(R.id.questionslist);
        save = findViewById(R.id.save);
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
//                (Hiddencabinet.this, android.R.layout.simple_spinner_item,
        spinner.setOnItemSelectedListener(this);

        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),questions);
        spinner.setAdapter(customAdapter);
        if (editText.getText() != null) {
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Hiddencabinet.this, "df", Toast.LENGTH_SHORT).show();
                if (editText.getText().toString() != null) {
                    if (spinner.getSelectedItem() != null) {
                        preferencesUtility.setQuestion(spinner.getSelectedItem().toString()); // Storing string
                        preferencesUtility.setAnser(editText.getText().toString()); // Storing string
                        Toast.makeText(Hiddencabinet.this, "Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Hiddencabinet.this, PrivateFolder.class));


                    }

                } else {
                    Toast.makeText(Hiddencabinet.this, "enter answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        if (!DataProvider.getbuy()) {
//            UnifiedNativeAd native_admob = DataProvider.getInstance().get_native_admob();
//            if (native_admob != null) {
//
//                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
//                        .inflate(R.layout.native_back_button, null);
//                populateUnifiedNativeAdView(native_admob, adView);
//                frameLayout.removeAllViews();
//                frameLayout.addView(adView);
//
//                DataProvider.getInstance().load_native_admob();
//                check = true;
//            }
//
//
//        }
    }
    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(), questions.get(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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

    public void addquestions() {
        questions.add("what is your best teacher name?");
        questions.add("what was your first school's name?");
        questions.add("what was your first pet's name?");
        questions.add("write your own questions?");


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> countryNames;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, ArrayList<String> countryNames) {
            this.context = applicationContext;
            this.countryNames = countryNames;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return countryNames.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.my_spinner_style, null);
            TextView names =  view.findViewById(R.id.spinnertext);
            names.setTextColor(Color.BLACK);
            names.setText(countryNames.get(i));
            return view;
        }
    }

}
