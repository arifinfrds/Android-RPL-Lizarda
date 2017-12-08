package com.lizarda.lizarda.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Product;
import com.lizarda.lizarda.ui.admin.AdminAdapter;
import com.lizarda.lizarda.ui.detail_produk.DetailProdukActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.KEY_PRODUCT_ID;
import static com.lizarda.lizarda.Const.NOT_SET;

/**
 * Created by arifinfrds on 12/8/17.
 */

public class ProfileProductAdapter extends RecyclerView.Adapter<ProfileProductAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    private Context mContext;

    public ProfileProductAdapter(ArrayList<Product> mProducts, Context mContext) {
        this.mProducts = mProducts;
        this.mContext = mContext;
    }

    @Override
    public ProfileProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_profile, parent, false);
        return new ProfileProductAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileProductAdapter.ViewHolder holder, int position) {
        final Product product = mProducts.get(position);

        holder.mTvTitle.setText(product.getName());
        holder.mTvHarga.setText("Rp. " + product.getPrice());

        if (product.isSold()) {
            holder.mTvHarga.setText(mContext.getResources().getString(R.string.status_sold));
        } else {
            holder.mTvHarga.setText("Rp. " + product.getPrice());
        }

        if (product.getPhotoUrl() != null) {
            if (product.getPhotoUrl().equalsIgnoreCase(NOT_SET)) {
                Picasso.with(mContext).load(R.drawable.no_image)
                        .resize(70, 70).into(holder.mIvThumbnail);
            } else {
                Picasso.with(mContext).load(product.getPhotoUrl())
                        .resize(70, 70).into(holder.mIvThumbnail);
            }
        } else {
            Picasso.with(mContext).load(R.drawable.no_image)
                    .resize(70, 70).into(holder.mIvThumbnail);
        }

        holder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToEditProductActivity(product.getId());
            }
        });
    }

    private void navigateToEditProductActivity(String productId) {
//        Intent intent = new Intent(mContext, DetailProdukActivity.class);
//        intent.putExtra(KEY_PRODUCT_ID, productId);
//        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_profile)
        ImageView mIvThumbnail;

        @BindView(R.id.tv_title)
        TextView mTvTitle;

        @BindView(R.id.tv_harga)
        TextView mTvHarga;

        @BindView(R.id.frameLayout_item_suggest_container)
        FrameLayout mContainerLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
