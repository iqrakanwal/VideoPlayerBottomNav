package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.videoplayer.mediaplaye.privatevideo.player.R;
import com.videoplayer.mediaplaye.privatevideo.player.models.ThemeChoiceItem;
import com.videoplayer.mediaplaye.privatevideo.player.utills.PreferencesUtility;
import net.steamcrafted.materialiconlib.MaterialIconView;
import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ItemHolder> {
    Activity context;
    PreferencesUtility preferencesUtility;
    ArrayList<DataModel> themeChoiceItems = new ArrayList<>();
    int choiceId = 0;

    public ThemeAdapter(Activity context){
        this.context = context;
        preferencesUtility = new PreferencesUtility(context);
        preferencesUtility = new PreferencesUtility(context);
        choiceId = preferencesUtility.getThemeSettings();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, null);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
       final DataModel themeChoiceItem = themeChoiceItems.get(i);
        DataModel item = (DataModel) themeChoiceItems.get(i);

        itemHolder.textView.setText(item.color);

        // holder.imageView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, item.drawable) );

        //  holder.imageView.setImageResource(item.drawable);
        itemHolder.relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, item.drawable) );

        itemHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceId = themeChoiceItem.getId();
                ThemeAdapter.this.updateData(themeChoiceItems);
                preferencesUtility.setThemSettings(themeChoiceItem.getId());
                Toast.makeText(context, ""+themeChoiceItem.getId(), Toast.LENGTH_SHORT).show();


            }
        });

      //  itemHolder.textView.setText(themeChoiceItems.get(i).getText().toString());
        //itemHolder.layoutContainer.setBackgroundColor(ContextCompat.getColor(context,themeChoiceItem.getColor()));
//itemHolder.imageView.setColorResource(ContextCompat.getColor(context,themeChoiceItem.getColor()));
//        itemHolder.layoutContainer.setBackgroundColor(ContextCompat.getColor(context,themeChoiceItem.getColor()));
//        itemHolder.imageView.setColor(themeChoiceItem.getId() == choiceId ? Color.GREEN : Color.WHITE);
//        itemHolder.imageView.setVisibility(themeChoiceItem.getId() == choiceId ? View.VISIBLE:View.INVISIBLE);

//        itemHolder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // choiceId = themeChoiceItem.getId();
//               ThemeChoiceItemAdapter.this.updateData(themeChoiceItems);
//               // preferencesUtility.setThemSettings(themeChoiceItem.getId());
//            }
//        });

    }
    public void updateData(ArrayList<DataModel> items){

        if(items == null) items = new ArrayList<>();
        ArrayList<DataModel> r = new ArrayList<>(items);
        int currentSize = themeChoiceItems.size();
        if(currentSize != 0) {
            this.themeChoiceItems.clear();
            this.themeChoiceItems.addAll(r);
            notifyDataSetChanged();
        }
        else {
            this.themeChoiceItems.addAll(r);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return themeChoiceItems.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        DataModel item;

        public ItemHolder(View view) {
            super(view);
            //thumbnail = view.findViewById(R.id.thumbnail);
//            view.setOnClickListener((View.OnClickListener) mContext);
            textView = (TextView) view.findViewById(R.id.textView);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        }
    }
}