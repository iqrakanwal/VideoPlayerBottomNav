package com.videoplayer.mediaplaye.privatevideo.player.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.videoplayer.mediaplaye.privatevideo.player.R;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    ArrayList mValues;
    Context mContext;
    protected ItemListener mListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        DataModel item;


        public MyViewHolder(View view) {
            super(view);
            //thumbnail = view.findViewById(R.id.thumbnail);
//            view.setOnClickListener((View.OnClickListener) mContext);
            textView = (TextView) view.findViewById(R.id.textView);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);

        }
    }

    public RecyclerViewAdapter(Context context, ArrayList values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        DataModel item = (DataModel) mValues.get(position);

        holder.textView.setText(item.text);

       // holder.imageView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, item.drawable) );

      //  holder.imageView.setImageResource(item.drawable);
        holder.relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, item.drawable) );



    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}

