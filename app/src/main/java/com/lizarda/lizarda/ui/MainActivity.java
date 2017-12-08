package com.lizarda.lizarda.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.lizarda.lizarda.model.User;
import com.lizarda.lizarda.ui.admin.AdminActivity;
import com.lizarda.lizarda.ui.admin.AdminAdapter;
import com.lizarda.lizarda.ui.list_produk.ProductListActivity;
import com.lizarda.lizarda.ui.profile.ProfileActivity;
import com.lizarda.lizarda.ui.detail_produk.DetailProdukActivity;
import com.lizarda.lizarda.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_USER;
import static com.lizarda.lizarda.Const.KEY_ARRAY_LIST_PRODUCT_ID;
import static com.lizarda.lizarda.Const.NOT_SET;
import static com.lizarda.lizarda.Const.TAG.TAG_SEARCH;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    private ArrayList<String> mProductsId;

    private ImageView mIvProfile;
    private TextView mTvNamaUser;
    private TextView mTvEmailUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFirebase();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        mIvProfile = (ImageView) header.findViewById(R.id.iv_profile_home);
        mTvNamaUser = ((TextView) header.findViewById(R.id.tv_nama_home));
        mTvEmailUser = ((TextView) header.findViewById(R.id.tv_email_home));

        // fetch user
        fetchUser();


        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        // show default fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new HomeFragment());
        ft.commit();


        mProductsId = new ArrayList<>();

        // check current user in db
        checkIfUserExistInDatabase();

        fetchStatusAdmin();

    }

    private void checkIfUserExistInDatabase() {
        mDatabaseRef.child(CHILD_USER).child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            createUser();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void fetchStatusAdmin() {
        mDatabaseRef.child(CHILD_USER).child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);

                            if (user.isAdmin()) {
                                Toast.makeText(MainActivity.this, "Hello Admin!", Toast.LENGTH_SHORT).show();
                                navigateToAdminActivity();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void navigateToAdminActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }


    private void fetchUser() {
        mDatabaseRef.child(CHILD_USER).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {

                    mTvEmailUser.setText(mUser.getEmail());
                    if (user.getNama() != null) {
                        mTvNamaUser.setText(user.getNama());
                    } else {
                        mTvNamaUser.setText(user.getNama());
                    }

                    if (user.getPhotoUrl() != null) {
                        if (user.getPhotoUrl().equals("")
                                || user.getPhotoUrl().equalsIgnoreCase(NOT_SET)
                                || user.getPhotoUrl().contains(NOT_SET)) {
                            Picasso.with(getApplicationContext()).load(R.drawable.profile_thumbnail)
                                    .into(mIvProfile);
                        } else {
                            Picasso.with(getApplicationContext()).load(user.getPhotoUrl())
                                    .into(mIvProfile);
                        }
                    } else {
                        Picasso.with(getApplicationContext()).load(R.drawable.profile_thumbnail)
                                .into(mIvProfile);
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isUserExistInDatabase(String uid) {
        final boolean[] isExist = {false};
        mDatabaseRef.child(CHILD_USER).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isExist[0] = dataSnapshot.exists();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return isExist[0];
    }

    private void createUser() {
        User user = new User(
                mUser.getUid(),
                mUser.getEmail(),
                NOT_SET,
                NOT_SET,
                NOT_SET,
                NOT_SET,
                false,
                10000000
        );

        mDatabaseRef.child(CHILD_USER).child(mUser.getUid()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Database user sukses terbuat.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Database user gagal terbuat.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        handleSearchView(menu);
//
//        return true;
//    }

    private void handleSearchView(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query == null) {
                    Toast.makeText(MainActivity.this, "Harap isi keyword pencarian,"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    if (query.length() <= 0 || query.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Harap isi keyword pencarian,"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        performSearch(query);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        String[] from = new String[]{"cafe_name"};
        int[] to = new int[]{R.id.tv_suggestion_text};

        SimpleCursorAdapter searchCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        searchView.setSuggestionsAdapter(searchCursorAdapter);

        MenuItem searchMenuItem = menu.getItem(0);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // Refresh here with full list.
                        return true;
                    }
                });
    }


    private void performSearch(final String query) {
        Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();

        // cek pencarian apakah ada di database
        // kalo ada, ke detail activity
        // kalo nggk ada, kasih notif nggk ada

        // disumsikan ada
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {

                        Product product = productDataSnapshot.getValue(Product.class);

//                        boolean condition = product.getName().toLowerCase().contains(query.toLowerCase())
//                                || product.getCategory().toLowerCase().contains(query.toLowerCase());

                        boolean condition = product.getCategory().toLowerCase().contains(query.toLowerCase());

                        if (condition) {
                            // Log.d(TAG_SEARCH, "onDataChange: query: " + query + " product.getName(): " + product.getName());
                            mProductsId.add(product.getId());
                        } else {
                            Toast.makeText(MainActivity.this, "Tidak ada data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    navigateToListActivity(mProductsId);
                } else {
                    Toast.makeText(MainActivity.this, "Tidak ada data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void navigateToListActivity(ArrayList<String> productsId) {
        Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
        // intent.putStringArrayListExtra(KEY_ARRAY_LIST_PRODUCT_ID, productsId);
        intent.putExtra(KEY_ARRAY_LIST_PRODUCT_ID, productsId);
        startActivity(intent);
    }

    private void navigateToDetailProdukActivity() {
        Intent intent = new Intent(MainActivity.this, DetailProdukActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_browse) {
            displayView(id);
        } else if (id == R.id.nav_profile) {
            displayView(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    private void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_browse:
                title = getResources().getString(R.string.title_home);
                fragment = new HomeFragment();
                break;
            case R.id.nav_profile:
                navigateToProfileActivity();
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void navigateToProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

}
