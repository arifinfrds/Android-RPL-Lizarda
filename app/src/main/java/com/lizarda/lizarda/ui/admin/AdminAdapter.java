package com.lizarda.lizarda.ui.admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.NOT_SET;

/**
 * Created by arifinfrds on 12/8/17.
 */

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    private Context mContext;
    private AdminCallback mCallback;


    public AdminAdapter(ArrayList<Product> mProducts, Context mContext, AdminCallback mCallback) {
        this.mProducts = mProducts;
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin, parent, false);
        return new AdminAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdminAdapter.ViewHolder holder, final int position) {

        final Product product = mProducts.get(position);

        holder.mTvTitle.setText(product.getName());
        holder.mTvContent.setText(product.getOwnerId());

        if (product.getPhotoUrl() != null) {
            if (product.getPhotoUrl().equalsIgnoreCase(NOT_SET)) {
                Picasso.with(mContext)
                        .load(R.drawable.no_image)
                        .resize(70, 70)
                        .into(holder.mIvThumbnail);
            } else {
                Picasso.with(mContext)
                        .load(product.getPhotoUrl())
                        .resize(70, 70)
                        .into(holder.mIvThumbnail);
            }
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.no_image)
                    .resize(70, 70)
                    .into(holder.mIvThumbnail);
        }


        holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onDeleteProduct(product.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_thumbnail_item_admin)
        ImageView mIvThumbnail;

        @BindView(R.id.tv_title_item_admin)
        TextView mTvTitle;

        @BindView(R.id.tv_content_item_admin)
        TextView mTvContent;

        @BindView(R.id.btn_delete_item_admin)
        Button mBtnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
