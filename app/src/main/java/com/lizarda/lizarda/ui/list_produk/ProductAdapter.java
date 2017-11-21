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

import butterknife.BindView;
import butterknife.ButterKnife;

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
        final Product product = mProducts.get(position);

        holder.mTvTitle.setText(product.getName());
        holder.mTvContent.setText("Rp. " + product.getPrice());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        @BindView(R.id.container_list_item)
        FrameLayout mContainer;

        @BindView(R.id.iv_thumbnail_detail_category)
        ImageView mIvThumbnail;

        @BindView(R.id.tv_title)
        TextView mTvTitle;

        @BindView(R.id.tv_content)
        TextView mTvContent;


        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);

        }
    }
}
