package com.lizarda.lizarda.ui.detail_produk;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Model;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.lizarda.lizarda.model.Product;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.KEY_PRODUCT_ID;


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

    private CommentAdapter mCommentAdapter;

    private ArrayList<Model> mModels;

    private Bundle mExtras;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


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

        setupFirebase();

        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            String productId = mExtras.getString(KEY_PRODUCT_ID);
            fetchProductWithId(productId);
        }


        ButterKnife.bind(this);

        mBtnSendComment.setOnClickListener(this);

        mToolbarLayout.setTitle("Green Iguana");

        mModels = Model.generateModels();

        mCommentAdapter = new CommentAdapter(mModels, this);

        setupRecyclerView(mRvComment);
    }

    private void fetchProductWithId(final String productId) {
        mDatabaseRef.child(CHILD_PRODUCT).child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                // updateUI
                updateUI(product);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateUI(Product product) {
        mToolbarLayout.setTitle(product.getName());
        mTvOwnerProduk.setText("Owner dengan ID " + product.getOwnerId());
        mTvNamaProduk.setText(product.getName());
        mTvJenisProduk.setText(product.getCategory());
        mTvDeskripsiProduk.setText(product.getDescription());
        mTvHargaProduk.setText("Rp. " + product.getPrice());
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mCommentAdapter);
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
    private void uploadComment(String comment) {
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

    private void performShare() {
        Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
    }
}
