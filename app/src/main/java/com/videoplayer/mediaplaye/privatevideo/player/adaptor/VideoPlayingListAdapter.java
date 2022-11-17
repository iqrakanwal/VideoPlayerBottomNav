package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import net.steamcrafted.materialiconlib.MaterialIconView;
import java.io.File;
import java.util.ArrayList;

public class VideoPlayingListAdapter extends RecyclerView.Adapter<VideoPlayingListAdapter.ItemHolder> {

    Activity context;
    private ArrayList<FileItem> videoItems = new ArrayList<>();
    public VideoPlayingListAdapter(Activity context){
        this.context = context;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_playing, null);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        FileItem videoItem = videoItems.get(i);
        File file= new File(videoItem.getPath());
        file.getAbsolutePath();
        itemHolder.title.setText(videoItem.getFileTitle());
        itemHolder.duration.setText(videoItem.getPath());
        Glide.with(context.getApplicationContext())
                .load(file.getAbsolutePath())
                .into(itemHolder.imageView);
        itemHolder.txtVideoPath.setText(videoItem.getPath());
        itemHolder.container.setOnClickListener(v -> {
            Mainutils.getInstance().playingVideo = videoItem;
            Mainutils.getInstance().videoBackendService.playVideo(0,false);
        });

        itemHolder.btnRemove.setOnClickListener(v -> {
            videoItems.remove(i);
            Mainutils.getInstance().videoItemsPlaylist = videoItems;
            updateData(videoItems);
        });


    }
    public void updateData(ArrayList<FileItem> items){
        if(items == null) items = new ArrayList<>();
        ArrayList<FileItem> r = new ArrayList<>(items);
        int currentSize = videoItems.size();
        if(currentSize != 0) {
            this.videoItems.clear();
            this.videoItems.addAll(r);
            notifyItemRangeRemoved(0, currentSize);
            //tell the recycler view how many new items we added
            notifyItemRangeInserted(0, r.size());
        }
        else {
            this.videoItems.addAll(r);
            notifyItemRangeInserted(0, r.size());
        }

    }
    @Override
    public int getItemCount() {
        return videoItems.size();
    }



    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView title, duration,txtVideoPath;
        protected ImageView imageView;
        protected MaterialIconView btnRemove;

        View container;

        public ItemHolder(View view) {
            super(view);
            container = view;
            this.txtVideoPath = view.findViewById(R.id.txtVideoPath);
            this.title = view.findViewById(R.id.txtVideoTitle);
            this.imageView = view.findViewById(R.id.imageView);
            this.duration = view.findViewById(R.id.txtVideoDuration);
            this.btnRemove = view.findViewById(R.id.btn_remove_to_playingList);
        }

    }
    public void deleteVideo(FileItem videoItem){
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.delete_video))
                .content(context.getString(R.string.confirm) +" " + videoItem.getFileTitle() + " ?")
                .positiveText(context.getString(R.string.confirm_delete))
                .negativeText(context.getString(R.string.confirm_cancel))
                .onPositive((dialog1, which) -> {
                    File deleteFile = new File(videoItem.getPath());
                    if(deleteFile.exists()){
                        if(deleteFile.delete()){
                            Mainutils.getInstance().isNeedRefreshFolder = true;
                            if(removeIfPossible(videoItem))
                                context.finish();
                            else {
                                updateData(videoItems);
                            }

                        }
                    }
                })
                .onNegative((dialog12, which) -> dialog12.dismiss())
                .show();
    }
    private boolean removeIfPossible(FileItem videoItem){
        for(FileItem video:videoItems) {
            if (videoItem.getPath().equals(video.getPath())){
                videoItems.remove(videoItem);
                break;
            }

        }
        Mainutils.getInstance().videoItemsPlaylist = new ArrayList<>(videoItems);
        if(videoItems.size() == 0){
            return true;
        }else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean name = sharedPreferences.getBoolean("autoplay", false);



            if(name){
                Mainutils.getInstance().playNext();

            }else {

            }

        }
        return false;
    }
}
