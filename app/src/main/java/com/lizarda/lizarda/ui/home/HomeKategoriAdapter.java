package com.lizarda.lizarda.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizarda.lizarda.Const;
import com.lizarda.lizarda.model.Kategori;
import com.lizarda.lizarda.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arifinfrds on 10/31/17.
 */

public class HomeKategoriAdapter extends RecyclerView.Adapter<HomeKategoriAdapter.ViewHolder> {

    private ArrayList<Kategori> mCategories;
    private HomeKategoriCallback mCallback;
    private Context mContext;

    public HomeKategoriAdapter(ArrayList<Kategori> categories, HomeKategoriCallback callback,
                               Context context) {
        mCategories = categories;
        mCallback = callback;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_kategori_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(mContext).load(mCategories.get(position).getImageResId()).into(holder.mIvKategori);
        holder.mTvNamaKategori.setText(mCategories.get(position).getNama());

        holder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onKategoriClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_home)
        ImageView mIvKategori;

        @BindView(R.id.tv_nama_kategori_item_home)
        TextView mTvNamaKategori;

        @BindView(R.id.container_kategori_home)
        FrameLayout mContainerLayout;

        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, itemView);

        }
    }

}
