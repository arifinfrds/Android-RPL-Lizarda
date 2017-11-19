package com.lizarda.lizarda.ui.list_produk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Product;

import java.util.ArrayList;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    private ListProdukCallback mCallback;

    public ProductAdapter(ArrayList<Product> products, ListProdukCallback callback) {
        this.mProducts = products;
        this.mCallback = callback;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_category, parent, false);
        return new ProductAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mIvThumbnail;
        public TextView mTvTitle;
        public TextView mTvDetail;

        public FrameLayout mContainer;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContainer = view.findViewById(R.id.container_list_item);

            mIvThumbnail = view.findViewById(R.id.iv_thumbnail_detail_category);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvDetail = view.findViewById(R.id.tv_description_detail);

        }
    }
}
