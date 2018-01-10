package com.yoon.memoria.History;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Main.Fragment.Place.PlaceRecyclerViewAdapter;
import com.yoon.memoria.Main.MainActivity;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.R;
import com.yoon.memoria.databinding.PlaceItemBinding;

/**
 * Created by Yoon on 2018-01-10.
 */

public class HistoryRecyclerViewAdapter extends BaseRecyclerViewAdapter<Place,HistoryRecyclerViewAdapter.ViewHolder>{

    public HistoryRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(HistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        Place place = getItem(position);
        holder.binding.setPlace(place);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new HistoryRecyclerViewAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        PlaceItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
