package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Encryptor;
import com.videoplayer.mediaplaye.privatevideo.player.activities.EqSettings;
import com.videoplayer.mediaplaye.privatevideo.player.activities.Equalizerclass;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FastScroller;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FolderDetails;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MainClass;
import com.videoplayer.mediaplaye.privatevideo.player.activities.MusicDetail;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.mandatoryfiles.DataProvider;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.Playlist;
import com.videoplayer.mediaplaye.privatevideo.player.models.Songs;
import com.videoplayer.mediaplaye.privatevideo.player.repository.SongsViewModel;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class AudioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScroller.BubbleTextGetter {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    Activity context;
    MusicDetail musicdetail;

    public int type = 0;
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    private ArrayList<FileItem> videoItems = new ArrayList<>();

    public AudioAdapter(Activity context, ArrayList<FileItem> videoItems) {
        this.musicdetail = (MusicDetail) context;
        this.videoItems = videoItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;

        type = i;
         if (i == AD_TYPE)
        {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_layout, null);
          //  Toast.makeText(context, "Ad type", Toast.LENGTH_SHORT).show();
            return new AdHolder(itemView);

        }
          else if (i == CONTENT_TYPE) {
//        if (PreferencesUtility.getInstance(context).isAlbumsInGrid())
//            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio_grid, null);
//        else
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio, null);


        itemView = itemView;
            }
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemHolder1, int i) {


        if (itemHolder1.getItemViewType()==CONTENT_TYPE)
        {
            ItemHolder itemHolder=(ItemHolder)itemHolder1;
            itemHolder.relativeLayout.setVisibility(View.VISIBLE);
            itemHolder.ad_container.setVisibility(View.GONE);
            FileItem videoItem = videoItems.get(i);
            File file= new File(videoItem.getPath());
            itemHolder.title.setText(videoItem.getFileTitle());
            itemHolder.artistname.setText(videoItem.getResolution());
            itemHolder.txtVideoDuration.setText(videoItem.getDuration());




            itemHolder.checkBox.setChecked(videoItem.isSelected());

            if (musicdetail.is_in_action_mode) {
                Anim anim = new Anim(100, itemHolder.linear);
                anim.setDuration(300);
                itemHolder.linear.setAnimation(anim);
                itemHolder.checkBox.setVisibility(View.VISIBLE);
                itemHolder.option.setVisibility(View.GONE);
            } else {

                Anim anim = new Anim(0, itemHolder.linear);
                anim.setDuration(300);
                itemHolder.linear.setAnimation(anim);
                itemHolder.checkBox.setChecked(false);
                itemHolder.checkBox.setVisibility(View.GONE);

                itemHolder.option.setVisibility(View.VISIBLE);


            }
            itemHolder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemHolder.checkBox.setVisibility(View.GONE);
                    musicdetail.startselection(i);
                    return true;
                }
            });
            itemHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicdetail.check(view, i);
                }
            });

     /*   Glide.with(context.getApplicationContext())
                .load(videoItem.getPath())
                .into(itemHolder.imageView);*/
            //    itemHolder.txtVideoPath.setText("/".concat(videoItem.getFolderName()).concat("     ").concat(videoItem.getFileSize()));
//        itemHolder.container.setBackgroundColor(videoItem.isSelected() ? ContextCompat.getColor(context, R.color.multiselected) : Color.TRANSPARENT);
//        itemHolder.container.setOnLongClickListener(v -> {
//            // if(context instanceof FolderDetailActivity) {
//            GlobalVar.getInstance().isMutilSelectEnalble = true;
//            videoItem.setLongClick(true);
//            videoItem.setSelected(!videoItem.isSelected());
//            itemHolder.container.setBackgroundColor(videoItem.isSelected() ? ContextCompat.getColor(context, R.color.multiselected) : Color.TRANSPARENT);
//            if (context instanceof BaseActivity) {
//                ((BaseActivity) context).updateMultiSelectedState();
//            }
//            //  }
//            return false;
//        });
            itemHolder.relativeLayout.setOnClickListener(v -> {
                Mainutils.getInstance().videoItemsPlaylist = videoItems;
                Mainutils.getInstance().playingVideo = videoItem;
                Mainutils.getInstance().seekPosition = 0;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean name = sharedPreferences.getBoolean("backgroundplaycheck", false);
                if (name) {
                    Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);

               //     Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);

                } else if (!Mainutils.getInstance().isPlayingAsPopup()) {
                 //   Equalizerclass equalizerclass= new Equalizerclass();
                   // equalizerclass.loadEqualizerSettings(context);
                    Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);

                    Mainutils.getInstance().getPlayer().addAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int playbackState) {

                        }

                        @Override
                        public void onTimelineChanged(EventTime eventTime, int reason) {

                        }

                        @Override
                        public void onPositionDiscontinuity(EventTime eventTime, int reason) {

                        }

                        @Override
                        public void onSeekStarted(EventTime eventTime) {

                        }

                        @Override
                        public void onSeekProcessed(EventTime eventTime) {

                        }

                        @Override
                        public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {

                        }

                        @Override
                        public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {

                        }

                        @Override
                        public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {

                        }

                        @Override
                        public void onLoadingChanged(EventTime eventTime, boolean isLoading) {

                        }

                        @Override
                        public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {

                        }

                        @Override
                        public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        }

                        @Override
                        public void onLoadStarted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {

                        }

                        @Override
                        public void onLoadCompleted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {

                        }

                        @Override
                        public void onLoadCanceled(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {

                        }

                        @Override
                        public void onLoadError(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

                        }

                        @Override
                        public void onDownstreamFormatChanged(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {

                        }

                        @Override
                        public void onUpstreamDiscarded(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {

                        }

                        @Override
                        public void onMediaPeriodCreated(EventTime eventTime) {

                        }

                        @Override
                        public void onMediaPeriodReleased(EventTime eventTime) {

                        }

                        @Override
                        public void onReadingStarted(EventTime eventTime) {

                        }

                        @Override
                        public void onBandwidthEstimate(EventTime eventTime, int totalLoadTimeMs, long totalBytesLoaded, long bitrateEstimate) {

                        }

                        @Override
                        public void onViewportSizeChange(EventTime eventTime, int width, int height) {

                        }

                        @Override
                        public void onNetworkTypeChanged(EventTime eventTime, @Nullable NetworkInfo networkInfo) {

                        }

                        @Override
                        public void onMetadata(EventTime eventTime, Metadata metadata) {

                        }

                        @Override
                        public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {

                        }

                        @Override
                        public void onDecoderInitialized(EventTime eventTime, int trackType, String decoderName, long initializationDurationMs) {

                        }

                        @Override
                        public void onDecoderInputFormatChanged(EventTime eventTime, int trackType, Format format) {

                        }

                        @Override
                        public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {

                        }

                        @Override
                        public void onAudioSessionId(EventTime eventTime, int audioSessionId) {
                           // add_equalizer(audioSessionId);

//                            Equalizer mEqualizer = new Equalizer(0, audioSessionId);
//                            BassBoost bassBoost = new BassBoost(0, audioSessionId);
//                            bassBoost.setEnabled(EqSettings.isEqualizerEnabled);
//                            BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
//                            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
//                            bassBoostSetting.strength = EqSettings.equalizerModel.getBassStrength();
//                            bassBoost.setProperties(bassBoostSetting);
//                            PresetReverb presetReverb = new PresetReverb(0, audioSessionId);
//                            presetReverb.setPreset(EqSettings.equalizerModel.getReverbPreset());
//                            presetReverb.setEnabled(EqSettings.isEqualizerEnabled);
//                            mEqualizer.setEnabled(EqSettings.isEqualizerEnabled);
//                            if (EqSettings.presetPos == 0){
//                                for (short bandIdx = 0; bandIdx < mEqualizer.getNumberOfBands(); bandIdx++) {
//                                    mEqualizer.setBandLevel(bandIdx, (short) EqSettings.seekbarpos[bandIdx]);
//                                }
//                            }
//                            else {
//                                mEqualizer.usePreset((short) EqSettings.presetPos);
//                            }
                        }

                        @Override
                        public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

                        }

                        @Override
                        public void onDroppedVideoFrames(EventTime eventTime, int droppedFrames, long elapsedMs) {

                        }

                        @Override
                        public void onVideoSizeChanged(EventTime eventTime, int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

                        }

                        @Override
                        public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {

                        }

                        @Override
                        public void onDrmKeysLoaded(EventTime eventTime) {

                        }

                        @Override
                        public void onDrmSessionManagerError(EventTime eventTime, Exception error) {

                        }

                        @Override
                        public void onDrmKeysRestored(EventTime eventTime) {

                        }

                        @Override
                        public void onDrmKeysRemoved(EventTime eventTime) {

                        }
                    });
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.addFlags(0);
                    intent.putExtra("activity", "audio");

                    context.startActivity(intent);
                    if (Mainutils.getInstance().videoBackendService != null)
                        Mainutils.getInstance().videoBackendService.releasePlayerView();
                } else {
                    ((BaseActivity) context).showFloatingView(context, true);
                }


            });


            itemHolder.option.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {
                    @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                    MenuInflater inflater = new MenuInflater(context);
                    inflater.inflate(R.menu.music_option, menuBuilder);
                    @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, view);

                    optionsMenu.setForceShowIcon(true);
                    menuBuilder.setCallback(new MenuBuilder.Callback() {
                        @Override
                        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.next_play:
                                    Mainutils.getInstance().videoItemsPlaylist.clear();
                                    Mainutils.getInstance().videoItemsPlaylist.add(0, videoItem);
                                    //     Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.privatefolder:
                                    File des = new File(Environment.getExternalStorageDirectory().getPath() + "/.Aaaa/");
                                    if (des.mkdir()) {
                                        //Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
                                    }
                                    File privatefolder = new File(videoItem.getPath());
                                    Encryptor.getEncrypter(true).encrypt(privatefolder, des);
                                    videoItems.remove(videoItem);
                                    Mainutils.getInstance().playingVideo = null;
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, videoItems.size());
                                    break;

                                case R.id.addtoplaylist:

                                    showBottomDialog(videoItem);
                                    break;
                                case R.id.addtoQueue:
                                    Mainutils.getInstance().videoItemsPlaylist.add(videoItem);
                                    Toast.makeText(context, "Add to Queue", Toast.LENGTH_SHORT).show();

                                    break;
                                case R.id.delete:

                                    new MaterialDialog.Builder(context)
                                            .title(context.getString(R.string.delete_video))
                                            .content(context.getString(R.string.confirm) + " " + videoItem.getFileTitle() + " ?")
                                            .positiveText(context.getString(R.string.confirm_delete))
                                            .negativeText(context.getString(R.string.confirm_cancel))
                                            .onPositive((dialog1, which) -> {
                                                File deleteFile = new File(videoItem.getPath());
                                                if (deleteFile.exists()) {
                                                    if (deleteFile.delete()) {
                                                        videoItems.remove(videoItem);
                                                        notifyItemRemoved(i);
                                                        notifyItemRangeChanged(i, videoItems.size());


                                                    }
                                                }
                                            })
                                            .onNegative((dialog12, which) -> dialog12.dismiss())
                                            .show();

                                    break;
                                case R.id.share:

                                    context.startActivity(Intent.createChooser(AppsUtils.shareAudio(context, videoItem), context.getString(R.string.action_share)));
                                    optionsMenu.dismiss();

                                    break;
                                case R.id.properties:

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    View view = context.getLayoutInflater().inflate(R.layout.content_video_info, null);

                                    TextView txtVideoTitle = view.findViewById(R.id.txtVideoTitle);
                                    txtVideoTitle.setText(videoItems.get(i).getFileTitle());

                                    TextView txtLocation = view.findViewById(R.id.txtLocation_value);
                                    txtLocation.setText(videoItems.get(i).getPath());

                                    TextView txtVideoFormat = view.findViewById(R.id.txtFormat_value);
                                    txtVideoFormat.setText(AppsUtils.getFileExtension(videoItems.get(i).getPath()));
                                    TextView txtfilesize = view.findViewById(R.id.txtFileSize);
                                    txtfilesize.setText("File " + getFileSize(file));

                                    TextView txtDuration = view.findViewById(R.id.txtDuration_value);
                                    txtDuration.setText(videoItems.get(i).getDuration());

                                    TextView txtDateAdded = view.findViewById(R.id.txtDateAdded_value);
                                    Date lastModDate = new Date(file.lastModified());
                                    txtDateAdded.setText("" + lastModDate);

                                    TextView txtVideoResolution = view.findViewById(R.id.txResolution_value);
                                    txtVideoResolution.setText(videoItems.get(i).getFileSize());
                                    builder.setView(view);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
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
//        itemHolder.imageViewOption.setOnClickListener(v -> {
//
//
//
//        });
        }        else if (itemHolder1.getItemViewType() == AD_TYPE) {


            AdHolder itemHolder=(AdHolder)itemHolder1;

            itemHolder.ad_container.setVisibility(View.VISIBLE);
            if (!DataProvider.getbuy()) {
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
    public void filterList(ArrayList<FileItem> filterdNames) {
        this.videoItems = filterdNames;
        notifyDataSetChanged();
    }
    public String getFileSize(File file) {

        if (!file.isFile()) {
            throw new IllegalArgumentException("Expected a file");
        }
        final double length = file.length();

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }



    @SuppressLint("RestrictedApi")
    private void buildPopUpMenu(View view) {

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

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 4 == 0)
            return AD_TYPE;
        else
            return CONTENT_TYPE;
    }

    public void updateData(ArrayList<FileItem> items) {
        if (items == null) items = new ArrayList<>();
        ArrayList<FileItem> r = new ArrayList<>(items);
        int currentSize = videoItems.size();
        if (currentSize != 0) {
            this.videoItems.clear();
            this.videoItems.addAll(r);
           // notifyItemRangeRemoved(0, currentSize);
            notifyItemRangeInserted(0, r.size());
        } else {
            this.videoItems.addAll(r);
            notifyItemRangeInserted(0, r.size());
        }

    }


    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public ArrayList<FileItem> getVideoItems() {
        if (videoItems == null) return new ArrayList<>();
        return videoItems;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return null;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView title, txtVideoPath, artistname;
        protected ImageView imageView;
        protected ImageView option;
        LinearLayout linear;
        CheckBox checkBox;

        TextView txtVideoDuration;
        LinearLayout ad_container;
        RelativeLayout relativeLayout;





        //  View container;

        public ItemHolder(View view) {
            super(view);
            //container = view;
            this.txtVideoPath = view.findViewById(R.id.txtVideoPath);
            this.title = view.findViewById(R.id.txtVideoTitle);
            artistname= view.findViewById(R.id.artistane);
            this.linear = view.findViewById(R.id.linearlayout);
            this.checkBox = view.findViewById(R.id.check);

            this.txtVideoDuration = view.findViewById(R.id.txtVideoDuration);
            this.imageView = view.findViewById(R.id.imageView);
            relativeLayout=view.findViewById(R.id.container);
            this.option = view.findViewById(R.id.imageViewOption);
            ad_container = view.findViewById(R.id.banner_container);
            ad_container.setVisibility(View.VISIBLE);

        }

    }
    public class AdHolder extends RecyclerView.ViewHolder {
         LinearLayout ad_container;

        //  View container;

        public AdHolder(View view) {
            super(view);
            //container = view;
            ad_container = view.findViewById(R.id.banner_container);
            ad_container.setVisibility(View.VISIBLE);


        }

    }


    public boolean is_add(int pos) {
        if ((pos + 1) % 4 == 0) {//&& DataProvider.getInstance().get_native_admob()!=null){

            return true;
        } else
            return false;


    }


    private void showBottomDialog(FileItem videoItem) {
        View view = context.getLayoutInflater().inflate(R.layout.video_option_dialog, null);
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
                             //   Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();

//                                Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(context, SelectVideoAudio.class);
//                                intent.putExtra("playlistname", input.getText().toString());
//                                context. startActivity(intent);
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
        playlistAdapter = new PlaylistAgainRecyclarView(context, playlist, videoItem);
        recyclerView.setAdapter(playlistAdapter);

//        LinearLayout option_playPopup = view.findViewById(R.id.option_play_popup);
//        option_playPopup.setOnClickListener(v -> {
//            GlobalVar.getInstance().playingVideo = videoItem;
//            GlobalVar.getInstance().videoItemsPlaylist = videoItems;
//            if (context instanceof BaseActivity) {
//                ((BaseActivity) context).showFloatingView(context, true);
//            }
//            dialog.dismiss();
//        });
//        LinearLayout option_play_audio = view.findViewById(R.id.option_play_audio);
//        option_play_audio.setOnClickListener(v -> {
//            PreferencesUtility.getInstance(context).setAllowBackgroundAudio(true);
//            GlobalVar.getInstance().videoItemsPlaylist = videoItems;
//            GlobalVar.getInstance().playingVideo = videoItem;
//            GlobalVar.getInstance().videoService.playVideo(GlobalVar.getInstance().seekPosition, false);
//            dialog.dismiss();
//        });
//        LinearLayout option_share = view.findViewById(R.id.option_share);
//        option_share.setOnClickListener(v -> {
//            context.startActivity(Intent.createChooser(kxUtils.shareVideo(context,videoItem),context.getString(R.string.action_share)));
//            dialog.dismiss();
//        });
//        LinearLayout option_rename = view.findViewById(R.id.option_rename);
//        option_rename.setOnClickListener(v -> {
//           /* RenameVideoDialog renamePlaylistDialog = RenameVideoDialog.newInstance(context,this,videoItem);
//            renamePlaylistDialog.show(((AppCompatActivity) context).getSupportFragmentManager(),"");
//            dialog.dismiss();*/
//        });
//        LinearLayout option_info = view.findViewById(R.id.option_info);
//        option_info.setOnClickListener(v -> {
//            dialog.dismiss();
//            createDialog(videoItem);
//        });
//        LinearLayout option_delete = view.findViewById(R.id.option_delete);
//        option_delete.setOnClickListener(v -> {
//            dialog.dismiss();
//         /*   new MaterialDialog.Builder(context)
//                    .title(context.getString(R.string.delete_video))
//                    .content(context.getString(R.string.confirm) +" " + videoItem.getAudioTitle() + " ?")
//                    .positiveText(context.getString(R.string.confirm_delete))
//                    .negativeText(context.getString(R.string.confirm_cancel))
//                    .onPositive((dialog1, which) -> {
//                        File deleteFile = new File(videoItem.getPath());
//                        if(deleteFile.exists()){
//                            if(deleteFile.delete()){
//                                videoItems.remove(videoItem);
//                                updateData(videoItems);
//                                if(context instanceof BaseActivity){
//                                    ((BaseActivity) context).updateListAfterDelete(videoItems);
//                                }
//                            }
//                        }
//                    })
//                    .onNegative((dialog12, which) -> dialog12.dismiss())
//                    .show();*/
//        });
        dialog.setContentView(view);
        dialog.show();
    }

    private boolean isNewNamePossible(String newName, FileItem fileItem) {
        if (fileItem == null) return false;
        String fileEx = AppsUtils.getFileExtension(fileItem.getPath());
        if (Mainutils.getInstance().folderItem == null || Mainutils.getInstance().folderItem.getVideoItems() == null)
            return false;
        for (FileItem videoItem : Mainutils.getInstance().folderItem.getVideoItems()) {
            if (newName.equals(videoItem.getFileTitle()) && fileEx.equals(AppsUtils.getFileExtension(videoItem.getPath())))
                return false;
        }
        return true;
    }

    private void createDialog(FileItem videoItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = context.getLayoutInflater();
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




    public static void scanFile(Context context, String[] path) {
        MediaScannerConnection.scanFile(context.getApplicationContext(), path, null, null);
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
