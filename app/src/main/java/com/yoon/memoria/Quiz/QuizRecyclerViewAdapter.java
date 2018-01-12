package com.yoon.memoria.Quiz;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Main.Fragment.Place.PlaceRecyclerViewAdapter;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.R;
import com.yoon.memoria.databinding.PlaceItemBinding;
import com.yoon.memoria.databinding.QuizItemBinding;

/**
 * Created by Yoon on 2018-01-12.
 */

public class QuizRecyclerViewAdapter extends BaseRecyclerViewAdapter<Place,QuizRecyclerViewAdapter.ViewHolder> implements BaseRecyclerViewAdapter.OnItemClickListener {

    private QuizContract.View view;

    public QuizRecyclerViewAdapter(Context context, QuizContract.View view) {
        super(context);
        this.view = view;
        setOnItemClickListener(this);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        Place place = getItem(position);
        holder.binding.setPlace(place);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new QuizRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onItemClick(View view, int position) {
          this.view.check(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        QuizItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
