package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.models.Folder;

import java.util.ArrayList;

public class FolderRecyclarAdaptor extends RecyclerView.Adapter<FolderRecyclarAdaptor.MyViewHolder> {
    ArrayList<Folder> folders;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView foldername;
        private TextView no_of_files;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foldername = itemView.findViewById(R.id.text);
            no_of_files = itemView.findViewById(R.id.num_of_file);
        }


        // each data item is just a string in this case

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FolderRecyclarAdaptor( Context context1,ArrayList<Folder> folders1) {
        this.folders = folders1;
        this.context = context1;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public FolderRecyclarAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // always use a fallback ViewHolder
        View view = layoutInflater.inflate(R.layout.folder_item, parent, false);
        return new FolderRecyclarAdaptor.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.foldername.setText(folders.get(position).getFolderName());
        //holder.no_of_files.setText(folders.get(position).getNo_of_file());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return folders.size();
    }
}