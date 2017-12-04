package com.lizarda.lizarda.ui.list_produk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_POPULARITY_COUNT;
import static com.lizarda.lizarda.Const.KEY_ARRAY_LIST_PRODUCT_ID;
import static com.lizarda.lizarda.Const.KEY_BUTTON_ID;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.FIREBASE.LIMIT_NEW_LISTING;
import static com.lizarda.lizarda.Const.KEY_KATEGORI;
import static com.lizarda.lizarda.Const.KEY_PRODUCT_ID;
import static com.lizarda.lizarda.Const.TAG.TAG_POPULAR;
import static com.lizarda.lizarda.Const.TAG.TAG_SEARCH;

public class ProductListActivity extends AppCompatActivity implements ListProdukCallback {

    private ArrayList<Product> mProducts;
    private ProductAdapter mAdapters;

    @BindView(R.id.rv_list_detail_kategori)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar_list)
    ProgressBar mProgressBar;

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
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        setupFirebase();

        changeToolbarTitle();

        prepareData();
    }

    private void changeToolbarTitle() {
        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mButtonMoreId = mExtras.getInt(KEY_BUTTON_ID);
            String kategori = mExtras.getString(KEY_KATEGORI);

            ArrayList<String> arrayListProductIdKey = (ArrayList<String>) getIntent().getSerializableExtra(KEY_ARRAY_LIST_PRODUCT_ID);
            Log.d(TAG_SEARCH, "changeToolbarTitle: arrayListProductIdKey: " + arrayListProductIdKey);

            // cek jika ini intent setelah user klik kategori
            if (kategori != null) {
                fetchProduct(kategori);
                mActionBar.setTitle(kategori);
            } else if (arrayListProductIdKey != null) {
                Log.d(TAG_SEARCH, "changeToolbarTitle: arrayListProductIdKey != null: fetchSearch...");
                fetchProduct(arrayListProductIdKey);
            } else {
                if (mButtonMoreId == R.id.btn_more_explore_home) {
                    mActionBar.setTitle("Explore");
                    // fetch explore ...
                    fetchProduct();
                }
                if (mButtonMoreId == R.id.btn_more_popular_home) {
                    mActionBar.setTitle("Popular");
                    // fetch popular ...
                    fetchPopular();
                }
                if (mButtonMoreId == R.id.btn_more_new_listing_home) {
                    mActionBar.setTitle("New Listing");
                    // fetch new listing ...
                    fetchNewListing();
                }
            }
        }
    }


    private void fetchProduct(final ArrayList<String> arrayListProductIdKey) {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mProducts.isEmpty()) {
                    mProducts.clear();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                        Product product = productDataSnapshot.getValue(Product.class);
                        // cek jika id product ada di list
                        mProducts.add(product);

//                          setupRecyclerView();
//                    for (String productId : arrayListProductIdKey) {
//                        if (product.getId().equals(productId)) {
//                            // Log.d(TAG_SEARCH, "onDataChange: product.getId(): " + product.getId());
//                            Log.d(TAG_SEARCH, "onDataChange: product.getCategory(): " + product.getCategory() + " product.getName(): " + product.getName());
//                        }
//                    }

//                        for (int i = 0; i < arrayListProductIdKey.size(); i++) {
//                            if (product.getId().equals(arrayListProductIdKey.get(i))) {
//                                mProducts.add(product);
//                                Log.d(TAG_SEARCH, "onDataChange: if");
//                            } else {
//                                Log.d(TAG_SEARCH, "onDataChange: else");
//                            }
//                            // Log.d(TAG_SEARCH, "onDataChange: product.getCategory(): " + product.getCategory() + " product.getName(): " + product.getName());
//                        }
//
//                        for (Product p : mProducts) {
//                            Log.d(TAG_SEARCH, "onDataChange: Product: " + product.getId());
//                        }
                    }

//                    if (!mProducts.isEmpty()) {
//                        mProducts.clear();
//                    }
//                    for (int i = 0; i < mProducts.size(); i++) {
//                        for (int j = 0; j < arrayListProductIdKey.size(); j++) {
//                            if (arrayListProductIdKey.get(j).equals(mProducts.get(i).getId())) {
//                                mProducts.add(mProducts.get(i));
//                            } else {
//                                Toast.makeText(ProductListActivity.this, "not equal", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    setupRecyclerView();

                    // FIXME: 11/26/17 BINGUNG!
//                    for (int i = 0; i < mProducts.size(); i++) {
//                        for (int j = 0; j < arrayListProductIdKey.size(); j++) {
//                            if (mProducts.get(i).getId().equals(arrayListProductIdKey.get(j))) {
//                                Log.d(TAG_SEARCH, "onDataChange: if");
//                            } else {
//                                Log.d(TAG_SEARCH, "onDataChange: else");
//                            }
//                        }
//                    }


                } else {
                    Toast.makeText(ProductListActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchProduct(final String kategori) {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mProducts.isEmpty()) {
                    mProducts.clear();
                }
                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);

                    if (product.getCategory().equalsIgnoreCase(kategori)) {
                        mProducts.add(product);
                    }
                }
                // updateUI
                setupRecyclerView();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fetchProduct() {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mProducts.isEmpty()) {
                    mProducts.clear();
                }
                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);
                    mProducts.add(product);
                }
                // updateUI
                setupRecyclerView();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }


    private void fetchPopular() {
        mDatabaseRef.child(CHILD_PRODUCT).orderByChild(CHILD_POPULARITY_COUNT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mProducts.isEmpty()) {
                    mProducts.clear();
                }
                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);
                    Log.d(TAG_POPULAR, "onDataChange: product.getPopularityCount(): " + product.getPopularityCount());
                    mProducts.add(product);
                }
                // updateUI
                setupRecyclerView();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fetchNewListing() {
        mDatabaseRef.child(CHILD_PRODUCT).limitToLast(LIMIT_NEW_LISTING).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mProducts.isEmpty()) {
                    mProducts.clear();
                }
                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);
                    mProducts.add(product);
                }
                // updateUI
                setupRecyclerView();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();
                mProgressBar.setVisibility(View.GONE);
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

        LinearLayoutManager defaultHorizontalLayout = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        LinearLayoutManager reverseHorizontalLayout = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                true
        );
        reverseHorizontalLayout.setReverseLayout(true);
        reverseHorizontalLayout.setStackFromEnd(true);

        if (mButtonMoreId == R.id.btn_more_popular_home) {
            mRecyclerView.setLayoutManager(reverseHorizontalLayout);
        } else {
            mRecyclerView.setLayoutManager(defaultHorizontalLayout);
        }

        mAdapters = new ProductAdapter(mProducts, this, this);
        mRecyclerView.setAdapter(mAdapters);
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

    private void showErrorMessage() {
        Toast.makeText(
                ProductListActivity.this,
                "Terjadi kesalahan. Silahkan coba lagi.",
                Toast.LENGTH_SHORT
        ).show();
    }
}
