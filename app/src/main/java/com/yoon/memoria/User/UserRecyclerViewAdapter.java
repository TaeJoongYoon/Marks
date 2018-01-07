package com.yoon.memoria.User;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Model.Post;
import com.yoon.memoria.R;
import com.yoon.memoria.Reading.ReadingActivity;
import com.yoon.memoria.databinding.UserItemBinding;

import java.util.List;

/**
 * Created by Yoon on 2018-01-07.
 */

public class UserRecyclerViewAdapter extends BaseRecyclerViewAdapter<Post,UserRecyclerViewAdapter.PostViewHolder> implements BaseRecyclerViewAdapter.OnItemClickListener {

    public UserRecyclerViewAdapter(Context context){
        super(context);
        setOnItemClickListener(this);
    }

    @Override
    public UserRecyclerViewAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindView(PostViewHolder holder, int position) {
        Post post = getItem(position);
        holder.binding.setPost(post);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ReadingActivity.class);
        intent.putExtra("Uid",getItem(position).getPostUid());
        getContext().startActivity(intent);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        UserItemBinding binding;
        public PostViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
