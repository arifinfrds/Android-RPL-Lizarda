package com.lizarda.lizarda.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.PopupMenu;

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
import com.lizarda.lizarda.model.User;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_USER;
import static com.lizarda.lizarda.Const.NOT_SET;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

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

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


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

        mBtnBrowse.setOnClickListener(this);
        mBtnInputProduct.setOnClickListener(this);

        setupFirebase();
        // userIdFromDatabase();

        mEtNamaProduct.setFocusable(false);
        mEtJenisProduct.setFocusable(false);
        mEtDescriptionProduct.setFocusable(false);
        mEtHargaProduct.setFocusable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_browse_add_product:
                showPopupMenuPhoto();
                break;
            case R.id.btn_input_add_product:
                if (isInputEmpty()) {
                    Toast.makeText(this, "Mohon cek input Anda.", Toast.LENGTH_SHORT).show();
                } else {
                    writeToNodeProduct();
                }
                break;
        }
    }

    private void showPopupMenuPhoto() {
        PopupMenu popup = new PopupMenu(AddProductActivity.this, mBtnBrowse);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.image_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        AddProductActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });
        popup.show();
    }


    private void writeToNodeProduct() {
        String hargaStr = mEtHargaProduct.getText().toString();
        double harga = Double.parseDouble(hargaStr);

        String productId = mDatabaseRef.push().getKey();
        Product product = new Product(
                productId,
                mEtNamaProduct.getText().toString(),
                mEtDescriptionProduct.getText().toString(),
                NOT_SET,
                false,
                harga,
                mEtJenisProduct.getText().toString(),
                ""
        );
        mDatabaseRef.child(CHILD_PRODUCT).child(productId).setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(
                                AddProductActivity.this,
                                "Sukses Tambah Produk Anda.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                AddProductActivity.this,
                                "Terjadi kesalahan. Gagal tambah product Anda.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isInputEmpty() {
        String name = mEtNamaProduct.getText().toString();
        String jenis = mEtJenisProduct.getText().toString();
        String description = mEtDescriptionProduct.getText().toString();
        String hargaStr = mEtHargaProduct.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(jenis) || TextUtils.isEmpty(description)
                || TextUtils.isEmpty(hargaStr)) {
            return true;
        } else {
            return false;
        }
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
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

    // MARK: - untuk generate database only! =======================================================
    private ArrayList<String> mUsersId = new ArrayList<>();

    private void userIdFromDatabase() {

        mDatabaseRef.child(CHILD_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("Count", "" + dataSnapshot.getChildrenCount());
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        mUsersId.add(user.getId());
                        Log.d("Count", "onDataChange: " + user.getId());
                    }
                    writeManyProduct();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeManyProduct() {

        String[] names = {
                "Ular padang pasir",
                "Iguana Hijau",
                "Bunglon hutan"
        };

        String[] desctiprions = {
                "Jual ular langka, umur 1 bulan, sehat.",
                "Jual Iguana Reptil langka, umur 1 tahun lincah.",
                "Bunglon peliharaan dari Makassar.",
        };

        String[] categories = {
                "Ular",
                "Iguana",
                "Bunglon"
        };

        for (int i = 0; i < 50; i++) {
            Random rn = new Random();
            double harga = rn.nextInt(5000000 - 200000 + 1) + 200000;

            int index = rn.nextInt(2 - 0) + 0;
            int indexUid = rn.nextInt(3 - 0) + 0;

            String productId = mDatabaseRef.push().getKey();
            Product product = new Product(
                    productId,
                    names[index],
                    desctiprions[index],
                    NOT_SET,
                    false,
                    harga,
                    categories[index],
                    mUsersId.get(indexUid)
            );

            mDatabaseRef.child(CHILD_PRODUCT).child(productId).setValue(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Toast.makeText(AddProductActivity.this, "Sukses Tambah Produk Anda.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Toast.makeText(AddProductActivity.this, "Terjadi kesalahan. Gagal tambah product Anda.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

}
