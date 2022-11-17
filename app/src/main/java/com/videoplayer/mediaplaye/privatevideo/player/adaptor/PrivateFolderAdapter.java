package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FastScroller;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PrivateFolderAdapter extends RecyclerView.Adapter<PrivateFolderAdapter.ItemHolder> implements FastScroller.BubbleTextGetter {

    Activity context;
    private List<FileItem> contactListFiltered;
    private ArrayList<FileItem> videoItems = new ArrayList<>();
    public PrivateFolderAdapter(Activity context, ArrayList<FileItem> videoItems1){
        this.context = context;
        this.contactListFiltered = videoItems1;
        this.videoItems=videoItems1;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
//            if(PreferencesUtility.getInstance(context).isAlbumsInGrid())
//             itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.private_item_video_grid, null);
//        else
             itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.private_item_video, null);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        FileItem videoItem = videoItems.get(i);
        itemHolder.title.setText(videoItem.getFileTitle());
        itemHolder.duration.setText(videoItem.getDuration());
        if(AppsUtils.getFileExtension(videoItem.getFileTitle())=="mp3"){
            Glide.with(context.getApplicationContext())
                    .load(R.drawable.musicicon)
                    .into(itemHolder.imageView);




        }else {
            Glide.with(context.getApplicationContext())
                    .load(videoItem.getPath())
                    .into(itemHolder.imageView);

        }

        itemHolder.title.setOnClickListener(v -> {
            Mainutils.getInstance().videoItemsPlaylist = videoItems;
            Mainutils.getInstance().playingVideo = videoItem;
            Mainutils.getInstance().seekPosition = 0;
                //   Equalizerclass equalizerclass= new Equalizerclass();
                //   equalizerclass.loadEqualizerSettings();
               // Mainutils.getInstance().videoService.getVideoPlayer().getAudioSessionId();
                Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);
                Intent intent = new Intent(context, PlayVideoActivity.class);
                context.startActivity(intent);
                if (Mainutils.getInstance().videoBackendService != null)
                    Mainutils.getInstance().videoBackendService.releasePlayerView();


//
//            GlobalVar.getInstance().videoItemsPlaylist = videoItems;
//            Toast.makeText(context, ""+GlobalVar.getInstance().videoItemsPlaylist, Toast.LENGTH_SHORT).show();
//            GlobalVar.getInstance().playingVideo = videoItem;
//            Toast.makeText(context, ""+GlobalVar.getInstance().playingVideo, Toast.LENGTH_SHORT).show();
//            GlobalVar.getInstance().seekPosition = 0;
//                if(!GlobalVar.getInstance().isPlayingAsPopup()){
//                    GlobalVar.getInstance().videoService.playVideo(GlobalVar.getInstance().seekPosition,false);
//                    Intent intent = new Intent(context, PlayVideoActivity.class);
//                    context.startActivity(intent);
//                    if(GlobalVar.getInstance().videoService != null)
//                        GlobalVar.getInstance().videoService.releasePlayerView();
//                }else {
//                    ((BaseActivity) context).showFloatingView(context,true);
//                }


             });



        itemHolder.imageViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("ResourceType") Context wrapper = new ContextThemeWrapper(context, R.attr.stylepop);
                PopupMenu popup = new PopupMenu(wrapper, view,R.attr.stylepop);
                popup.getMenuInflater().inflate(R.menu.private_folder_option, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {


                            case R.id.unlock:
                                File des = new File(Environment.getExternalStorageDirectory().getPath() + "/");
                                File src=new  File(videoItem.getPath());
                                src.renameTo(new File(des.getAbsolutePath()+"/"+src.getName()));

                                // moveFile( src.getAbsolutePath(), des.getAbsolutePath() );
                                videoItems.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i,videoItems.size());
                                break;
                            case R.id.delete:

                                CustomDeltedialog cdelete = new CustomDeltedialog(context, videoItem, i);
                                cdelete.show();


                                break;

                            case R.id.properties:
                                CustominfoClass custominfoClass = new CustominfoClass(context, videoItem);
                                custominfoClass.show();

                                break;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });



    }



    public class CustominfoClass extends Dialog {

        public Activity c;
        public Dialog d;
        TextView foldername;

        TextView folderpath;


        TextView foldersize;


        TextView foderdate;
        TextView format;
        TextView resolution;
        TextView lenght;


        TextView ok;
        TextView filename;

        FileItem folderItem;

        public CustominfoClass(Activity a, FileItem folderItem) {
            super(a);
            // TODO Auto-generated constructor stub
            this.folderItem = folderItem;
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.content_fille_info);
            foldername = findViewById(R.id.foldername);
            folderpath = findViewById(R.id.folderlocation);

            foldersize = findViewById(R.id.foldersize);
            format = findViewById(R.id.foldersize);
            resolution = findViewById(R.id.foldersize);
            lenght = findViewById(R.id.foldersize);


            foderdate = findViewById(R.id.folderdate);
            ok = findViewById(R.id.ok);
            filename = findViewById(R.id.filename);

            foldername.setText(folderItem.getFolderName());
            folderpath.setText("Private Folder");
            File file = new File(folderItem.getPath());

            foldersize.setText("");
            filename.setText(file.getName());
            Date lastModDate = new Date(file.lastModified());
            foderdate.setText("" + lastModDate);
            format.setText("");
            resolution.setText("");
            lenght.setText("");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }


    }
    public class CustomDeltedialog extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        FileItem folderItem;
        ImageView cross;
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
            cross =  findViewById(R.id.cross);
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
                    break; case R.id.cross:


                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
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

    public ArrayList<FileItem> getVideoItems(){
        if(videoItems == null) return new ArrayList<>();
        return videoItems;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return null;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView title, duration,txtVideoPath;

        protected ImageView imageView,imageViewOption;

        View container;

        public ItemHolder(View view) {
            super(view);
            container = view;
            this.txtVideoPath = view.findViewById(R.id.txtVideoPath);
            this.title = view.findViewById(R.id.txtVideoTitle);
            this.imageView = view.findViewById(R.id.imageView);
            this.duration = view.findViewById(R.id.txtVideoDuration);
            this.imageViewOption = view.findViewById(R.id.imageViewOption);

        }

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
public interface ContactsAdapterListener {
    void onContactSelected(FileItem contact);
}
    private void createDialog(FileItem videoItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.content_video_info,null);

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
    private boolean checkError(List<FileItem> songs, int position){
        if(songs.size() >= position && position >= 0) return true;
        return false;
    }
    public int getTotalVideoSelected(){
        int totalVideoSelected = 0;
        if(videoItems == null || videoItems.size() == 0) return 0;
        for (FileItem videoItem: videoItems){
            if(videoItem.isSelected()) totalVideoSelected += 1;
        }
        return totalVideoSelected;
    }
    public ArrayList<FileItem> getVideoItemsSelected(){
        ArrayList<FileItem> resultList = new ArrayList<>();
        for (FileItem videoItem: videoItems){
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
                                    videoItems.remove(item);
                                }
                            }
                    }
                    updateData(videoItems);
                })
                .onNegative((dialog12, which) -> dialog12.dismiss())
                .show();
    }
    public void filterList(ArrayList<FileItem> filterdNames) {
        this.videoItems = filterdNames;
        notifyDataSetChanged();
    }

}
