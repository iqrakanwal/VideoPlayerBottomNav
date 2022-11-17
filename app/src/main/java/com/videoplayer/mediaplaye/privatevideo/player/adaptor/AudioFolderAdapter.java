package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Encryptor;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FolderDetails;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MusicDetail;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.FragmentAudioList;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.PlaylistListViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.io.File;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AudioFolderAdapter extends RecyclerView.Adapter<AudioFolderAdapter.ItemHolder> {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    Activity context;
    private ArrayList<FolderItem> folderItems = new ArrayList<>();
    public  int type=0;
    public AudioFolderAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;
        //  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder, null);
        //   Toast.makeText(context, ""+i, Toast.LENGTH_SHORT).show();

        type=i;

        if (i == AD_TYPE)
        {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_layout, null);

        }
        else if (i == CONTENT_TYPE) {


                itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder, null);

            itemView=itemView;
            //return new ItemHolder(itemView);
        }

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {



        if(type==CONTENT_TYPE)
        {
        FolderItem folderItem = folderItems.get(i);
        itemHolder.folderTitle.setText(folderItem.getFolderName());
        itemHolder.folderPath.setText(folderItem.getFolderPath());
        itemHolder.folderSize.setText(String.valueOf(folderItem.getVideoItems().size()));
        itemHolder.container.setOnClickListener(v -> {
            Mainutils.getInstance().folderItem = folderItem;
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            context.startActivity(new Intent(context, MusicDetail.class));
//            FragmentAudioList myFragment = new FragmentAudioList();
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutaudio, myFragment).addToBackStack(null).commit();

//            Intent intent = new Intent(context, FolderDetailActivity.class);
//            context.startActivity(intent);
//            context.overridePendingTransition(R.anim.slide_in_left,
//                    R.anim.slide_stay_x);
        });
        itemHolder.imageViewOption.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                        File file = new File(folderItem.getFolderPath());
                        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                        MenuInflater inflater = new MenuInflater(context);
                        inflater.inflate(R.menu.folder_option, menuBuilder);
                        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, v);
                        optionsMenu.setForceShowIcon(true);
                        menuBuilder.setCallback(new MenuBuilder.Callback() {
                            @Override
                            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.addtoplaylist:
                                        PlaylistDatabase playlistDatabase;
                                        playlistDatabase = PlaylistDatabase.getInstance(context);
                                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                                        alertDialogBuilder.setTitle("PlaylistName");
                                        final EditText input = new EditText(context);
                                        input.setTextColor(Color.BLACK);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        input.setLayoutParams(lp);
                                        alertDialogBuilder.setView(input);
                                        alertDialogBuilder
                                                .setCancelable(false)
                                                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        Playlist playlist = new Playlist(input.getText().toString());
                                                        playlistDatabase.getplaylist().insert(playlist);

                                                        Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();
                                                        folderItem.getVideoItems();

                                                        int playlistid;
                                                        PlaylistListViewModel viewModelNotes;
                                                        SongsViewModel viewModelSong;

                                                        viewModelNotes = ViewModelProviders.of((FragmentActivity) context).get(PlaylistListViewModel.class);
                                                        viewModelSong = ViewModelProviders.of((FragmentActivity) context).get(SongsViewModel.class);

                                                        playlistid = viewModelNotes.getidbytext(input.getText().toString());


                                                        for (int i = 0; i < folderItem.getVideoItems().size(); i++) {
                                                            folderItem.getVideoItems().get(i);
                                                            File file = new File(folderItem.getVideoItems().get(i).getPath());
                                                            Songs song = new Songs(folderItem.getVideoItems().get(i).getFileTitle(), file.getAbsolutePath(), playlistid);
                                                            viewModelSong.insertNote(song);

                                                            Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                        for (int i = 0; i < folderItem.getVideoItems().size(); i++) {


                                        }


                                        break;
//                                case R.id.hidefromscanlist:
//
//
//                                    break;

                                    case R.id.privatefolder:
                                        File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
                                        if ((des.mkdir())) {
                                            Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
                                        }
                                        File privatefolder = new File(folderItem.getFolderPath());
                                        Encryptor.getEncrypter(true).encrypt(privatefolder, des);
                                        Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
                                        folderItems.remove(folderItem);
                                        notifyItemRemoved(i);
                                        notifyItemRangeChanged(i, folderItems.size());
                                        //     Mainutils.getInstance().playingVideo = null;
                                        break;
                                    case R.id.rename:
//                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//                                    View view = context.getLayoutInflater().inflate(R.layout.custom_rename_dialog, null);
//                                    EditText editText = view.findViewById(R.id.edittext);
//                                    Button ok = view.findViewById(R.id.cancel);
//                                    Button cancel = view.findViewById(R.id.ok);
//                                    builder.setView(view);
//                                    ok.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            if(editText.getText()!=null){
//
//                                                File oldFolder = new File(Environment.getExternalStorageDirectory(),folderItem.getFolderName());
//                                                File newFolder = new File(Environment.getExternalStorageDirectory(),editText.getText().toString());
//                                                boolean success = oldFolder.renameTo(newFolder);
//                                                if(success){
//
//                                                    folderItem.setFolderName(editText.getText().toString());
//                                                    Toast.makeText(context, "rename", Toast.LENGTH_SHORT).show();
//                                                }
//
//                                            }else{
//
//
//                                                Toast.makeText(context, "addfilename", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                    });
//
//                                    android.app.AlertDialog dialog = builder.create();
//                                    dialog.show();
                                    CustomDialogClass cdd = new CustomDialogClass(context, folderItem);
                                        cdd.show();
                                        break;
                                    case R.id.delete:
                                   CustomDeltedialog cdelete = new CustomDeltedialog(context, folderItem, i);
                                        cdelete.show();

                                        break;
                                    case R.id.properties:
                                        CustominfoClass cdetailed = new CustominfoClass(context, folderItem);
                                        cdetailed.show();
//                                   android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//                                    View view = context.getLayoutInflater().inflate(R.layout.content_folder_info, null);
//
//                                    TextView foldername = view.findViewById(R.id.foldername);
//                                    foldername.setText(folderItems.get(i).getFolderName());
//
//                                    TextView folderpath = view.findViewById(R.id.folderlocation);
//                                    folderpath.setText(folderItems.get(i).getFolderPath());
//
//                                    TextView foldersize = view.findViewById(R.id.foldersize);
//                                    folderSize(file);
//                                    foldersize.setText(""+folderSize(file));
//
//                                    TextView foderdate = view.findViewById(R.id.folderdate);
//                                    Date lastModDate = new Date(file.lastModified());
//                                    foderdate.setText(""+lastModDate);
//
//
//                                    TextView ok = view.findViewById(R.id.ok);
//
//
//                                    android.app.AlertDialog dialog = builder.create();
//
//                                    ok.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            dialog.dismiss();                                       }
//                                    });
//                                    builder.setView(view);
//                                    dialog.show();
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



    }
        else if(type==AD_TYPE)
        {
            if (!DataProvider.getbuy() )
            {
                UnifiedNativeAd native_admob = DataProvider.getInstance().get_native_admob();
                if (native_admob != null) {

                    UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater()
                            .inflate(R.layout.native_admob, null);
                    populateUnifiedNativeAdView(native_admob, adView);
                    // frameLayout.removeAllViews();
                    //itemView.addView(adView);

                    DataProvider.getInstance().load_native_admob();

                    itemHolder.ad_container.removeAllViews();
                    itemHolder.ad_container.addView(adView);
                    //  return new ItemHolder(adView);

                }

            }

        }
    }
    public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        FolderItem folderItem;
        EditText editText;

        public CustomDialogClass(Activity a, FolderItem folderItem) {
            super(a);
            // TODO Auto-generated constructor stub
            this.folderItem = folderItem;
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_rename_dialog);
            yes = (Button) findViewById(R.id.ok);
            no = (Button) findViewById(R.id.cancel);
            editText = findViewById(R.id.edittext);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ok:
                    if (editText.getText() != null) {


                        File src = new File(folderItem.getFolderPath());


                        Toast.makeText(c, "" + src.getAbsolutePath(), Toast.LENGTH_LONG).show();

                        File filenew = new File(src.getParent() + "/" + editText.getText().toString());
                        Toast.makeText(c, "" + filenew.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        src.renameTo(new File(src.getParent() + "/" + editText.getText().toString()));

//                                                File oldFolder = new File(Environment.getExternalStorageDirectory(),folderItem.getFolderName());
//                                                File newFolder = new File(Environment.getExternalStorageDirectory(),editText.getText().toString());
//                                                boolean success = oldFolder.renameTo(newFolder);


                        folderItem.setFolderName(editText.getText().toString());
                        Toast.makeText(context, "rename", Toast.LENGTH_SHORT).show();


                    } else {


                        Toast.makeText(context, "addfilename", Toast.LENGTH_SHORT).show();
                    }

                    dismiss();
                    notifyDataSetChanged();
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

    public void filterList(ArrayList<FolderItem> filterdNames) {
        this.folderItems = filterdNames;
        notifyDataSetChanged();
    }



    public class CustomDeltedialog extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        FolderItem folderItem;
        ImageView cross;
        int position;

        public CustomDeltedialog(Activity a, FolderItem folderItem, int position1) {
            super(a);
            this.folderItem = folderItem;
            this.c = a;
            this.position = position1;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_delete_dialog);
            yes = (Button) findViewById(R.id.yes);
            no = (Button) findViewById(R.id.no);
            cross =  findViewById(R.id.cross);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    File dir = new File( folderItem.getFolderPath());
                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    }
                    dir.delete();
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    folderItems.remove(folderItem);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, folderItems.size());


                    dismiss();
                    break;
                case R.id.no:


                    dismiss();
                    break; case R.id.cross:


                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public class CustominfoClass extends Dialog {

        public Activity c;
        public Dialog d;
        TextView foldername;

        TextView folderpath;


        TextView foldersize;


        TextView foderdate;


        TextView ok;

        FolderItem folderItem;

        public CustominfoClass(Activity a, FolderItem folderItem) {
            super(a);
            // TODO Auto-generated constructor stub
            this.folderItem = folderItem;
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.content_folder_info);
            foldername = findViewById(R.id.foldername);
            folderpath = findViewById(R.id.folderlocation);

            foldersize = findViewById(R.id.foldersize);


            foderdate = findViewById(R.id.folderdate);
            ok = findViewById(R.id.ok);

            foldername.setText(folderItem.getFolderName());
            folderpath.setText(folderItem.getFolderPath());
            File file = new File(folderItem.getFolderPath());

            foldersize.setText("" + humanReadableByteCountSI(folderSize(file)));
            Date lastModDate = new Date(file.lastModified());
            foderdate.setText("" + lastModDate);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }


    }
    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }
    public void updateData(ArrayList<FolderItem> items) {
        if (items == null) items = new ArrayList<>();

        int currentSize = folderItems.size();
        if(currentSize != 0) {
            this.folderItems.clear();
            this.folderItems.addAll(items);
            notifyItemRangeRemoved(0, currentSize);
            notifyItemRangeInserted(0, items.size());
        }
        else {
            this.folderItems.addAll(items);
            notifyItemRangeInserted(0, items.size());
        }
    }

    @Override
    public int getItemCount() {
        return folderItems.size();
    }

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView folderTitle, folderPath, folderSize;
        protected ImageView imageViewOption;
        LinearLayout ad_container;

        View container;

        public ItemHolder(View view) {
            super(view);
            container = view;
            this.folderTitle = view.findViewById(R.id.txtFolderName);
            this.folderPath = view.findViewById(R.id.txtFolderPath);
            this.folderSize = view.findViewById(R.id.txtFolderSize);
            this.imageViewOption = view.findViewById(R.id.folderoption);
            ad_container=view.findViewById(R.id.banner_container);
            ad_container.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public int getItemViewType(int position)
    {
        if((position+1)%4==0 )//&& DataProvider.getInstance().get_native_admob()!=null)
            return AD_TYPE;
        else
            return CONTENT_TYPE;
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
        ((Button) adView.getCallToActionView()).setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
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


    public boolean is_add (int pos ){
        if((pos+1)%4==0 )
        {//&& DataProvider.getInstance().get_native_admob()!=null){

            return true;
        }else
            return false;



    }

//    private void showBottomDialog(FolderItem folderItem) {
//        View view = context.getLayoutInflater().inflate(R.layout.folder_option_dialog, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(context);
//
//        LinearLayout option_play = view.findViewById(R.id.option_play);
//        option_play.setOnClickListener(v -> {
//            if (folderItem == null || folderItem.getVideoItems() == null || folderItem.getVideoItems().size() == 0)
//                return;
//            GlobalVar.getInstance().videoItemsPlaylist = folderItem.getVideoItems();
//            GlobalVar.getInstance().folderItem = folderItem;
//            GlobalVar.getInstance().playingVideo = folderItem.getVideoItems().get(0);
//            GlobalVar.getInstance().videoService.playVideo(GlobalVar.getInstance().seekPosition, false);
//            Intent intent = new Intent(context, PlayVideoActivity.class);
//            context.startActivity(intent);
//            if (GlobalVar.getInstance().videoService != null)
//                GlobalVar.getInstance().videoService.releasePlayerView();
//            dialog.dismiss();
//        });
//
//        LinearLayout option_play_audio = view.findViewById(R.id.option_play_audio);
//        option_play_audio.setOnClickListener(v -> {
//            if (folderItem == null || folderItem.getVideoItems() == null || folderItem.getVideoItems().size() == 0)
//                return;
//            PreferencesUtility.getInstance(context).setAllowBackgroundAudio(true);
//            GlobalVar.getInstance().folderItem = folderItem;
//            GlobalVar.getInstance().videoItemsPlaylist = folderItem.getVideoItems();
//            GlobalVar.getInstance().playingVideo = folderItem.getVideoItems().get(0);
//            GlobalVar.getInstance().videoService.playVideo(GlobalVar.getInstance().seekPosition, false);
//            dialog.dismiss();
//        });
//
//        dialog.setContentView(view);
//        dialog.show();
//    }
}
