package com.lizarda.lizarda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 10/31/17.
 */

public class HomeNewListingAdapter extends RecyclerView.Adapter<HomeNewListingAdapter.ViewHolder> {

    private ArrayList<Model> mModels;

    public HomeNewListingAdapter(ArrayList<Model> models) {
        mModels = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_home, parent, false);

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
//            mIvDivisi = (ImageView) view.findViewById(R.id.iv_divisi_jadwal);
//            mTvNamaDivisi = (TextView) view.findViewById(R.id.tv_nama_divisi_jadwal);
        }
    }

}
