package com.lizarda.lizarda.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Intent;

import com.lizarda.lizarda.ui.detail_produk.DetailProdukActivity;
import com.lizarda.lizarda.model.Model;
import com.lizarda.lizarda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arifinfrds on 10/31/17.
 */

public class HomeSuggestAdapter extends RecyclerView.Adapter<HomeSuggestAdapter.ViewHolder> {

    private ArrayList<Model> mModels;
    private Context mContext;

    public HomeSuggestAdapter(ArrayList<Model> models, Context context) {
        mModels = models;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 navigateToDetailProdukActivity();
            }
        });
    }


    private void navigateToDetailProdukActivity() {
        Intent intent = new Intent(mContext, DetailProdukActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mIvDivisi;
        public TextView mTvNamaDivisi;

        @BindView(R.id.frameLayout_item_suggest_container)
        FrameLayout mContainerLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
             ButterKnife.bind(this, view);
//            mIvDivisi = (ImageView) view.findViewById(R.id.iv_divisi_jadwal);
//            mTvNamaDivisi = (TextView) view.findViewById(R.id.tv_nama_divisi_jadwal);
        }
    }

}
