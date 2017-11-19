package com.lizarda.lizarda.ui.home;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizarda.lizarda.model.Model;
import com.lizarda.lizarda.R;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 10/31/17.
 */

public class HomeKategoriAdapter extends RecyclerView.Adapter<HomeKategoriAdapter.ViewHolder> {

    private ArrayList<Model> mModels;
    private HomeKategoriCallback mCallback;

    public HomeKategoriAdapter(ArrayList<Model> models, HomeKategoriCallback callback) {
        mModels = models;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_kategori_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onKategoriClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mIvDivisi;
        public TextView mTvNamaDivisi;

        public FrameLayout mContainerLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContainerLayout = view.findViewById(R.id.container_kategori_home);
//            mIvDivisi = (ImageView) view.findViewById(R.id.iv_divisi_jadwal);
//            mTvNamaDivisi = (TextView) view.findViewById(R.id.tv_nama_divisi_jadwal);
        }
    }

}
