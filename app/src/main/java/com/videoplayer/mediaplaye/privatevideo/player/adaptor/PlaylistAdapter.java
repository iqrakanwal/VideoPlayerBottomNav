package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.R;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemHolder> {

    Activity context;
    private ArrayList<String> playlist = new ArrayList<>();

    public PlaylistAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.playlist_model_item, null);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

    }


    @Override
    public int getItemCount() {
        return playlist.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView folderTitle, folderPath, folderSize;
        protected ImageView imageViewOption;

        View container;

        public ItemHolder(View view) {
            super(view);
            container = view;
            this.folderTitle = view.findViewById(R.id.txtFolderName);
            this.folderPath = view.findViewById(R.id.txtFolderPath);
            this.folderSize = view.findViewById(R.id.txtFolderSize);
            this.imageViewOption = view.findViewById(R.id.optiomenu);

        }

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
