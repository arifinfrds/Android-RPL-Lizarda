package com.lizarda.lizarda.ui.profile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.User;
import com.lizarda.lizarda.ui.add_product.AddProductActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.lizarda.lizarda.Const.NOT_SET;


public class ProfileActivity extends AppCompatActivity {

    private TextView txtNama;
    private TextView txtAlamat;
    private TextView txtDeskripsi;
    private TextView txtEmail;
    private String userID;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;

    @BindView(R.id.iv_thumbnail_profile)
    CircleImageView mCivThumbnail;

    @BindView(R.id.tv_saldo_profile)
    TextView mTvSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNama = (TextView) findViewById(R.id.tv_nama_home);
        txtAlamat = (TextView) findViewById(R.id.txt_alamat);
        txtDeskripsi = (TextView) findViewById(R.id.txt_deskripsi);
        txtEmail = (TextView) findViewById(R.id.tv_email_home);

        setTitle("Profile");

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference("User");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        txtEmail.setText(user.getEmail());


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddProductActivity();
            }
        });

        //ambil data dari database
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                txtNama.setText(dataSnapshot.child(userID).getValue(User.class).getNama());
                txtAlamat.setText(dataSnapshot.child(userID).getValue(User.class).getAlamat());
                txtDeskripsi.setText(dataSnapshot.child(userID).getValue(User.class).getDeskripsi());

                mTvSaldo.setText("Saldo : Rp. " + dataSnapshot.child(userID).getValue(User.class).getSaldo());

                if (dataSnapshot.child(userID).getValue(User.class).getPhotoUrl() != null) {
                    if (dataSnapshot.child(userID).getValue(User.class).getPhotoUrl().equals("")
                            || dataSnapshot.child(userID).getValue(User.class).getPhotoUrl().equalsIgnoreCase(NOT_SET)
                            || dataSnapshot.child(userID).getValue(User.class).getPhotoUrl().contains(NOT_SET)) {
                        Picasso.with(getApplicationContext()).load(R.drawable.profile_thumbnail)
                                .into(mCivThumbnail);
                    } else {
                        Picasso.with(getApplicationContext())
                                .load(dataSnapshot.child(userID).getValue(User.class).getPhotoUrl())
                                .into(mCivThumbnail);
                    }
                } else {
                    Picasso.with(getApplicationContext()).load(R.drawable.profile_thumbnail)
                            .into(mCivThumbnail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //blum selesaiboi
    private void navigateToEditUser() {
        Toast.makeText(this, "mantap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, edtProfileActivity.class);
        startActivity(intent);
    }
    //sampe sini

    private void navigateToAddProductActivity() {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit_profile:
                navigateToEditUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
