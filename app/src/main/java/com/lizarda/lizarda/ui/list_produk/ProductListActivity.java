package com.lizarda.lizarda.ui.list_produk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Product;
import com.lizarda.lizarda.ui.detail_produk.DetailProdukActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.BUTTON_ID_KEY;

public class ProductListActivity extends AppCompatActivity implements ListProdukCallback {

    private ArrayList<Product> mProducts;
    private ProductAdapter mAdapters;

    @BindView(R.id.rv_list_detail_kategori)
    RecyclerView mRvDetailKategori;

    private Bundle mExtras;
    private int mButtonMoreId;

    private ActionBar mActionBar;

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

        changeToolbarTitle();

        ButterKnife.bind(this);
        prepareData();
        setupRecyclerView();
    }

    private void changeToolbarTitle() {
        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mButtonMoreId = mExtras.getInt(BUTTON_ID_KEY);

            if (mButtonMoreId == R.id.btn_more_suggest_home) {
                mActionBar.setTitle("Suggest");
                // fetch suggest ...
            }
            if (mButtonMoreId == R.id.btn_more_popular_home) {
                mActionBar.setTitle("Popular");
                // fetch popular ...
            }
            if (mButtonMoreId == R.id.btn_more_new_listing_home) {
                mActionBar.setTitle("New Listing");
                // fetch new listing ...
            }
        }
    }

    @Override
    public void onItemClick() {
        navigateToDetailProdukActivity();
    }

    private void navigateToDetailProdukActivity() {
        Intent intent = new Intent(this, DetailProdukActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerView() {
        mRvDetailKategori.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mRvDetailKategori.setAdapter(new ProductAdapter(mProducts, this));
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
