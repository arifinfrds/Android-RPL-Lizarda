package com.lizarda.lizarda.ui.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
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
import com.lizarda.lizarda.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_USER;
import static com.lizarda.lizarda.Const.FIREBASE.LIMIT_EXPLORE;

public class AdminActivity extends AppCompatActivity implements AdminCallback {

    @BindView(R.id.rv_product_admin)
    RecyclerView mRvProduct;

    @BindView(R.id.progress_bar_admin)
    ProgressBar mProgressBar;

    private ArrayList<Product> mProducts;
    private AdminAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mProducts = new ArrayList<>();

        setupFirebase();

        fetchProduct();

    }

    private void fetchProduct() {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mProducts.isEmpty()) {
                    mProducts.clear();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                        Product product = productDataSnapshot.getValue(Product.class);
                        mProducts.add(product);
                    }
                    // updateUI
                    mProgressBar.setVisibility(View.GONE);
                    setupRecyclerView();
                } else {
                    toast("Tidak ada data.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDeleteProduct(final String productId) {

        // delete product with certain productId
        mDatabaseRef.child(CHILD_PRODUCT).child(productId).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        toast("Produk : " + productId + " berhasil terhapus.");
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toast("Produk : " + productId + " gagal terhapus.");
                    }
                });
    }

    private void setupRecyclerView() {

        LinearLayoutManager defaultHorizontalLayout = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        mRvProduct.setLayoutManager(defaultHorizontalLayout);

        mAdapter = new AdminAdapter(mProducts, this, this);
        mRvProduct.setAdapter(mAdapter);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
    }

    private void toast(String message) {
        Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}
