package com.lizarda.lizarda.ui.detail_produk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Comment;
import com.lizarda.lizarda.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_USER;
import static com.lizarda.lizarda.Const.NOT_SET;


/**
 * Created by arifinfrds on 11/1/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> mComments;
    private Context mContext;
    private DatabaseReference mDatabaseRef;

    public CommentAdapter(ArrayList<Comment> comments, Context context, DatabaseReference databaseRef) {
        this.mComments = comments;
        this.mContext = context;
        this.mDatabaseRef = databaseRef;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_comment_detail_produk, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.mTvUsername.setText(comment.getCommentOwnerId());
        holder.mTvComment.setText(comment.getText());

        mDatabaseRef.child(CHILD_USER).child(comment.getCommentOwnerId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        holder.mTvUsername.setText(user.getEmail());

                        if (user.getPhotoUrl() != null) {
                            if (user.getPhotoUrl().equals("")
                                    || user.getPhotoUrl().equalsIgnoreCase(NOT_SET)
                                    || user.getPhotoUrl().contains(NOT_SET)) {
                                Picasso.with(mContext).load(R.drawable.profile_thumbnail)
                                        .resize(24, 24).into(holder.mCivProfile);
                            } else {
                                Picasso.with(mContext).load(user.getPhotoUrl())
                                        .resize(24, 24).into(holder.mCivProfile);
                            }
                        } else {
                            Picasso.with(mContext).load(R.drawable.profile_thumbnail)
                                    .resize(24, 24).into(holder.mCivProfile);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        @BindView(R.id.iv_user_comment_detail_produk)
        CircleImageView mCivProfile;

        @BindView(R.id.tv_username_comment_detail_produk)
        TextView mTvUsername;

        @BindView(R.id.tv_detail_comment_detail_produk)
        TextView mTvComment;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
        }
    }
}
