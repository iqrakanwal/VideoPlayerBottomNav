package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Constant;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Encryptor;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MainClass;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Method;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.VideoAdapter;
import com.videoplayer.mediaplaye.privatevideo.player.adaptor.WrapContentLinearLayoutManager;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoadListener;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.VideoLoader;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.musiclistener;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderLoader;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.zip.Inflater;

public class AllFolderList extends MyFragment implements musiclistener {
    TextView videoname, pausetime, totalduatation;
    RecyclerView recyclerView;
    FolderAdapter folderAdapter;
    LinearLayout listview, sortby, search, privatefolder, selectall, deleteselected;
    ImageView option;
   // ImageView screachcross;
    ImageView close;
    EditText editTextSearch;

    ArrayList<FolderItem> folderItems;
    boolean check = false;
    public static int flagforadaptor = 0;
    LinearLayout frameLayout;
    TextView viewtext;
    ImageView viewicon;
    Inflater inflater;
    ImageView imageView;
    ProgressBar progressBar;
    LinearLayout bottomLinearLayout;
    RelativeLayout relativeLayout;
    musiclistener musiclistener;
    RelativeLayout searchviewkalayout;
    String recentplaytitle, recentplaypath;
    ArrayList<FileItem> recentplaylist;

    public AllFolderList() {

    }

    public static AllFolderList newInstance() {
        AllFolderList fragment = new AllFolderList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        VideoLoader videoLoader = new VideoLoader(getActivity());
//        videoLoader.loadDeviceVideos(new VideoLoadListener() {
//
//
//            public void onVideoLoaded(final ArrayList<FileItem> items) {
//                progressBar.setVisibility(View.GONE);
//                folderItems = FolderLoader.getFolderList(items);
//                folderAdapter.updateData(folderItems);
//            }
//        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_folder_list, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        relativeLayout = rootView.findViewById(R.id.recentplay);
        viewtext = rootView.findViewById(R.id.view);
        editTextSearch = rootView.findViewById(R.id.editText);
        searchviewkalayout =rootView.findViewById(R.id.searchviewkalayout);
        viewicon = rootView.findViewById(R.id.viewicon);
        close = rootView.findViewById(R.id.close);
        listview = rootView.findViewById(R.id.listview);
        sortby = rootView.findViewById(R.id.sortby);
        bottomLinearLayout = rootView.findViewById(R.id.bottomLinearLayout);
        search = rootView.findViewById(R.id.search);
        privatefolder = rootView.findViewById(R.id.privatefolder);
        option = rootView.findViewById(R.id.option);
     //   screachcross = rootView.findViewById(R.id.crosssearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(getContext());
                MenuInflater inflater = new MenuInflater(getContext());
                inflater.inflate(R.menu.folder_bottom_option, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(getContext(), menuBuilder, view,false,R.attr.stylepop, R.style.YOURSTYLE);
                optionsMenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addtoplaylist:


//                                PlaylistDatabase playlistDatabase;
//                                playlistDatabase = PlaylistDatabase.getInstance(getContext());
//
//                                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
//                                alertDialogBuilder.setTitle("PlaylistName");
//                                final EditText input = new EditText(getContext());
//                                input.setTextColor(Color.BLACK);
//                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                                        LinearLayout.LayoutParams.MATCH_PARENT,
//                                        LinearLayout.LayoutParams.MATCH_PARENT);
//                                input.setLayoutParams(lp);
//                                alertDialogBuilder.setView(input);
//                                alertDialogBuilder
//                                        .setCancelable(false)
//                                        .setPositiveButton("add", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//
//                                                Playlist playlist = new Playlist(input.getText().toString());
//                                                playlistDatabase.getplaylist().insert(playlist);
//
//                                                Toast.makeText(getContext(), "inserted", Toast.LENGTH_SHORT).show();
//                                                folderItem.getVideoItems();
//
//                                                int playlistid;
//                                                PlaylistListViewModel viewModelNotes;
//                                                SongsViewModel viewModelSong;
//
//                                                viewModelNotes = ViewModelProviders.of((getActivity()).get(PlaylistListViewModel.class);
//                                                viewModelSong = ViewModelProviders.of((FragmentActivity) context).get(SongsViewModel.class);
//
//                                                playlistid = viewModelNotes.getidbytext(input.getText().toString());
//
//
//                                                for (int i = 0; i < folderItem.getVideoItems().size(); i++) {
//                                                    folderItem.getVideoItems().get(i);
//                                                    File file = new File(folderItem.getVideoItems().get(i).getPath());
//                                                    Songs song = new Songs(folderItem.getVideoItems().get(i).getFileTitle(), file.getAbsolutePath(), playlistid);
//                                                    viewModelSong.insertNote(song);
//
//                                                    Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();
//
//                                                }
//
//                                            }
//                                        })
//                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//                                for (int i = 0; i < folderItem.getVideoItems().size(); i++) {
//
//
//                                }
//

                                break;
                            case R.id.share:


                                break;
                            case R.id.hidefromscanlist:

                                break;


                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {
                    }
                });

                optionsMenu.show();


            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listview.setVisibility(View.VISIBLE);
                sortby.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                searchviewkalayout.setVisibility(View.GONE);
                editTextSearch.setText("");

            }
        });
        selectall = rootView.findViewById(R.id.seletcall);
        deleteselected = rootView.findViewById(R.id.deleteselected);


        deleteselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesUtility.getInstance(getContext()).isAlbumsInGrid()) {
                    PreferencesUtility.getInstance(getContext()).setAlbumsInGrid(false);

                    setLayoutManager();

                } else {
                    PreferencesUtility.getInstance(getContext()).setAlbumsInGrid(true);
                    setLayoutManager();

                }


            }
        });
        sortby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomsortClass cdd = new CustomsortClass(getActivity());
                cdd.show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchviewkalayout.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                sortby.setVisibility(View.GONE);
               // editTextSearch.setVisibility(View.VISIBLE);
            }
        });
        privatefolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
reload();
            }
        });
        deleteselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), ""+seletedfolder.toString(), Toast.LENGTH_SHORT).show();
//                for (int i = 0; i < seletedfolder.size(); i++) {
//
//                    File f = new File(seletedfolder.get(i).getFolderPath());
//                    f.delete();
//                }
                folderAdapter.notifyDataSetChanged();
            }
        });


//        musiclistener= new musiclistener() {
//            @Override
//            public void getfolder(String url) {
//                flagforadaptor=1;
//                VideoAdapter videoAdapter = null;
//                videoAdapter = new VideoAdapter(getActivity());
//                ArrayList<FileItem> videoItems = new ArrayList<>();
//                if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid())
//                {
//
//                    GridLayoutManager gridManager=new GridLayoutManager(getActivity(),3);
//                    VideoAdapter finalVideoAdapter = videoAdapter;
//                    gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                        @Override
//                        public int getSpanSize(int position)
//                        {
//                            //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
//                            if(finalVideoAdapter.is_add(position))
//                                return 3;
//                            else
//                                return 1;
//                        }
//                    });
//                    recyclerView.setLayoutManager(gridManager);
//                } else {
//                    recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                }
//
//                videoItems.clear();
//                videoItems = Mainutils.getInstance().folderItem.getVideoItems();
//                recyclerView.setAdapter(videoAdapter);
//                videoAdapter.updateData(videoItems);
//
//
//            }
//        };
        imageView = rootView.findViewById(R.id.imageview);
        progressBar = rootView.findViewById(R.id.progressbar);
        videoname = rootView.findViewById(R.id.videoname);
        pausetime = rootView.findViewById(R.id.pausetime);
        totalduatation = rootView.findViewById(R.id.totalduration);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FileItem fileItem = new FileItem();
                fileItem.setFileTitle(recentplaytitle);
                fileItem.setPath(recentplaypath);
                Mainutils.getInstance().videoItemsPlaylist = recentplaylist;
                Mainutils.getInstance().playingVideo = fileItem;
                Mainutils.getInstance().videoBackendService.playVideo(0, false);
                Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                getActivity().startActivity(intent);
            }
        });
        frameLayout = rootView.findViewById(R.id.banner_container);

        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
            viewtext.setText("List View ");
            viewicon.setBackgroundResource(R.drawable.ic_listicon);

            GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if (folderAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(gridManager);
        } else {
            viewtext.setText("GridView");
            viewicon.setBackgroundResource(R.drawable.ic_grid);


            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }

        folderAdapter = new FolderAdapter(getActivity(), this);
        recyclerView.setAdapter(folderAdapter);

        loadEveryThing();
        return rootView;
    }


    public void bottomlayout() {
        bottomLinearLayout.setVisibility(View.VISIBLE);


    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<FolderItem> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (FolderItem s : folderItems) {
            //if the existing elements contains the search input
            if (s.getFolderName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        folderAdapter.filterList(filterdNames);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            Toast.makeText(getContext(), ""+inflater, Toast.LENGTH_SHORT).show();
            if (inflater != null) {

                if (!DataProvider.getbuy() && check == false) {
                    UnifiedNativeAd native_admob = DataProvider.getInstance().get_native_admob();
                    if (native_admob != null) {

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                .inflate(R.layout.native_back_button, null);
                        populateUnifiedNativeAdView(native_admob, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);

                        DataProvider.getInstance().load_native_admob();
                        check = true;

                    }

                }


            }


        } else {


        }
    }


//    public void changfeadaptor(){
//
//        if(flagforadaptor==1){
//            folderItems= new ArrayList<>();
//            Mainutils.getInstance().folderItem=null;
//            progressBar.setVisibility(View.VISIBLE);
//            if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
//                GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3);
//                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
//                        if (folderAdapter.is_add(position))
//                            return 3;
//                        else
//                            return 1;
//                    }
//                });
//                recyclerView.setLayoutManager(gridManager);
//            } else {
//                recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//            }
//
//            folderAdapter = new FolderAdapter(getActivity(), musiclistener);
//            recyclerView.setAdapter(folderAdapter);
//
//            loadEveryThing();
//
//
//
//
//        }
//
//    }

    @Override
    public void onResume() {
        super.onResume();
     //   recyclerView.setAdapter(folderAdapter);
        if (Mainutils.getInstance().playingVideo != null) {
            relativeLayout.setVisibility(View.VISIBLE);

//if(Mainutils.getInstance().playingVideo.getFileTitle().contains("mp3")){
//    Glide.with(getActivity())
//            .load(R.drawable.ic_music)
//            .into(imageView);
//}

//            if(Mainutils.getInstance().playingVideo.getFileTitle().endsWith(".mp3")){
//
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_music));
//                }
//            }
            Glide.with(getActivity())
                    .load(Mainutils.getInstance().playingVideo.getPath()).listener(new RequestListener<Drawable>() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    imageView.setImageResource(R.drawable.musicicon);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imageView.setImageDrawable(resource);

                    return false;
                }
            }).submit()
            ;
            recentplaylist = new ArrayList<>();
            recentplaypath = Mainutils.getInstance().playingVideo.getPath();
            recentplaytitle = Mainutils.getInstance().playingVideo.getFileTitle();
            recentplaylist = Mainutils.getInstance().videoItemsPlaylist;
            totalduatation.setText(Mainutils.getInstance().playingVideo.getDuration());
            videoname.setText("" + Mainutils.getInstance().playingVideo.getFileTitle());
            pausetime.setText("" + convertDuration(Mainutils.getInstance().getPlayer().getCurrentPosition()));

        }

    }

    public static String convertDuration(long duration) {
        String out = null;
        long hours = 0;
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

    @SuppressLint("ResourceType")
    public void setLayoutManager() {


        if (PreferencesUtility.getInstance(getActivity()).isAlbumsInGrid()) {
            viewtext.setText("List View ");
            viewicon.setBackgroundResource(R.drawable.ic_listicon);
            GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //   Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                    if (folderAdapter.is_add(position))
                        return 3;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(gridManager);
        } else {
            viewtext.setText("GridView");
            viewicon.setBackgroundResource(R.drawable.ic_grid);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

        //  recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

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


    private void loadEveryThing() {
        VideoLoader videoLoader = new VideoLoader(getActivity());
        videoLoader.loadDeviceVideos(new VideoLoadListener() {
            public void onVideoLoaded(final ArrayList<FileItem> items) {

                //  Toast.makeText(getContext(), ""+items, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                folderItems = FolderLoader.getFolderList(items, false);
                folderAdapter.updateData(folderItems);
            }
        });
    }

    @Override
    public void sortAZ() {
        if (folderItems != null && folderItems.size() > 0) {
            folderItems = sortFolderAZ(folderItems);
            folderAdapter.updateData(folderItems);
        }

    }

    @Override
    public void sortZA() {
        if (folderItems != null && folderItems.size() > 0) {
            folderItems = sortFolderZA(folderItems);
            folderAdapter.updateData(folderItems);
        }

    }
    private void reload() {
        VideoLoader videoLoader = new VideoLoader(getActivity());
        videoLoader.loadDeviceVideos(new VideoLoadListener() {


            public void onVideoLoaded(final ArrayList<FileItem> items) {

                //  Toast.makeText(getContext(), ""+items, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                folderItems = FolderLoader.getFolderList(items, false);
                folderAdapter.updateData(folderItems);
            }
        });
    }

    @Override
    public void sortSize() {
        if (folderItems != null && folderItems.size() > 0) {
            folderItems = sortFolderNumberSong();
            folderAdapter.updateData(folderItems);
        }

    }

    private ArrayList<FolderItem> sortFolderAZ(ArrayList<FolderItem> folders) {
        ArrayList<FolderItem> m_folders = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < folders.size(); i++) {
            names.add(folders.get(i).getFolderName() + "_" + folders.get(i).getFolderPath());
        }
        Collections.sort(names, String::compareToIgnoreCase);

        for (int i = 0; i < names.size(); i++) {
            String path = names.get(i);
            for (int j = 0; j < folders.size(); j++) {
                if (path.equals(folders.get(j).getFolderName() + "_" + folders.get(j).getFolderPath())) {
                    m_folders.add(folders.get(j));
                }
            }
        }


        return m_folders;
    }

    private ArrayList<FolderItem> sortFolderZA(ArrayList<FolderItem> folders) {
        ArrayList<FolderItem> m_folders = sortFolderAZ(folders);
        Collections.reverse(m_folders);

        return m_folders;

    }

    private ArrayList<FolderItem> sortFolderNumberSong() {
        ArrayList<FolderItem> m_folders = folderItems;
        for (int i = 0; i < m_folders.size() - 1; i++) {
            for (int j = 0; j < m_folders.size() - 1 - i; j++) {
                if (m_folders.get(j).getVideoItems().size() < m_folders.get(j + 1).getVideoItems().size()) {
                    Collections.swap(m_folders, j, j + 1);
                }
            }
        }

        return m_folders;

    }


    @Override
    public void getfolder(String url) {

        bottomlayout();


    }

    public class CustomsortClass extends Dialog {

        public Activity c;
        public Dialog d;
        RadioGroup radiogroup;
        RadioButton nameradio;


        TextView ok, cancel;


        public CustomsortClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.content_sort_info);
            radiogroup = findViewById(R.id.radiogroup);

            nameradio = findViewById(R.id.nameradio);


            ok = findViewById(R.id.ok);
            cancel = findViewById(R.id.cancel);


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int selectedId = radiogroup.getCheckedRadioButtonId();
                    nameradio = (RadioButton) findViewById(selectedId);


                    if (nameradio.getText() == "Name") {
                        Toast.makeText(c, ""+nameradio.getText().toString(), Toast.LENGTH_SHORT).show();
                        sortAZ();

                    } else if (nameradio.getText() == "Date") {


                    } else if (nameradio.getText() == "Size") {


                    } else if (nameradio.getText() == "Lenght") {

                    }


                    dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                }
            });
        }
    }
}