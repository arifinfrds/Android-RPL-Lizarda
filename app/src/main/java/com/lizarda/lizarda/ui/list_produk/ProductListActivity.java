package com.lizarda.lizarda.ui.list_produk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Product;
import com.lizarda.lizarda.ui.detail_produk.DetailProdukActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.BUTTON_ID_KEY;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.KEY_KATEGORI;
import static com.lizarda.lizarda.Const.KEY_PRODUCT_ID;

public class ProductListActivity extends AppCompatActivity implements ListProdukCallback {

    private ArrayList<Product> mProducts;
    private ProductAdapter mAdapters;

    @BindView(R.id.rv_list_detail_kategori)
    RecyclerView mRvDetailKategori;

    private Bundle mExtras;
    private int mButtonMoreId;

    private ActionBar mActionBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupFirebase();

        changeToolbarTitle();

        ButterKnife.bind(this);
        prepareData();
    }

    private void changeToolbarTitle() {
        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mButtonMoreId = mExtras.getInt(BUTTON_ID_KEY);
            String kategori = mExtras.getString(KEY_KATEGORI);

            if (kategori != null) {
                fetchProduct(kategori);
            } else {
                if (mButtonMoreId == R.id.btn_more_suggest_home) {
                    mActionBar.setTitle("Suggest");
                    // fetch suggest ...
                    fetchProduct();
                }
                if (mButtonMoreId == R.id.btn_more_popular_home) {
                    mActionBar.setTitle("Popular");
                    // fetch popular ...
                    fetchProduct();
                }
                if (mButtonMoreId == R.id.btn_more_new_listing_home) {
                    mActionBar.setTitle("New Listing");
                    // fetch new listing ...
                    fetchProduct();
                }
            }


        }
    }

    private void fetchProduct(final String kategori) {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);

                    if (product.getCategory().equalsIgnoreCase(kategori)) {
                        mProducts.add(product);
                    }
                }
                // updateUI
                setupRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchProduct() {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);
                    mProducts.add(product);
                }
                // updateUI
                setupRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemClick(String productId) {
        navigateToDetailProdukActivity(productId);
    }

    private void navigateToDetailProdukActivity(String productId) {
        Intent intent = new Intent(this, DetailProdukActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, productId);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        mRvDetailKategori.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mRvDetailKategori.setAdapter(new ProductAdapter(mProducts, this));
    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
    }

    private void prepareData() {
        mProducts = new ArrayList<>();
//        mProducts.add(new Product("0", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000, "ular"));
//        mProducts.add(new Product("1", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("2", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("3", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("4", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("5", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("6", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("7", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("8", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("9", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("10", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("11", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("12", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
//        mProducts.add(new Product("13", "Ular", "Ular hijau", "http:/www.google.com", false, 80.000));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
