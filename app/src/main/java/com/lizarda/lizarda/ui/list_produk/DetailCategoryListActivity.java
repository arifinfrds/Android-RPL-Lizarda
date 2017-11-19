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
import com.lizarda.lizarda.ui.home.HomeKategoriCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailCategoryListActivity extends AppCompatActivity implements ListProdukCallback {

    private ArrayList<Product> mProducts;
    private ProductAdapter mAdapters;

    @BindView(R.id.rv_list_detail_kategori)
    RecyclerView mRvDetailKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);
        prepareData();
        setupRecyclerView();
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
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
        mProducts.add(new Product("Ular", "Ular Hijau"));
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
