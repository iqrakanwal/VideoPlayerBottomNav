package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.models.MusicSelection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.videoplayer.mediaplaye.privatevideo.player.activities.FavoriteActivity.favoritesongsList;
import static com.videoplayer.mediaplaye.privatevideo.player.activities.SelectVideoAudio.songsList;

public class FileselectionAdaptorfors extends RecyclerView.Adapter<FileselectionAdaptorfors.MyViewHolder> {
    private List<MusicSelection> folders;
    Context context;
    int checkAccumulator;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView audioName;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.filename);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

    }

    public FileselectionAdaptorfors(Context context1, List<MusicSelection> folders1) {
        this.folders = folders1;
        this.context = context1;
        checkAccumulator = 0;

    }

    @Override
    public FileselectionAdaptorfors.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fileselection_item, parent, false);
        return new FileselectionAdaptorfors.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        File file= new File(folders.get(position).getaName());
        file.getAbsolutePath();
        holder.audioName.setText(file.getName());
        holder.checkBox.setChecked(folders.get(position).getSelected());
        holder.checkBox.setTag(folders.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    MusicSelection fruits1 = (MusicSelection) holder.checkBox.getTag();
                    fruits1.setSelected(holder.checkBox.isChecked());
                    folders.get(position).setSelected(holder.checkBox.isChecked());
                    songsList.add(folders.get(position).getaName());
                    favoritesongsList.add(folders.get(position).getaName());

                } else if (!isChecked) {
                    songsList.remove(folders.get(position).getaName());
                    favoritesongsList.remove(folders.get(position).getaName());

                }


            }
        });




}


    @Override
    public int getItemCount() {
        return folders.size();
    }
}