package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.videoplayer.mediaplaye.privatevideo.player.BaseActivity;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.listeners.musiocoptionlistener;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.util.ArrayList;

public class AudioRecyclarAdaptor extends RecyclerView.Adapter<AudioRecyclarAdaptor.MyViewHolder> {
    Activity context;
    musiocoptionlistener musiocoptionlistener;
    Mainutils mainutils = Mainutils.getInstance();
    ArrayList<FileItem> fileItems=new ArrayList<>() ;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView audioName;
        private TextView path;
        private RelativeLayout linearLayout;
        ImageView imageView;
        ImageView option;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioname);
            path = itemView.findViewById(R.id.path);
            linearLayout = itemView.findViewById(R.id.ll_audio);
            imageView = itemView.findViewById(R.id.audio_thumbnail);
            option = itemView.findViewById(R.id.option);
        }


    }

    public AudioRecyclarAdaptor(Activity context1, musiocoptionlistener musiocoptionlistener1) {
        this.context = context1;
        this.musiocoptionlistener = musiocoptionlistener1;
    }

    @Override
    public AudioRecyclarAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.audio_item, parent, false);
        return new AudioRecyclarAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        FileItem videoItem = fileItems.get(position);

        File f = new File(fileItems.get(position).getPath());
        Glide
                .with(context)
                .load(f.getPath())
                .centerCrop()
                .into(holder.imageView);

     //   Toast.makeText(context, "" + videoItem.getFileTitle(), Toast.LENGTH_SHORT).show();
        holder.audioName.setText(fileItems.get(position).getFileTitle());
        holder.audioName.setOnClickListener(v -> {
            Mainutils.getInstance().videoItemsPlaylist = fileItems;
            Mainutils.getInstance().playingVideo = videoItem;
            Mainutils.getInstance().seekPosition = 0;

                    Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition,false);
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    context.startActivity(intent);
                    if(Mainutils.getInstance().videoBackendService != null)
                        Mainutils.getInstance().videoBackendService.releasePlayerView();


            try {
                videoItem.setLongClick(false);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }





    });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // globalVar.playingVideo= folders.get(position);


//                Intent intent= new Intent(context, PlayVideoActivity.class);
//                intent.putExtra("playviodeurl", folders.get(position).getPath());
//                context.startActivity(intent);


            }
        });


        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.music_option, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.next_play:
                                /*File des = new File(Environment.getExternalStorageDirectory().getPath() + "/Aaaa/");
                                File f = new File(fileItems.get(position).getPath());

                                Encryptor.getEncrypter(true).encrypt(f, des);*/


                                                           //  fileItems.remove(videoItem);
                        //        notifyItemRemoved(position);
                               /* fileItems.clear();
                                notifyItemRemoved(position);
*/                              //  fileItems.remove(position);
                             // updatedata();
//                                notifyItemRangeRemoved(0, position);
//                                //tell the recycler view how many new items we added
//                                notifyItemRangeInserted(0, fileItems.size());

                               // updatedata();
                                break;
                            case R.id.addtoplaylist:
                                // musiocoptionlistener.addtoplaylist(folders.get(position).getaPath());


                                break;
                            case R.id.addtoQueue:

                                //  musiocoptionlistener.addtoqueue(folders.get(position).getaPath());

                                break;

                            case R.id.delete:

                                new MaterialDialog.Builder(context)
                    .title(context.getString(R.string.delete_video))
                    .content(context.getString(R.string.confirm) +" " + videoItem.getFileTitle() + " ?")
                    .positiveText(context.getString(R.string.confirm_delete))
                    .negativeText(context.getString(R.string.confirm_cancel))
                    .onPositive((dialog1, which) -> {
                        File deleteFile = new File(videoItem.getPath());
                        if(deleteFile.exists()){
                            if(deleteFile.delete()){
                                fileItems.remove(videoItem);
                                updateData(fileItems);
                                if(context instanceof BaseActivity){
                                    ((BaseActivity) context).updateListAfterDelete(fileItems);
                                }
                            }
                        }
                    })
                    .onNegative((dialog12, which) -> dialog12.dismiss())
                    .show();

                                break;
                            case R.id.share:


                                //  musiocoptionlistener.share(folders.get(position).getaPath());

                                break;
                            case R.id.properties:

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                View view = context.getLayoutInflater().inflate(R.layout.content_video_info, null);

                                TextView txtVideoTitle = view.findViewById(R.id.txtVideoTitle);
                                txtVideoTitle.setText(fileItems.get(position).getPath());

                                TextView txtLocation = view.findViewById(R.id.txtLocation_value);
                                txtLocation.setText(fileItems.get(position).getPath());

                                TextView txtVideoFormat = view.findViewById(R.id.txtFormat_value);
                                txtVideoFormat.setText(AppsUtils.getFileExtension(fileItems.get(position).getPath()));

                                TextView txtDuration = view.findViewById(R.id.txtDuration_value);
                                txtDuration.setText(fileItems.get(position).getPath());

                                TextView txtDateAdded = view.findViewById(R.id.txtDateAdded_value);
                                txtDateAdded.setText(fileItems.get(position).getPath());

                                TextView txtVideoSize = view.findViewById(R.id.txtFileSize_value);
                                txtVideoSize.setText(fileItems.get(position).getPath());

                                TextView txtVideoResolution = view.findViewById(R.id.txResolution_value);
                                txtVideoResolution.setText(fileItems.get(position).getPath());
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();


            }
        });
    }


    public void updateData(ArrayList<FileItem> items){
        if(items == null) items = new ArrayList<>();
        ArrayList<FileItem> r = new ArrayList<>(items);
        int currentSize = fileItems.size();
        if(currentSize != 0) {
            this.fileItems.clear();
            this.fileItems.addAll(r);
            notifyItemRangeRemoved(0, currentSize);
            //tell the recycler view how many new items we added
            notifyItemRangeInserted(0, r.size());
        }
        else {
            this.fileItems.addAll(r);
            notifyItemRangeInserted(0, r.size());
        }

    }
//public ArrayList<FileItem> updatedata(){
//
//
//        fileItems.clear();
//    String[] proj = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME , MediaStore.Audio.Media.DATA};// Can include more data for more details and check it.
//    File c = new File(Environment.getExternalStorageDirectory().getPath()+"/bluetooth/" );
//
//    Cursor audioCursor =context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
//            MediaStore.Audio.Media.DATA +" like ?",
//            new String[]{c+"%"},
//            null);
//    if (audioCursor != null) {
//        if (audioCursor.moveToFirst()) {
//            do {
//                int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
//                String fullPath = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                FileItem a = new FileItem();
//                a.setFileTitle(audioCursor.getString(audioIndex));
//                a.setPath(fullPath);
//                fileItems.add(a);
//            } while (audioCursor.moveToNext());
//        }
//    }
//    audioCursor.close();
//    return fileItems;
//}
    @Override
    public int getItemCount() {
        return fileItems.size();
    }
//    private void showBottomDialog(FileItem videoItem) {
//        View view = context.getLayoutInflater().inflate(R.layout.video_option_dialog, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(context);
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
//            RenameVideoDialog renamePlaylistDialog = RenameVideoDialog.newInstance(context,this,videoItem);
//            renamePlaylistDialog.show(((AppCompatActivity) context).getSupportFragmentManager(),"");
//            dialog.dismiss();
//        });
//        LinearLayout option_info = view.findViewById(R.id.option_info);
//        option_info.setOnClickListener(v -> {
//            dialog.dismiss();
//            createDialog(videoItem);
//        });
//        LinearLayout option_delete = view.findViewById(R.id.option_delete);
//        option_delete.setOnClickListener(v -> {
//            dialog.dismiss();
//            new MaterialDialog.Builder(context)
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
//                    .show();
//        });
//        dialog.setContentView(view);
//        dialog.show();
//    }
public int getTotalVideoSelected(){
    int totalVideoSelected = 0;
    if(fileItems == null || fileItems.size() == 0) return 0;
    for (FileItem videoItem: fileItems){
        if(videoItem.isSelected()) totalVideoSelected += 1;
    }
    return totalVideoSelected;
}
    public ArrayList<FileItem> getVideoItemsSelected(){
        ArrayList<FileItem> resultList = new ArrayList<>();
        for (FileItem videoItem: fileItems){
            if(videoItem.isSelected()) resultList.add(videoItem);
        }
        return resultList;
    }
    public void deleteListVideoSelected(){
        ArrayList<FileItem> deletedList = getVideoItemsSelected();
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.delete_video))
                .content(context.getString(R.string.confirm) +" " + String.valueOf(deletedList.size()) + " " + context.getString(R.string.video) + " ?")
                .positiveText(context.getString(R.string.confirm_delete))
                .negativeText(context.getString(R.string.confirm_cancel))
                .onPositive((dialog1, which) -> {
                    for(FileItem item :deletedList){
                        File deleteFile = new File(item.getPath());
                        if (deleteFile.exists()) {
                            if (deleteFile.delete()) {
                                fileItems.remove(item);
                            }
                        }
                    }
                    updateData(fileItems);
                })
                .onNegative((dialog12, which) -> dialog12.dismiss())
                .show();
    }

}