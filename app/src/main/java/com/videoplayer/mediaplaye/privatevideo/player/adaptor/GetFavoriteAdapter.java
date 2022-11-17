package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.videoplayer.mediaplaye.privatevideo.player.Mainutils;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.activities.PlayVideoActivity;
import com.videoplayer.mediaplaye.privatevideo.player.database.PlaylistDatabase;
import com.videoplayer.mediaplaye.privatevideo.player.models.Favorite;
import com.videoplayer.mediaplaye.privatevideo.player.models.FileItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.AppsUtils;

import java.io.File;
import java.util.ArrayList;

public class GetFavoriteAdapter extends RecyclerView.Adapter<GetFavoriteAdapter.ItemHolder> {

    Activity context;
    private ArrayList<Favorite> favoriteArrayList = new ArrayList<>();
    PlaylistDatabase playlistDatabase;

    public GetFavoriteAdapter(Activity context, ArrayList<Favorite> favoriteArrayList1) {
        this.context = context;
        this.favoriteArrayList = favoriteArrayList1;
        playlistDatabase = PlaylistDatabase.getInstance(context);

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_favorite, null);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        File file = new File(favoriteArrayList.get(i).getSongPath());
        itemHolder.songname.setText(file.getName());
        FileItem fileItem = new FileItem();
        fileItem.setFileTitle(file.getName());
        fileItem.setPath(file.getAbsolutePath());

        itemHolder.songname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Mainutils.getInstance().playingVideo = fileItem;
                Mainutils.getInstance().videoBackendService.playVideo(Mainutils.getInstance().seekPosition, false);
                Intent intent = new Intent(context, PlayVideoActivity.class);
                context.startActivity(intent);

            }

        });

        itemHolder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.favirote_option, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.delete:

                                new MaterialDialog.Builder(context)
                                        .title(context.getString(R.string.delete_video))
                                        .content(context.getString(R.string.confirm) + " " + fileItem.getFileTitle() + " ?")
                                        .positiveText(context.getString(R.string.confirm_delete))
                                        .negativeText(context.getString(R.string.confirm_cancel))
                                        .onPositive((dialog1, which) -> {
                                            Favorite favorite=new Favorite();
                                            favorite.setSongPath(fileItem.getPath());

                                            playlistDatabase.getFavorite().delete(favorite);

                                                    favoriteArrayList.remove(fileItem);
                                                  //  updateData(favoriteArrayList);

                                            favoriteArrayList.remove(favoriteArrayList.get(i));
                                            notifyItemRemoved(i);
                                            notifyItemRangeChanged(i, favoriteArrayList.size());

                                        })
                                        .onNegative((dialog12, which) -> dialog12.dismiss())
                                        .show();

                                break;
                            case R.id.share:
                                context.startActivity(Intent.createChooser(AppsUtils.shareVideo(context, fileItem),
                                        context.getString(R.string.action_share)));


                                break;

                        }
                        return false;
                    }
                });
                popup.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView songname;
        protected ImageView options;
        View container;

        public ItemHolder(View view) {
            super(view);
            container = view;
            this.songname = view.findViewById(R.id.songname);
            this.options = view.findViewById(R.id.options);

        }

    }
    public void updateData(ArrayList<Favorite> items) {
        if (items == null) items = new ArrayList<>();
        ArrayList<Favorite> r = new ArrayList<>(items);
        int currentSize = favoriteArrayList.size();
        if (currentSize != 0) {
            this.favoriteArrayList.clear();
            this.favoriteArrayList.addAll(r);
            notifyItemRangeRemoved(0, currentSize);
            //tell the recycler view how many new items we added
            notifyItemRangeInserted(0, r.size());
        } else {
            this.favoriteArrayList.addAll(r);
            notifyItemRangeInserted(0, r.size());
        }

    }

}
