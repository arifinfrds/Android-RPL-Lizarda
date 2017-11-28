package com.lizarda.lizarda.ui.detail_produk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Comment;
import com.lizarda.lizarda.model.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.lizarda.lizarda.model.Product;
import com.lizarda.lizarda.model.User;
import com.squareup.picasso.Picasso;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_COMMENT;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_POPULARITY_COUNT;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_USER;
import static com.lizarda.lizarda.Const.KEY_PRODUCT_ID;
import static com.lizarda.lizarda.Const.NOT_SET;


public class DetailProdukActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.rv_comment_detail_produk)
    RecyclerView mRvComment;

    @BindView(R.id.et_comment_detail_produk)
    EditText mEtComment;

    @BindView(R.id.btn_send_comment_detail_produk)
    Button mBtnSendComment;

    @BindView(R.id.tv_profile_detail_produk)
    TextView mTvOwnerProduk;

    @BindView(R.id.tv_nama_detail_produk)
    TextView mTvNamaProduk;

    @BindView(R.id.tv_jenis_detail_produk)
    TextView mTvJenisProduk;

    @BindView(R.id.tv_description_detail_produk)
    TextView mTvDeskripsiProduk;

    @BindView(R.id.tv_harga_detail_produk)
    TextView mTvHargaProduk;

    @BindView(R.id.iv_produk_detail_produk)
    ImageView mIvProduk;

    private Bundle mExtras;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    private Product mProduct;

    private String mProductId;

    private ArrayList<Comment> mComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupFirebase();

        mComments = new ArrayList<>();

        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mProductId = mExtras.getString(KEY_PRODUCT_ID);
            fetchProductWithId(mProductId);
            fetchCommentWithProductId(mProductId);
        }

        ButterKnife.bind(this);

        mBtnSendComment.setOnClickListener(this);

        mToolbarLayout.setTitle("Green Iguana");

        setupRecyclerView(mRvComment);
    }

    private ArrayList<User> mUsers = new ArrayList<>();

    private void fetchCommentWithProductId(String productId) {
        mDatabaseRef.child(CHILD_COMMENT).child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mComments.isEmpty()) {
                    mComments.clear();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot commentDataSnapshot : dataSnapshot.getChildren()) {

                        if (commentDataSnapshot.exists()) {
                            Comment comment = commentDataSnapshot.getValue(Comment.class);
                            Log.d(CHILD_COMMENT, "onDataChange: ");
                            fetchEmailUsers();
                            mComments.add(comment);
                        }
                        setupRecyclerView(mRvComment);
                    }
                    fetchCommentUsername(mComments);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchEmailUsers() {
        mDatabaseRef.child(CHILD_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    mUsers.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchCommentUsername(final ArrayList<Comment> comments) {
        final ArrayList<User> mUsers = new ArrayList<>();

        mDatabaseRef.child(CHILD_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    mUsers.add(user);
                }

                // TODO: 11/27/17 fetch email / username tiap2 user yang comment
//                for (Comment comment : comments) {
//                    // comment.getCommentOwnerId();
//
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void fetchProductWithId(final String productId) {
        mDatabaseRef.child(CHILD_PRODUCT).child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                fetchUserNameWithProduct(product);
                mProduct = product;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updatePopularityCountProductWithProductId(String productId, int newCount) {
        mDatabaseRef.child(CHILD_PRODUCT).child(productId).child(CHILD_POPULARITY_COUNT).setValue(newCount);
    }

    private void fetchUserNameWithProduct(final Product product) {
        final String[] id = {""};
        mDatabaseRef.child(CHILD_USER).child(product.getOwnerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    updateUI(product, user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(Product product, String email) {
        mToolbarLayout.setTitle(product.getName());
        mTvOwnerProduk.setText(email);
        mTvNamaProduk.setText(product.getName());
        mTvJenisProduk.setText(product.getCategory());
        mTvDeskripsiProduk.setText(product.getDescription());
        mTvHargaProduk.setText("Rp. " + product.getPrice());

        if (!product.getPhotoUrl().equalsIgnoreCase(NOT_SET)) {
            Picasso.with(this).load(product.getPhotoUrl()).into(mIvProduk);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(
                new CommentAdapter(
                        mComments,
                        this
                )
        );
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_send_comment_detail_produk) {
            if (mEtComment.getText().toString().matches("")) {
                Toast.makeText(
                        this,
                        "Input komentar tidak boleh kosong.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                uploadComment(mEtComment.getText().toString());
            }
        }
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
    }

    // MARK: - kodingan upload ke fireabse
    private void uploadComment(String message) {

        if (mProductId != null) {
            String commentId = mDatabaseRef.push().getKey();
            Comment comment = new Comment(
                    commentId,
                    mUser.getUid(),
                    message,
                    new Date()
            );
            mDatabaseRef.child(CHILD_COMMENT).child(mProductId).child(commentId).setValue(comment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(CHILD_COMMENT, "onSuccess: ");
                            Toast.makeText(
                                    DetailProdukActivity.this,
                                    "Pesan tertambah.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(CHILD_COMMENT, "onFailure: ");
                            Toast.makeText(
                                    DetailProdukActivity.this,
                                    "Terjadi kesalahan. Pesan gagal tertambah.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
            mEtComment.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_produk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                performShare();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProduct != null) {
            updatePopularityCountProductWithProductId(
                    mProduct.getId(),
                    mProduct.getPopularityCount() + 1
            );
        }
    }

    private void performShare() {
        Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
    }
}
