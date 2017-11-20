package com.lizarda.lizarda.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lizarda.lizarda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity {

    @BindView(R.id.iv_product_add_product)
    ImageView mIvProduct;

    @BindView(R.id.btn_browse_add_product)
    Button mBtnBrowse;

    @BindView(R.id.et_nama_add_product)
    EditText mEtNamaProduct;

    @BindView(R.id.et_jenis_add_product)
    EditText mEtJenisProduct;

    @BindView(R.id.et_description_add_product)
    EditText mEtDescriptionProduct;

    @BindView(R.id.et_harga_add_product)
    EditText mEtHargaProduct;

    @BindView(R.id.btn_input_add_product)
    Button mBtnInputProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

//        mEtNamaProduct.setFocusable(false);
//        mEtJenisProduct.setFocusable(false);
//        mEtDescriptionProduct.setFocusable(false);
//        mEtHargaProduct.setFocusable(false);
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
