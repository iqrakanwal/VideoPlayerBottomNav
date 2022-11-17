package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Encryptor;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FastScroller;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FolderDetails;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.Favorite;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapter.folderSize;
import static com.videoplayer.mediaplaye.privatevideo.player.adaptor.FolderAdapter.humanReadableByteCountSI;


public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScroller.BubbleTextGetter {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    Activity context;
    FolderDetails folderDetails;
    public int type = 0;
    private ArrayList<FileItem> videoItems = new ArrayList<>();
    public VideoAdapter(Activity context, ArrayList<FileItem> videoItems) {
        this.folderDetails = (FolderDetails) context;
        this.videoItems = videoItems;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;

        type = i;
        if (i == AD_TYPE) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_layout, null);
            Toast.makeText(context, "Ad type", Toast.LENGTH_SHORT).show();
            return new AdHolder(itemView);

        } else if (i == CONTENT_TYPE) {

            if (PreferencesUtility.getInstance(context).isAlbumsInGrid())
                itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_grid, null);
            else
                itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, null);
            itemView = itemView;
        }
        return new ItemHolder(itemView);
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemHolder1, int i) {
        if (itemHolder1.getItemViewType() == CONTENT_TYPE) {
            ItemHolder itemHolder = (ItemHolder) itemHolder1;
            itemHolder.relativeLayout.setVisibility(View.VISIBLE);
            itemHolder.ad_container.setVisibility(View.GONE);
            FileItem videoItem = videoItems.get(i);
            itemHolder.title.setText(videoItem.getFileTitle());
            itemHolder.duration.setText(videoItem.getDuration());
            Glide.with(context.getApplicationContext())
                    .load(videoItem.getPath())
                    .into(itemHolder.imageView);
    /*        if (folderDetails.position == i) {
                itemHolder.checkBox.setChecked(true);
                folderDetails.position = -1;
            }
            if (folderDetails.position == i) {
                itemHolder.checkBox.setChecked(true);
                folderDetails.position = -1;
            }
            if (folderDetails.isSelectedall) {
                itemHolder.checkBox.setChecked(true);
                selection.add(videoItem);

            } else {

                itemHolder.checkBox.setChecked(false);
                selection.remove(videoItem);

            }*/



            itemHolder.checkBox.setChecked(videoItem.isSelected());

            if (folderDetails.is_in_action_mode) {
                Anim anim = new Anim(100, itemHolder.linear);
                anim.setDuration(300);
                itemHolder.linear.setAnimation(anim);
                itemHolder.checkBox.setVisibility(View.VISIBLE);
                itemHolder.imageViewOption.setVisibility(View.GONE);
            } else {

                Anim anim = new Anim(0, itemHolder.linear);
                anim.setDuration(300);
                itemHolder.linear.setAnimation(anim);
                itemHolder.checkBox.setChecked(false);
                itemHolder.checkBox.setVisibility(View.GONE);
                itemHolder.imageViewOption.setVisibility(View.VISIBLE);


            }


            itemHolder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemHolder.checkBox.setVisibility(View.GONE);
                    folderDetails.startselection(i);
                    return true;
                }
            });
            itemHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    folderDetails.check(view, i);
                }
            });
            itemHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(folderDetails.is_in_action_mode){
//                        folderDetails.check(v, i);
//
//                    }else{

                        Mainutils.getInstance().videoItemsPlaylist = videoItems;
                        Mainutils.getInstance().playingVideo = videoItem;
                        Mainutils.getInstance().seekPosition = 0;
                        if(Mainutils.getInstance().isbackgroundplay(context)){
                            Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);


                            Intent intent = new Intent(context, PlayVideoActivity.class);
                           // intent.putExtra("activity", "v");
                            context.startActivity(intent);
                        } else if (!Mainutils.getInstance().isPlayingAsPopup()) {
                            Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);
                            Intent intent = new Intent(context, PlayVideoActivity.class);
                            context.startActivity(intent);
                            if (Mainutils.getInstance().videoBackendService != null)
                                Mainutils.getInstance().videoBackendService.releasePlayerView();

                        }
                    }
                //}
            });
            itemHolder.imageViewOption.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {
                    @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                    MenuInflater inflater = new MenuInflater(context);
                    inflater.inflate(R.menu.video_option, menuBuilder);
                    @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, view);
                    optionsMenu.setForceShowIcon(true);
                    menuBuilder.setCallback(new MenuBuilder.Callback() {
                        @Override
                        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.privatef:
                                    File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
                                    if ((des.mkdir())) {
                                        Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
                                    }
                                    File privatefolder = new File(videoItem.getPath());
                                    Encryptor.getEncrypter(true).encrypt(privatefolder, des);
                                    Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
                                    videoItems.remove(videoItem);
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, videoItems.size());
                                    Mainutils.getInstance().playingVideo = null;
                                    break;
                                case R.id.addtoplaylist:
                                    showBottomDialog(videoItem);

                                    break;

                                case R.id.addtofavirote:

                                    PlaylistDatabase playlistDatabase = PlaylistDatabase.getInstance(context);
                                    Favorite fav = new Favorite();
                                    fav.setSongPath(videoItem.getPath());
                                    playlistDatabase.getFavorite().insert(fav);
                                    Toast.makeText(context, "Add Favirote", Toast.LENGTH_SHORT).show();

                                    break;

                                case R.id.rename:
                                    File f = new File(videoItem.getPath());
                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                                    alertDialogBuilder.setTitle("PlaylistName");
                                    final EditText input = new EditText(context);
                                    input.setTextColor(Color.BLACK);
                                    input.setText(f.getName());
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialogBuilder.setView(input);
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("rename", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    File src = new File(videoItem.getPath());
                                                    Toast.makeText(context, "" + src.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                                    File filenew = new File(src.getParent() + "/" + input.getText().toString());
                                                    Toast.makeText(context, "" + filenew.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                                    src.renameTo(new File(src.getParent() + "/" + input.getText().toString()));
                                                    videoItem.setFileTitle(input.getText().toString());
                                                    notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                    break;


                                case R.id.delete:

                                    CustomDeltedialog cdelete = new CustomDeltedialog(context, videoItem, i);
                                    cdelete.show();
//
//                                    new MaterialDialog.Builder(context)
//                                            .title(context.getString(R.string.delete_video))
//                                            .content(context.getString(R.string.confirm) + " " + videoItem.getFileTitle() + " ?")
//                                            .positiveText(context.getString(R.string.confirm_delete))
//                                            .negativeText(context.getString(R.string.confirm_cancel))
//                                            .onPositive((dialog1, which) -> {
//                                                File deleteFile = new File(videoItem.getPath());
//                                                if (deleteFile.exists()) {
//                                                    if (deleteFile.delete()) {
//                                                        videoItems.remove(videoItem);
//                                                        notifyItemRemoved(i);
//                                                        notifyItemRangeChanged(i, videoItems.size());
//                                                    }
//                                                }
//                                            })
//                                            .onNegative((dialog12, which) -> dialog12.dismiss())
//                                            .show();



                                    break;
                                case R.id.share:
                                    context.startActivity(Intent.createChooser(AppsUtils.shareVideo(context, videoItem),
                                            context.getString(R.string.action_share)));
                                    break;
                                case R.id.properties:
                                   CustominfoClass cdetailed = new CustominfoClass(context, videoItem);
                                   cdetailed.show();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.attr.alertDialogTheme);
//                                    View view = folderDetails.getLayoutInflater().inflate(R.layout.content_video_info, null);
//                                    TextView txtVideoTitle = view.findViewById(R.id.txtVideoTitle);
//                                    txtVideoTitle.setText(videoItems.get(i).getFileTitle());
//                                    TextView txtLocation = view.findViewById(R.id.txtLocation_value);
//                                    txtLocation.setText(videoItems.get(i).getPath());
//                                    TextView txtVideoFormat = view.findViewById(R.id.txtFormat_value);
//                                    txtVideoFormat.setText(AppsUtils.getFileExtension(videoItems.get(i).getPath()));
//                                    TextView txtDuration = view.findViewById(R.id.txtDuration_value);
//                                    txtDuration.setText(videoItems.get(i).getDuration());
//                                    TextView txtDateAdded = view.findViewById(R.id.txtDateAdded_value);
//                                    txtDateAdded.setText(videoItems.get(i).getDateAded());
//                                    TextView txtVideoResolution = view.findViewById(R.id.txResolution_value);
//                                    txtVideoResolution.setText(videoItems.get(i).getResolution());
//                                    builder.setView(view);
//                                    AlertDialog dialog = builder.create();
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
        else if (itemHolder1.getItemViewType() == AD_TYPE) {
            AdHolder itemHolder = (AdHolder) itemHolder1;
            itemHolder.ad_container.setVisibility(View.VISIBLE);
            if (!DataProvider.getbuy()) {
                UnifiedNativeAd native_admob = DataProvider.getInstance().get_native_admob();
                if (native_admob != null) {
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) folderDetails.getLayoutInflater().inflate(R.layout.native_admob, null);
                    populateUnifiedNativeAdView(native_admob, adView);
                    DataProvider.getInstance().load_native_admob();
                    itemHolder.ad_container.removeAllViews();
                    itemHolder.ad_container.addView(adView);

                }

            }

        }

    }
    public class CustomDeltedialog extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        FileItem folderItem;
        int position;

        public CustomDeltedialog(Activity a, FileItem folderItem, int position1) {
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
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    File dir = new File( folderItem.getPath());
                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    }
                    dir.delete();
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    videoItems.remove(folderItem);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, videoItems.size());


                    dismiss();
                    break;
                case R.id.no:


                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public void updateData(ArrayList<FileItem> items) {
        if (items == null) items = new ArrayList<>();
        ArrayList<FileItem> r = new ArrayList<>(items);
        int currentSize = videoItems.size();
        if (currentSize != 0) {
            this.videoItems.clear();
            this.videoItems.addAll(r);
            notifyItemRangeRemoved(0, currentSize);
            notifyItemRangeInserted(0, r.size());
        } else {
            this.videoItems.addAll(r);
            notifyItemRangeInserted(0, r.size());
        }

    }

    public static String getFileNameFromPath(String path) {
        if (path == null) return "Unknown File";
        int i = path.lastIndexOf("/");
        if (i == 0) return path;
        return path.substring(i);

    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) == 7)//&& DataProvider.getInstance().get_native_admob()!=null)
            return AD_TYPE;
        else
            return CONTENT_TYPE;
    }

    public ArrayList<FileItem> getVideoItems() {
        if (videoItems == null) return new ArrayList<>();
        return videoItems;
    }

    @Override
    public String getTextToShowInBubble(int pos) {

        return null;
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

        public CustominfoClass(Activity a, FileItem fileItem) {
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

    public void filterList(ArrayList<FileItem> filterdNames) {
        this.videoItems = filterdNames;
        notifyDataSetChanged();
    }
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title, duration, txtVideoPath;
        protected ImageView imageView, imageViewOption;
        LinearLayout ad_container;
        LinearLayout linear;
        RelativeLayout relativeLayout;
        CheckBox checkBox;
        View container;

        public ItemHolder(View view) {
            super(view);
            container = view;
            this.txtVideoPath = view.findViewById(R.id.txtVideoPath);
            this.title = view.findViewById(R.id.txtVideoTitle);
            this.imageView = view.findViewById(R.id.imageView);
            this.duration = view.findViewById(R.id.txtVideoDuration);
            this.checkBox = view.findViewById(R.id.check);
            this.imageViewOption = view.findViewById(R.id.imageViewOption);
            this.linear = view.findViewById(R.id.linearlayout);
            relativeLayout = view.findViewById(R.id.container);
            ad_container = view.findViewById(R.id.banner_container);
            ad_container.setVisibility(View.VISIBLE);
            this.checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            folderDetails.preperaSelection(view, getAdapterPosition());
        }
    }

    public class AdHolder extends RecyclerView.ViewHolder {
        LinearLayout ad_container;
        //  View container;
        public AdHolder(View view) {
            super(view);
            ad_container = view.findViewById(R.id.banner_container);
            ad_container.setVisibility(View.VISIBLE);


        }

    }


    public boolean is_add(int pos) {
        if ((pos + 1) == 7) {//&& DataProvider.getInstance().get_native_admob()!=null){

            return true;
        } else
            return false;


    }

    private void createDialog(FileItem videoItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = folderDetails.getLayoutInflater();
        View view = inflater.inflate(R.layout.content_video_info, null);

        TextView txtVideoTitle = view.findViewById(R.id.txtVideoTitle);
        txtVideoTitle.setText(videoItem.getFileTitle());

        TextView txtLocation = view.findViewById(R.id.txtLocation_value);
        txtLocation.setText(videoItem.getPath());

        TextView txtVideoFormat = view.findViewById(R.id.txtFormat_value);
        txtVideoFormat.setText(AppsUtils.getFileExtension(videoItem.getPath()));

        TextView txtDuration = view.findViewById(R.id.txtDuration_value);
        txtDuration.setText(videoItem.getDuration());

        TextView txtDateAdded = view.findViewById(R.id.txtDateAdded_value);
        txtDateAdded.setText(videoItem.getFileTitle());

        TextView txtVideoSize = view.findViewById(R.id.txtFileSize_value);
        txtVideoSize.setText(videoItem.getFileSize());

        TextView txtVideoResolution = view.findViewById(R.id.txResolution_value);
        txtVideoResolution.setText(videoItem.getFileTitle());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkError(List<FileItem> songs, int position) {
        if (songs.size() >= position && position >= 0) return true;
        return false;
    }

    public int getTotalVideoSelected() {
        int totalVideoSelected = 0;
        if (videoItems == null || videoItems.size() == 0) return 0;
        for (FileItem videoItem : videoItems) {
            if (videoItem.isSelected()) totalVideoSelected += 1;
        }
        return totalVideoSelected;
    }

    public ArrayList<FileItem> getVideoItemsSelected() {
        ArrayList<FileItem> resultList = new ArrayList<>();
        for (FileItem videoItem : videoItems) {
            if (videoItem.isSelected()) resultList.add(videoItem);
        }
        return resultList;
    }

    public void deleteListVideoSelected() {
        ArrayList<FileItem> deletedList = getVideoItemsSelected();
        Toast.makeText(context, "" + deletedList.size(), Toast.LENGTH_SHORT).show();
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.delete_video))
                .content(context.getString(R.string.confirm) + " " + String.valueOf(deletedList.size()) + " " + context.getString(R.string.video) + " ?")
                .positiveText(context.getString(R.string.confirm_delete))
                .negativeText(context.getString(R.string.confirm_cancel))
                .onPositive((dialog1, which) -> {
                    for (FileItem item : deletedList) {
                        File deleteFile = new File(item.getPath());
                        if (deleteFile.exists()) {
                            if (deleteFile.delete()) {
                                videoItems.remove(item);
                            }
                        }
                    }
                    updateData(videoItems);
                })
                .onNegative((dialog12, which) -> dialog12.dismiss())
                .show();
    }

    private void showBottomDialog(FileItem videoItem) {
        View view = folderDetails.getLayoutInflater().inflate(R.layout.video_option_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        ArrayList<Playlist> playlist = new ArrayList<>();
        PlaylistDatabase playlistDatabase = PlaylistDatabase.getInstance(context);
        playlist.addAll(playlistDatabase.getplaylist().getAll());

        ImageView linearLayout = view.findViewById(R.id.add);
        PlaylistAgainRecyclarView playlistAdapter;

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                int Playlistid = playlistDatabase.getplaylist().getIdByText(input.getText().toString());
                                videoItem.getPath();
                                SongsViewModel viewModelSong;
                                viewModelSong = ViewModelProviders.of((FragmentActivity) context).get(SongsViewModel.class);
                                viewModelSong.insertNote(new Songs(videoItem.getFileTitle(), videoItem.getPath(), Playlistid));
                                Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclarbottomsheet);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        playlistAdapter = new PlaylistAgainRecyclarView(folderDetails, playlist, videoItem);
        recyclerView.setAdapter(playlistAdapter);
        dialog.setContentView(view);
        dialog.show();
    }

    class Anim extends Animation {

        private int width, startwidth;
        private View view;

        public Anim(int width, View view) {


            this.width = width;
            this.view = view;
            this.startwidth = view.getWidth();

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newWidth = startwidth + (int) (width - startwidth) * (int) interpolatedTime;
            view.getLayoutParams().width = newWidth;
            view.requestLayout();

            super.applyTransformation(interpolatedTime, t);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
