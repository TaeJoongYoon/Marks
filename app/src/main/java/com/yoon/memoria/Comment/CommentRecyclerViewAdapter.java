package com.yoon.memoria.Comment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yoon.memoria.Custom.BaseRecyclerViewAdapter;
import com.yoon.memoria.Model.Comment;
import com.yoon.memoria.databinding.CommentItemBinding;

/**
 * Created by Yoon on 2018-01-12.
 */

public class CommentRecyclerViewAdapter extends BaseRecyclerViewAdapter<Comment,CommentRecyclerViewAdapter.ViewHolder> implements BaseRecyclerViewAdapter.OnItemClickListener, BaseRecyclerViewAdapter.OnItemLongClickListener{

    private CommentContract.View view;

    public CommentRecyclerViewAdapter(Context context, CommentContract.View view) {
        super(context);
        this.view = view;
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        Comment comment = getItem(position);
        holder.binding.setComment(comment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onItemClick(View view, int position) {
        this.view.toUser(getItem(position).getUid());
    }

    @Override
    public void onItemLongClick(View view, int position) {
        this.view.delete(getItem(position).getUid(), getItem(position).getCommentUid());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CommentItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
