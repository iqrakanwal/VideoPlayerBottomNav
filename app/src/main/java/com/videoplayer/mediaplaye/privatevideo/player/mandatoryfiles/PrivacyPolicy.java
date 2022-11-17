package com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.R;

import java.util.List;


public class PrivacyPolicy extends AppCompatActivity
{


    boolean buy=DataProvider.getbuy();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        TextView pp=(TextView)findViewById(R.id.pp);
        pp.setText("Expert Cleaner its subsidiaries and its affiliates (\"Company\" or \"We\" or \"Us\" or \"Our\") respect the privacy of its users (\"User\" or \"You\"). We are committed to protect our User's Information. We believe that you have a right to know our practices regarding what information we collect for Admob or third party Advertisement serving technologies, and Google Analytics for collecting Games & Apps statistics we use in our Games & Apps, and under what circumstances, if any; how we use it. When you play our Games & Apps. We disclose it.\n" +
                "IF YOU DO NOT AGREE TO THIS POLICY, PLEASE USE EXPERT CLEANER. \n" +
                "If we change or modify our privacy policy, we will post those changes to this privacy policy page. WE RESERVE THE RIGHT TO CHANGE OR MODIFY THIS PRIVACY POLICY AT ANY TIME, so please review it frequently.\n" +
                "BY PLAYING OUR GAMES & APPS, YOU AGREE THE TERMS & CONDITIONS SET FORTH IN THIS PRIVACY POLICY, including to the possible collection and processing of your Personal Information (as defined below).\n" +
                "\n" +
                "Online Use\n" +
                "Expert Cleaner does not collect any information from its Users. Some information may nonetheless be collected through third party services which are used in Expert Cleaner Games & Apps such as advertising networks and stored on such Third Parties' servers. \n" +
                "\n" +
                "Non Personal Information:\n" +
                "Expert Cleaner allows Third parties to collect non personal information or non-identifiable information (\"Non-Personal Information\").\n" +
                "To put it simply, with respect to such Non Personal Information we have no idea what is the identity of the User from which Third Parties have collected the Non-personal Information.\n" +
                "Non Personal Information collected by Third Parties may include the following technical information: \n" +
                "Type of operation system (e.g. Android, iOS, etc.)\n" +
                "Type of browser (e.g. Explorer, Firefox, Chrome, Safari, etc.)\n" +
                "Screen resolution (e.g. 800×600, 1024×768, etc.)\n" +
                "Language (e.g. English)\n" +
                "Device Vendor (e.g. Galaxy, HTC, iPhone, etc.)\n" +
                "Geo-location – only general location may be collected. No street name or city/town name is collected.\n" +
                "Expert Cleaner allows Third Parties collection of Non Personal Information for strict and limited purposes. Namely, for the limited purpose of analyzing the Service in order to provide You with the Service functionalities and performance.\n" +
                "Persistent User Identifier and No Other Personal Information:\n" +
                "Third Parties may also collect from Users a persistent User identifier. Expert Cleaner allows for this information to be collected strictly for internal operations of the Service including serving You with contextual advertisement (\"contextual advertisement\" are ads that are served to you without connection to any behavioral elements).\n" +
                "\n" +
                "Expert Cleaner does not allow Third Parties to collect information that may identify an individual (other than the persistent User identifier which is collected for the limited purpose of internal operations of the Service) nor does it allow the collection of any information of private and/or sensitive nature (\"Personal Information\").\n" +
                "\n" +
                "Expert Cleaner does not allow any personal information to be linked to the persistent identifier, even from other services.\n" +
                "Ad Serving Technology\n" +
                "Expert Cleaner's Games may employ proprietary or third party ad serving technologies that may use cookies, tracking pixels or other technologies to collect information as a result of ad serving through our Games & Apps as well as to help track results. Some dynamic in-game advertisement serving technology enable advertising to be temporarily uploaded into your Game, mobile device and replaced while you are online. We or third parties operating the advertisement serving technology may use information such as age and gender as well as information logged from your hardware or device to ensure that appropriate advertising is presented within the Games & Apps and to calculate or control the number of unique and repeat views of a given ad, and/or deliver ads that relate to your interests and measure the effectiveness of ad campaigns. We or third parties may collect data for this purpose including device ID's, information about your software, applications and hardware, browser information (and/or information passed via your browser), hardware, machine or device make and model, advertisement(s) served, in game location, length of time an advertisement was visible, other Internet and website usage information, web pages and mobile internet sites which have been viewed by you (as well as date and time), domain type, size of the advertisement, advertisement response (if any), and angle of view. The foregoing data may be used and disclosed per this policy and the privacy policy of the company providing the ad serving technology. \n" +
                "\n" +
                "These ad serving technologies are integrated into our Games & Apps; if you do not want to use this technology, do not play.\n" +
                "\n" +
                "Analytic Metrics Tools and Other Technologies\n" +
                "Expert Cleaner also uses Google Analytics, a web analytic service offered by Google Inc. (\"Google\") as a analytic metrics tool in our Games & Apps to collect information when User plays our Games & Apps on mobile devices. Google Analytics uses \"Cookies\", text files that are stored on your device and that enables the analysis of your usage of the game you are currently playing or Games & Apps you play. The information about your usage are collected through these cookies, will be transmitted to and stored on Google server based in the US. \n" +
                "\n" +
                "On behalf of Expert Cleaner, Google will use this information in order to evaluate your usage of Games & Apps played or currently playing, to make reports on game activities and/or to provide us with statistics report related to the- Games & Apps.\n" +
                "\n" +
                "\n" +
                "Information Sharing\n" +
                "We may share Information only in the following cases:\n" +
                "\n" +
                "(a) to satisfy any applicable law, regulation, legal process, subpoena or governmental request; \n" +
                "(b) to enforce this Privacy Policy or the Terms of Use, including investigation of potential violations thereof; \n" +
                "(c) to detect, prevent, or otherwise address fraud, security or technical issues; \n" +
                "(d) to respond to claims that any content published on the Service violates any right of a third-party; \n" +
                "(e) to protect the rights, property, or personal safety of the Company, its Users or the general public; \n" +
                "(f) when Company is undergoing any change in control, including by means of merger, acquisition or purchase of all or substantially all of the assets of Company; \n" +
                "(g) pursuant to your approval, in order to supply certain services You have requested from Company; \n" +
                "(h) to let our partners and affiliates serve You with contextual advertisements only;\n" +
                "\n" +
                "Security\n" +
                "We take a great care in maintaining the security of the Service and your information and in preventing unauthorized access to it through industry standard technologies and internal procedures, including through the use of encryption mechanisms. However, we cannot guarantee that unauthorized access will never occur.\n" +
                "\n" +
                "In App Purchase (IAP)\n" +
                "We provide some features or item to be bought or unlocked as in game or app purchases through the mechanism provided by the app store (Google, Apple etc.). We take it for granted that any IAP made by You or your child is with your full knowledge, understanding and consent. As We do not force or fool the user to make any IAP so the company would not be responsible for any refund or claim whatsoever regarding IAP. This will only applicable if / when \"In App Purchase\" feature is added in our Games & Apps.\n" +
                "\n" +
                "Questions and Feedback\n" +
                "If You have any questions concerning this Privacy Policy, You are most welcome to send us an email to the following address: kitoapps1@gmail.com and we will make an effort to reply within a reasonable time frame.\n");



        if(!DataProvider.getbuy())
        {
            UnifiedNativeAd native_admob= DataProvider.getInstance().get_native_admob();
            if(native_admob!=null)
            {
                LinearLayout frameLayout =
                        (LinearLayout) findViewById(R.id.banner_container);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.native_back_button, null);
                populateUnifiedNativeAdView(native_admob, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);

                DataProvider.getInstance().load_native_admob();
            }
        }
    
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
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
