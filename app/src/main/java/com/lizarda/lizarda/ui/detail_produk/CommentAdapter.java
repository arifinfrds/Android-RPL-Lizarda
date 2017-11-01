package com.lizarda.lizarda.ui.detail_produk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Model;

import java.util.ArrayList;


/**
 * Created by arifinfrds on 11/1/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Model> mModels;
    private Context mContext;

    public CommentAdapter(ArrayList<Model> mModels, Context mContext) {
        this.mModels = mModels;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_comment_detail_produk, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mIvDivisi;
        public TextView mTvNamaDivisi;


        public ViewHolder(View view) {
            super(view);
            mView = view;

        }
    }
}
