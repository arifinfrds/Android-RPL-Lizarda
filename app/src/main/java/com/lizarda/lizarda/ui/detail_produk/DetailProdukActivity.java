package com.lizarda.lizarda.ui.detail_produk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Comment;

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
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_SALDO;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_USER;
import static com.lizarda.lizarda.Const.KEY_PRODUCT_ID;
import static com.lizarda.lizarda.Const.NOT_SET;
import static com.lizarda.lizarda.Const.TAG.TAG_BUY_PRODUCT;
import static com.lizarda.lizarda.Const.TAG.TAG_DETAIL_PRODUK_UI;


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

    @BindView(R.id.tv_harga_or_status_detail_produk)
    TextView mTvHargaOrStatusProduk;

    @BindView(R.id.iv_produk_detail_produk)
    ImageView mIvProduk;

    @BindView(R.id.nested_scroll_view_detail_produk)
    NestedScrollView mNestedScrollView;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.coordinator_layout_detail_produk)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.btn_buy_detail_produk)
    Button mBtnBuyProduk;

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

        // Binding UI component
        ButterKnife.bind(this);

        // sembunyikan keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // scoll ke paling atas
        scollToTop();

        setupEditTextComment();

        // setup Firebase API
        setupFirebase();

        mComments = new ArrayList<>();

        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mProductId = mExtras.getString(KEY_PRODUCT_ID);
            fetchProductWithId(mProductId);
            fetchCommentWithProductId(mProductId);
        }

        mBtnSendComment.setOnClickListener(this);
        mBtnBuyProduk.setOnClickListener(this);

        mToolbarLayout.setTitle("Green Iguana");

        setupRecyclerView(mRvComment);
    }


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
                            mComments.add(comment);
                        }
                        setupRecyclerView(mRvComment);
                    }
                }
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

        if (product.isSold()) {
            mTvHargaOrStatusProduk.setText(getResources().getString(R.string.status_sold));
            mBtnBuyProduk.setVisibility(View.GONE);
            mBtnBuyProduk.setClickable(false);
        } else {
            mTvHargaOrStatusProduk.setText("Rp. " + product.getPrice());
            mBtnBuyProduk.setVisibility(View.VISIBLE);
            mBtnBuyProduk.setClickable(true);
        }

        mToolbarLayout.setTitle(product.getName());
        mTvOwnerProduk.setText(email);
        mTvNamaProduk.setText(product.getName());
        mTvJenisProduk.setText(product.getCategory());
        mTvDeskripsiProduk.setText(product.getDescription());

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
                        this,
                        mDatabaseRef
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

        if (id == R.id.btn_buy_detail_produk) {
            buyProduct();
        }
    }

    private void buyProduct() {
        AlertDialog alertDialog = new AlertDialog.Builder(DetailProdukActivity.this).create();
        alertDialog.setTitle("Buy");
        alertDialog.setMessage("Do you want to buy " + mProduct.getName() + " with price Rp. " + mProduct.getPrice() + " ?");

        alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "BUY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        handleBuyProduct(dialog);
                        // dialog.dismiss();
                    }
                });

        alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    private void handleBuyProduct(final DialogInterface dialog) {

        // cek saldo user
        mDatabaseRef.child(CHILD_USER).child(mUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        double saldo = user.getSaldo();

                        if (saldo < mProduct.getPrice()) {
                            Toast.makeText(DetailProdukActivity.this, "Saldo tidak mencukupi.", Toast.LENGTH_SHORT).show();
                        } else {
                            updateSaldoUser(dialog);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // ubah satatus barang terjual
        mProduct.setSold(true);
    }

    private void updateSaldoUser(final DialogInterface dialog) {
        // update ke tabel barang tsb
        mDatabaseRef.child(CHILD_PRODUCT).child(mProductId).setValue(mProduct)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Toast.makeText(DetailProdukActivity.this, "Barang sukses Terjual", Toast.LENGTH_SHORT).show();
                        Log.d(TAG_BUY_PRODUCT, "onComplete: handleBuyProduct: mProduct.isSold(): " + mProduct.isSold());

                        // update saldo user
                        setSaldoBaruUser(dialog);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_BUY_PRODUCT, "onFailure: handleBuyProduct: mProduct.isSold(): " + mProduct.isSold());
                        Log.d(TAG_BUY_PRODUCT, "onFailure: handleBuyProduct: e " + e.getLocalizedMessage());
                        Toast.makeText(DetailProdukActivity.this, "Terjadi kesalahan saat menjual product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setSaldoBaruUser(final DialogInterface dialog) {
        // get current user saldo
        mDatabaseRef.child(CHILD_USER).child(mUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);
                        double saldo = user.getSaldo();
                        double newSaldo = saldo - mProduct.getPrice();

                        // set saldo baru
                        user.setSaldo(newSaldo);

                        // update saldo user skrng dengan saldo yang baru
                        mDatabaseRef.child(CHILD_USER).child(mUser.getUid()).setValue(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG_BUY_PRODUCT, "onComplete: setSaldoBaruUser: user.getSaldo() : " + user.getSaldo());
                                        Toast.makeText(DetailProdukActivity.this, "Transaksi sukses", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG_BUY_PRODUCT, "onFailure: setSaldoBaruUser: e" + e.getLocalizedMessage());
                                        dialog.dismiss();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


//    private void handleBuyProduct(final DialogInterface dialog) {
//        // cek saldo database
//        mDatabaseRef.child(CHILD_USER).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                double saldo = user.getSaldo();
//
//                // mProduct.getPrice()
//                if (saldo < mProduct.getPrice()) {
//                    dialog.dismiss();
//                    Toast.makeText(DetailProdukActivity.this, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
//                } else {
//                    // coding pembelian
//                    handleAlgoritmaPembelian(user, dialog);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void handleAlgoritmaPembelian(final User user, final DialogInterface dialog) {
//
//        mDatabaseRef.child(CHILD_PRODUCT).child(mProductId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Product product = dataSnapshot.getValue(Product.class);
//
//                // codingan untuk ubah status jual
//                product.setSold(true);
//
//                double saldoBaru = user.getSaldo() - product.getPrice();
//
//                // update saldo
//                user.setSaldo(saldoBaru);
//
//                // update database user (saldonya di update)
//                handleUpdateDataUserWithUser(user, dialog);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(DetailProdukActivity.this, "Transaksi gagal.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void handleUpdateDataUserWithUser(User user, final DialogInterface dialog) {
//        mDatabaseRef.child(CHILD_USER).child(mUser.getUid()).setValue(user.getSaldo())
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        updateOwnerIdProductWithUser(mUser, dialog);
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(DetailProdukActivity.this, "Transaksi gagal.", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                });
//    }
//
//    private void updateOwnerIdProductWithUser(FirebaseUser mUser, final DialogInterface dialog) {
//
//        mProduct.setOwnerId(mUser.getUid());
//        mDatabaseRef.child(CHILD_PRODUCT).child(mProductId).setValue(mProduct)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        dialog.dismiss();
//                        Toast.makeText(DetailProdukActivity.this, "Transaksi berhasil.", Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(DetailProdukActivity.this, "Transaksi gagal. Terjadi kesalahan saat update product owner.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

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


    private void setupEditTextComment() {
        mEtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
                mEtComment.setFocusable(true);
            }
        });

        mEtComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(v);
                mEtComment.setFocusable(true);
                return false;
            }
        });
    }


    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private void scollToTop() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(
                    mCoordinatorLayout,
                    mAppBarLayout,
                    null,
                    0,
                    10000,
                    true
            );
            Log.d(TAG_DETAIL_PRODUK_UI, "onCreate: behavior is not null");
        } else {
            Log.d(TAG_DETAIL_PRODUK_UI, "onCreate: behavior is null");
        }
    }


}
