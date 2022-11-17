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

import java.util.ArrayList;

import static com.videoplayer.mediaplaye.privatevideo.player.activities.SelectVideoAudio.songsList;

public class FileselectionAdaptor extends RecyclerView.Adapter<FileselectionAdaptor.MyViewHolder> {
    private ArrayList<MusicSelection> folders;
    Context context;
    int checkAccumulator;

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        private TextView audioName;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.filename);
            checkBox = itemView.findViewById(R.id.checkbox);
//            checkBox.setChecked(false);
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView,
//                                             boolean isChecked) {
//                    if (isChecked) {
////                        Toast.makeText(ProductFilterRecyclerViewAdapter.this.context,
////                                "selected brand is " + brandName.getText(),
////                                Toast.LENGTH_LONG).show();
//                    } else {
//
//                    }
//                }
//            });
           }



    }

    public FileselectionAdaptor(Context context1, ArrayList<MusicSelection> folders1) {
        this.folders = folders1;
        this.context = context1;
        checkAccumulator = 0;

    }

    @Override
    public FileselectionAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fileselection_item, parent, false);
        return new FileselectionAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.audioName.setText(folders.get(position).getaName());
        holder.checkBox.setChecked(folders.get(position).getSelected());
        holder.checkBox.setTag(position);
//songsList.remove(position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                Integer pos = (Integer) holder.checkBox.getTag();
                //  Toast.makeText(ctx, imageModelArrayList.get(pos).getAnimal() + " clicked!", Toast.LENGTH_SHORT).show();
                if (folders.get(pos).getSelected()) {
                    folders.get(pos).setSelected(false);
                    checkAccumulator = checkAccumulator - 1;
                    songsList.remove(folders.remove(position));

                    if (checkAccumulator < 1) {
                       // layout1.layoutgone();
                    }
                    else
                    {
                     //   layout1.layout();
                    }
                } else {
                    checkAccumulator = checkAccumulator + 1;
                    songsList.add(folders.get(position).getaName());
                   // selectedImage.add(arrayList.get(position));
                    folders.get(pos).setSelected(true);
                    //layout1.layout();
                }
                Toast.makeText(context, ""+songsList.toString(), Toast.LENGTH_SHORT).show();

            }



        });

  }





    @Override
    public int getItemCount() {
        return folders.size();
    }
}