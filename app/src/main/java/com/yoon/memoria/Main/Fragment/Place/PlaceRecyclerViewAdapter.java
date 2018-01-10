package com.yoon.memoria.Main.Fragment.Place;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoRecyclerViewAdapter;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.PlaceItemBinding;

/**
 * Created by Yoon on 2018-01-09.
 */

public class PlaceRecyclerViewAdapter extends BaseRecyclerViewAdapter<Place,PlaceRecyclerViewAdapter.ViewHolder> implements BaseRecyclerViewAdapter.OnItemLongClickListener{

    public MainActivity activity;

    public PlaceRecyclerViewAdapter(Context context) {
        super(context);
        setOnItemLongClickListener(this);
        activity = (MainActivity)context;
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        Place place = getItem(position);
        holder.binding.setPlace(place);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new PlaceRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        PlaceItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
