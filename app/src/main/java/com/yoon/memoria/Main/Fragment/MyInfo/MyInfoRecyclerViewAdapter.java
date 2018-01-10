package com.yoon.memoria.Main.Fragment.MyInfo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.R;
import com.yoon.memoria.Reading.ReadingActivity;
import com.yoon.memoria.User.UserRecyclerViewAdapter;
import com.yoon.memoria.databinding.MyinfoItemBinding;

/**
 * Created by Yoon on 2018-01-09.
 */

public class MyInfoRecyclerViewAdapter extends BaseRecyclerViewAdapter<Post,MyInfoRecyclerViewAdapter.ViewHolder> implements BaseRecyclerViewAdapter.OnItemClickListener{

    public MyInfoRecyclerViewAdapter(Context context) {
        super(context);
        setOnItemClickListener(this);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        Post post = getItem(position);
        holder.binding.setPost(post);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myinfo_item, parent, false);
        return new MyInfoRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ReadingActivity.class);
        intent.putExtra("Uid",getItem(position).getPostUid());
        getContext().startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MyinfoItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
