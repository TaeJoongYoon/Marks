package com.yoon.memoria.FollowList;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Model.User;
import com.yoon.memoria.R;
import com.yoon.memoria.Reading.ReadingActivity;
import com.yoon.memoria.databinding.FollowItemBinding;

/**
 * Created by Yoon on 2018-01-07.
 */

public class FollowRecyclerViewAdapter extends BaseRecyclerViewAdapter<User,FollowRecyclerViewAdapter.UserViewHolder> implements BaseRecyclerViewAdapter.OnItemClickListener {


    public FollowRecyclerViewAdapter(Context context){
        super(context);
        setOnItemClickListener(this);
    }

    @Override
    public void onBindView(UserViewHolder holder, int position) {
        User user = getItem(position);
        holder.binding.setUser(user);
    }

    @Override
    public FollowRecyclerViewAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ReadingActivity.class);
        intent.putExtra("Uid",getItem(position).getUid());
        getContext().startActivity(intent);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        FollowItemBinding binding;
        public UserViewHolder(View itemView){
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
