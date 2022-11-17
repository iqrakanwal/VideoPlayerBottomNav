package com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.videoplayer.mediaplaye.privatevideo.player.classes.VideopUtils;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.models.AdsModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class DataProvider {


    private HashMap<Object, Object> Ads;

    private static class SingletonHolder {
        public static final DataProvider INSTANCE = new DataProvider(VideopUtils.getcontext());
    }

    public static DataProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }


    Context mcontext;
    boolean broadcast = false;
    public static boolean buyvalue;
    public boolean group1 = false, fb_enable = false, failed = false, load_request_send = false;
    ;
    FirebaseAnalytics mFirebaseAnalytics;


    @SuppressLint("MissingPermission")
    public DataProvider(Context context) {

        mcontext = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mInterstitialAd = new InterstitialAd(VideopUtils.getcontext());
       check_InApp();
        // Ad_data();

      ((new check_Ads())).execute();


    }

   /* public Map<String, Object> get_Ads() {
        return Ads;
    }
*/

    public boolean should_download_data() {
        if (broadcast)
            return true;
        SharedPreferences pref = mcontext.getSharedPreferences("ads", 0);
        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();
        long prev = pref.getLong("ads_load_time", 0);
        if (prev == 0)
            return true;
        Date current = new Date(time);
        Date previous = new Date(prev);

        long diff = current.getTime() - previous.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        if (days < 7)
            return false;
        else
            return true;
    }

    public void save_Time() {
        SharedPreferences pref = mcontext.getSharedPreferences("ads", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("ads_load_time", Calendar.getInstance().getTimeInMillis());
        editor.commit();

    }


    ////For Ad ids

 public ArrayList<AdsModel> adsModels = new ArrayList<>();

    // Map<String, Object> Ads = new HashMap<>();
    public boolean read_data_remainig = false;


    private class check_Ads extends AsyncTask<Void, Void, Void> {
        // Do the long-running work in here


        @Override
        protected Void doInBackground(Void... voids) {

            save_millis_to_db();
            if (should_download_data()) {
                    init_firestore();
            } else {
                PlaylistDatabase playlistDatabase = PlaylistDatabase.getInstance(mcontext);
                playlistDatabase.getads().getads();
//                DataBaseAdapter dba = new DataBaseAdapter(mcontext);
//                dba.open();
              //  ArrayList<Task> arraylist = (ArrayList<Task>) list;

                adsModels.addAll(playlistDatabase.getads().getads());

                       // = playlistDatabase.getads().getads();
                if (adsModels.size() > 0)
                    read_data_remainig = true;

            }


            return null;
        }

        protected void onPostExecute(Void result) {


        }

    }

    public long get_millis() {

        SharedPreferences prefs = mcontext.getSharedPreferences("first_time", 0);
        long millis = prefs.getLong("time", System.currentTimeMillis());
        return millis;


    }

    public void save_millis_to_db() {
        long current = System.currentTimeMillis();
        SharedPreferences prefs = mcontext.getSharedPreferences("first_time", 0);

        boolean saved = prefs.getBoolean("time_saved", false);

        if (!saved) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("time", current);
            editor.putBoolean("time_saved", true);
            editor.commit();


        }

    }

    boolean first = false;

    public void init_firestore() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("ads").document("test_ads");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   if (task.isSuccessful()) {

                                                       DocumentSnapshot document = task.getResult();
                                                       document.getData();
                                                       Map<String, Object> list = document.getData();


                                                       Iterator<String> itr = list.keySet().iterator();
                                                       while (itr.hasNext()) {
                                                           String key = itr.next();
                                                           String value = list.get(key).toString();
                                                           adsModels.add(new AdsModel(value, key));

                                                       }
//                                                       for(Object item : task.getResult().getData().values()) {
//                                                           adsModels.add(new AdsModel(item.toString(),));
//
//
//
//                                                       }

//                                                       for(Object item : task.getResult().getData().values()) {
//                                                           adsModels.add(item.toString());
//                                                          // Log.v("vettore", String.valueOf(partecipantsArrayList));
//                                                       }
//                                                     //  adsModels.add(document.getData()) ;
//for(int i =0 ; i<document.getData().size();i++){
//
//
//
//
//    adsModels.add(new AdsModel(document.getId(),document.getData()))
//
//
//}

//                                                       for (Map.Entry<String, Object> set : document.getData()) {
//                                                           System.out.println(set.getKey() + " = " + set.getValue());
//                                                       }
                                                       read_data_remainig = true;

                                                       (new write_ads_to_db()).execute();

                                                   } else {
                                                       ((new get_Ads_from_db())).execute();

                                                   }


                                               }

                                           }
        );


    }

    private class write_ads_to_db extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // DataBaseAdapter dba = new DataBaseAdapter(mcontext);
            PlaylistDatabase playlistDatabase = PlaylistDatabase.getInstance(mcontext);

            // dba.open();
            for (int i = 0; i < adsModels.size(); i++) {
                playlistDatabase.getads().insert(new AdsModel(adsModels.get(i).getAd_id(), adsModels.get(i).getName()));

            }
            //   if (playlistDatabase.getads().insert_Ads(adsModels))
            save_Time();
            // dba.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private class get_Ads_from_db extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistDatabase playlistDatabase = PlaylistDatabase.getInstance(mcontext);


            adsModels.addAll(playlistDatabase.getads().getads());
            if (adsModels.size() > 0)
                read_data_remainig = true;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void Ad_data() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();


       /* adsModels.add(new AdsModel("inter_admob_1", "ca-app-pub-3940256099942544/1033173712"));
        adsModels.add(new AdsModel("inter_admob_2", "ca-app-pub-3940256099942544/1033173712"));
        adsModels.add(new AdsModel("inter_admob_3", "ca-app-pub-3940256099942544/1033173712"));
        adsModels.add(new AdsModel("inter_admob_4", "ca-app-pub-3940256099942544/1033173712"));
        adsModels.add(new AdsModel("inter_admob_5", "ca-app-pub-3940256099942544/1033173712"));


        adsModels.add(new AdsModel("native_admob_1", "ca-app-pub-3940256099942544/1044960115"));
        adsModels.add(new AdsModel("native_admob_2", "ca-app-pub-3940256099942544/1044960115"));
        adsModels.add(new AdsModel("native_admob_3", "ca-app-pub-3940256099942544/1044960115"));
        adsModels.add(new AdsModel("native_admob_4", "ca-app-pub-3940256099942544/1044960115"));
        adsModels.add(new AdsModel("native_admob_5", "ca-app-pub-3940256099942544/1044960115"));
*/

        Ads = new HashMap<>();

        Ads.put("inter_admob_1", "ca-app-pub-3940256099942544/1033173712");
        Ads.put("inter_admob_2", "ca-app-pub-3940256099942544/1033173712");
        Ads.put("inter_admob_3", "ca-app-pub-3940256099942544/1033173712");
        Ads.put("inter_admob_4", "ca-app-pub-3940256099942544/1033173712");
        Ads.put("inter_admob_5", "ca-app-pub-3940256099942544/1033173712");


        Ads.put("native_back", "ca-app-pub-3940256099942544/1044960115");
        Ads.put("native_admob_1", "ca-app-pub-3940256099942544/1044960115");
        Ads.put("native_admob_2", "ca-app-pub-3940256099942544/1044960115");
        Ads.put("native_admob_3", "ca-app-pub-3940256099942544/1044960115");
        Ads.put("native_admob_4", "ca-app-pub-3940256099942544/1044960115");

        Ads.put("group1", "0");



        db.collection("ads").document("test_ads")
                .set(Ads)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully written!");
                        Toast.makeText(mcontext, "88", Toast.LENGTH_SHORT).show();
                        Toast.makeText(mcontext, "successfully written", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error writing document", e);
                        Toast.makeText(mcontext, "88", Toast.LENGTH_SHORT).show();

                        Toast.makeText(mcontext, "Error written", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void log_event(String key, String value) {

        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        mFirebaseAnalytics.logEvent(key, bundle);
    }


    ArrayList<String> ads_inter_admob = new ArrayList<>();
    ArrayList<String> ads_native_admob = new ArrayList<>();
    InterstitialAd mInterstitialAd;
    UnifiedNativeAd nativeAd;
    Handler handler_admob;
    Runnable admob;
    int reload_counter = 0, inter_admob = 0, native_admob = 0;

    public void load_Ads() {

        mInterstitialAd = new InterstitialAd(VideopUtils.getcontext());
        mInterstitialAd.setAdUnitId(ads_inter_admob.get(inter_admob));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (handler_admob != null)
                    handler_admob.removeCallbacksAndMessages(null);
                if (reload_counter > 3) {
                    failed = true;
                    return;
                }
                ++reload_counter;


                handler_admob = new Handler();
                admob = new Runnable() {
                    @Override
                    public void run() {

                        mInterstitialAd.loadAd(new AdRequest.Builder().build());

                    }
                };

                try {
                    if (i == 3)
                        handler_admob.postDelayed(admob, 15 * 60000);
                    else
                        handler_admob.postDelayed(admob, 2000);
                } catch (Exception e) {

                }

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                reload_counter = 0;
                failed = false;
                Toast.makeText(mcontext, "loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public InterstitialAd get_interstitial() {
        return mInterstitialAd;

    }

    @SuppressLint("MissingPermission")
    public void reload_admob() {

        if (inter_admob < ads_inter_admob.size() - 1)
            inter_admob++;
        else
            inter_admob = 0;

        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            return;
        }
        mInterstitialAd = new InterstitialAd(VideopUtils.getcontext());
        mInterstitialAd.setAdUnitId(ads_inter_admob.get(inter_admob));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }
    public void on_complete() {
        if (!read_data_remainig)
            return;

        load_request_send = true;




        for (int i = 0; i < adsModels.size(); i++) {
            if (adsModels.get(i).getName().contains("native")) {
                ads_native_admob.add(adsModels.get(i).getAd_id());
            } else
                ads_inter_admob.add(adsModels.get(i).getAd_id());

        }


        Collections.shuffle(ads_inter_admob);

        if (ads_inter_admob != null && ads_inter_admob.size() > 0)
            load_Ads();




        Collections.shuffle(ads_native_admob);

        if (ads_native_admob != null && ads_native_admob.size() > 0 ) {
            load_native_admob();


        }


    }
//    public UnifiedNativeAd on_complete() {
//        if (!read_data_remainig)
//            return;
//
//        load_request_send = true;
//        long first_launch = get_millis();
//        int day1 = Integer.parseInt(adsModels.get("group1").toString());
//        long group1_millis1 = first_launch + (day1 * 86400000L);
//        long current = System.currentTimeMillis();
//
//        if (current > group1_millis1)
//            group1 = true;
//        else
//            group1 = false;
//
//        for (int i = 0; i < adsModels.size(); i++) {
//            if (adsModels.get(i).getName().contains("native")) {
//                ads_native_admob.add(adsModels.get(i).getAd_id());
//            } else
//                ads_inter_admob.add(adsModels.get(i).getAd_id());
//
//        }
//
//        Collections.shuffle(ads_inter_admob);
//
//        if (ads_inter_admob != null && ads_inter_admob.size() > 0)
//            load_Ads();
//
//
//        Collections.shuffle(ads_native_admob);
//
//        if (ads_native_admob != null && ads_native_admob.size() > 0) {
//            load_native_admob();
//
//
//        }
//
//
//        Collections.shuffle(ads_inter_admob);
//
//        if (ads_inter_admob != null && ads_inter_admob.size() > 0)
//            load_Ads();
//
//
//        Collections.shuffle(ads_native_admob);
//
//        if (ads_native_admob != null && ads_native_admob.size() > 0) {
//            load_native_admob();
//
//
//        }
//
//    }


    public UnifiedNativeAd get_native_admob() {
        return nativeAd;
    }

    @SuppressLint("MissingPermission")
    public void load_native_admob() {

        nativeAd = null;

        String ADMOB_AD_UNIT_ID = ads_native_admob.get(native_admob);
        AdLoader.Builder builder = new AdLoader.Builder(mcontext, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                nativeAd = unifiedNativeAd;
                Toast.makeText(mcontext, "Native loaded", Toast.LENGTH_SHORT).show();
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {


            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();


            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        if (native_admob < ads_native_admob.size() - 1)
            native_admob++;
        else
            native_admob = 0;

    }

    public static boolean show_ad = true;
    static int clicks = 0;

    public static void toggle_ad_check() {
        ++clicks;
        if (clicks >= 3) {
            show_ad = true;
            clicks = 0;
        } else
            show_ad = false;

    }

    public boolean buy = false;

    public void check_InApp() {

        SharedPreferences pref = mcontext.getSharedPreferences("ads", 0);
        buy = pref.getBoolean("buy", false);
        if (buy) {
            boolean foever = pref.getBoolean("forever", false);
            if (!foever) {
                Calendar cal = Calendar.getInstance();
                long time = cal.getTimeInMillis();
                long prev = pref.getLong("Ad_time", 0);
                Date current = new Date();
                Date previous = new Date(prev);
                int duration = pref.getInt("duration", 0);
                if (duration == 0)
                    return;
                long diff = current.getTime() - previous.getTime();
                long days = TimeUnit.MILLISECONDS.toDays(diff);
                if (duration != 6) {
                    if (days >= duration) {
                        SharedPreferences prefs = mcontext.getSharedPreferences("ads", 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("buy", false);
                        editor.putLong("Ad_time", 0);
                        editor.putString("duration", "0");
                        editor.commit();
                        buy = false;
                    }

                } else if (duration == 6) {
                    duration = 6 * 30;
                    if (days >= duration) {
                        SharedPreferences prefs = mcontext.getSharedPreferences("ads", 0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("buy", false);
                        editor.putLong("Ad_time", 0);
                        editor.putString("duration", "0");
                        editor.commit();
                        buy = false;
                    }
                }


            }
        }


    }




    public static void setbuy(boolean value) {
        buyvalue = value;


    }

    public static boolean getbuy() {

        return buyvalue;

    }



}