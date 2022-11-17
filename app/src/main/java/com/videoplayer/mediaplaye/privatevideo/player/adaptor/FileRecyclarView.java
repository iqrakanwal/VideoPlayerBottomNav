package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileModel;


import java.util.ArrayList;

public class FileRecyclarView extends RecyclerView.Adapter<FileRecyclarView.MyViewHolder> {
    ArrayList<FileModel> arrayList= new ArrayList<FileModel>();
    public static ArrayList<FileModel> selectedImage = new ArrayList<FileModel>();
    int checkAccumulator;

    Context c;
    public FileRecyclarView(Context c1, ArrayList<FileModel> arrayList) {

        this.c= c1;

        checkAccumulator = 0;

        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FileRecyclarView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_model_item, parent, false);

        return new FileRecyclarView.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull final FileRecyclarView.MyViewHolder holder,final int position) {
        holder.setIsRecyclable(false);

        holder.fileDisplayName.setText(arrayList.get(position).getFileDisplayName());
        holder.chk.setChecked(arrayList.get(position).getSelected());
        holder.chk.setTag(position);
        holder.fileSize.setText(arrayList.get(position).getSize()+" MB");


holder.fileDate.setText(arrayList.get(position).getDate());
        Glide
                .with(c)
                .load(arrayList.get(position).getFilePath())
                .centerCrop()

                .into(holder.fileDisplay);
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {



            }

        });
       /* holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CheckBox chk = (CheckBox) v;

                if (chk.isChecked()) {

                    selectedImage.add(arrayList.get(position));


                } else if (!chk.isChecked()) {

                    selectedImage.remove(arrayList.remove(position));


                }
            }
        });

*/
    }
    private void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
    }

    @Override
    public long getItemId(final int position) {


        return super.getItemId(position);
    }


    @Override
    public void onViewRecycled(@NonNull FileRecyclarView.MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView fileDisplayName, fileSize;
        public ImageView fileDisplay;
        public TextView fileDate;
        CheckBox chk;

        public MyViewHolder(View view) {
            super(view);
            fileDisplayName = (TextView) view.findViewById(R.id.file_display_name);
            fileSize = (TextView) view.findViewById(R.id.file_size);
            fileDisplay = (ImageView) view.findViewById(R.id.file_display);
            fileDate =  view.findViewById(R.id.file_date);
            chk = view.findViewById(R.id.checkbox);
        }

    }
}