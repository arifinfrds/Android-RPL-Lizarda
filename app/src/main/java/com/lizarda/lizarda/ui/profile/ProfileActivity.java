package com.lizarda.lizarda.ui.profile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Map;

import static com.lizarda.lizarda.model.User.*;


public class ProfileActivity extends AppCompatActivity {

    private TextView txtNama;
    private TextView txtAlamat;
    private TextView txtDeskripsi;
    private TextView txtEmail;
    private String userID;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNama = (TextView) findViewById(R.id.txt_nama);
        txtAlamat = (TextView) findViewById(R.id.txt_alamat);
        txtDeskripsi = (TextView) findViewById(R.id.txt_deskripsi);
        txtEmail = (TextView) findViewById(R.id.txt_email);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference("User");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        txtEmail.setText(user.getEmail());

        FloatingActionButton fabEdit = findViewById(R.id.fab2);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToEditUser();
            }
        });


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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //blum selesaiboi
    private void navigateToEditUser(){
        Toast.makeText(this, "mantap",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, edtProfileActivity.class);
        startActivity(intent);
    }
    //sampe sini

    private void navigateToAddProductActivity() {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
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
