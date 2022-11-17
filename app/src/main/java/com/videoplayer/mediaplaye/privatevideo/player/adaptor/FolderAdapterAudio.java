package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.FolderDetails;
import com.videoplayer.mediaplaye.privatevideo.player.fragments.FragmentVideoList;
import com.videoplayer.mediaplaye.privatevideo.player.models.FolderItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;

import java.util.ArrayList;

public class FolderAdapterAudio extends RecyclerView.Adapter<FolderAdapterAudio.ItemHolder> {
    Activity context;
    private ArrayList<FolderItem> folderItems = new ArrayList<>();
    public FolderAdapterAudio(Activity context) {
        this.context = context;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if(PreferencesUtility.getInstance(context).isAlbumsInGrid())
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_grid, null);
        else
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder, null);
        return new ItemHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

        FolderItem folderItem = folderItems.get(i);
        itemHolder.folderTitle.setText(folderItem.getFolderName());
        itemHolder.folderPath.setText(folderItem.getFolderPath());
        itemHolder.folderSize.setText(String.valueOf(folderItem.getVideoItems().size()).concat(" ").concat(context.getString(R.string.video)));

        itemHolder.container.setOnClickListener(v -> {
            Mainutils.getInstance().folderItem = folderItem;
            Toast.makeText(context, ""+folderItem.getFolderName(), Toast.LENGTH_SHORT).show();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            context.startActivity(new Intent(context, FolderDetails.class));

//            FragmentVideoList myFragment = new FragmentVideoList();
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.music, myFragment).addToBackStack(null).commit();




            //Intent intent = new Intent(context, FolderDetailActivity.class);
           /// context.startActivity(intent);
           // context.overridePendingTransition(R.anim.slide_in_left,
              //      R.anim.slide_stay_x);
        });

    }
    public void updateData(ArrayList<FolderItem> items) {
        if (items == null) items = new ArrayList<>();

        int currentSize = folderItems.size();
        if(currentSize != 0) {
            this.folderItems.clear();
            this.folderItems.addAll(items);
            notifyItemRangeRemoved(0, currentSize);
            //tell the recycler view how many new items we added
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

        }

    }


}
