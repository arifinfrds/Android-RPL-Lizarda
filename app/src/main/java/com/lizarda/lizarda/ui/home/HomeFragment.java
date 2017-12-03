package com.lizarda.lizarda.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lizarda.lizarda.model.Kategori;
import com.lizarda.lizarda.model.Model;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Product;
import com.lizarda.lizarda.ui.list_produk.ProductListActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lizarda.lizarda.Const.FIREBASE.CHILD_POPULARITY_COUNT;
import static com.lizarda.lizarda.Const.FIREBASE.LIMIT_POPULAR;
import static com.lizarda.lizarda.Const.FIREBASE.PRODUCT_DEFAULT_POPULARITY_COUNT;
import static com.lizarda.lizarda.Const.KEY_BUTTON_ID;
import static com.lizarda.lizarda.Const.FIREBASE.CHILD_PRODUCT;
import static com.lizarda.lizarda.Const.FIREBASE.LIMIT_NEW_LISTING;
import static com.lizarda.lizarda.Const.FIREBASE.LIMIT_EXPLORE;
import static com.lizarda.lizarda.Const.KEY_KATEGORI;
import static com.lizarda.lizarda.Const.TAG.TAG_POPULAR;


public class HomeFragment extends Fragment implements HomeKategoriCallback, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // MARK: - Properties
    private ArrayList<Model> mModels;
    private ArrayList<Product> mSuggestProducts;
    private ArrayList<Product> mPopularProducts;
    private ArrayList<Product> mNewListingProducts;

    @BindView(R.id.rv_kategori_home)
    RecyclerView mRvKategori;

    @BindView(R.id.rv_suggest_home)
    RecyclerView mRvSuggest;

    @BindView(R.id.rv_popular_home)
    RecyclerView mRvPopular;

    @BindView(R.id.rv_new_listing_home)
    RecyclerView mRvNewListing;

    @BindView(R.id.btn_more_explore_home)
    Button mBtnMoreSuggest;

    @BindView(R.id.btn_more_popular_home)
    Button mBtnMorePopular;

    @BindView(R.id.btn_more_new_listing_home)
    Button mBtnMoreNewListing;

    @BindView(R.id.progress_bar_suggest_home)
    ProgressBar mProgressBarSuggest;

    @BindView(R.id.progress_bar_popular_home)
    ProgressBar mProgressBarPopular;

    @BindView(R.id.progress_bar_new_listing_home)
    ProgressBar mProgressBarNewListing;

    @BindView(R.id.tv_section_explore_home)
    TextView mTvSectionExplore;

    @BindView(R.id.tv_section_popular_home)
    TextView mTvSectionPopular;

    @BindView(R.id.tv_section_new_listing_home)
    TextView mTvSectionNewListing;

    private HomeKategoriAdapter mHomeKategoriAdapter;
    private HomeSuggestAdapter mHomeSuggestAdapter;
    private HomePopularAdapter mHomePopularAdapter;
    private HomeNewListingAdapter mHomeNewListingAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mModels = Model.generateModels();
        mSuggestProducts = new ArrayList<>();
        mPopularProducts = new ArrayList<>();
        mNewListingProducts = new ArrayList<>();

        mBtnMoreSuggest.setOnClickListener(this);
        mBtnMorePopular.setOnClickListener(this);
        mBtnMoreNewListing.setOnClickListener(this);

        setupFirebase();


        mHomeKategoriAdapter = new HomeKategoriAdapter(Kategori.getCategories(), this, getContext());
        setupRecyclerView(mRvKategori);

        // updateChildValuesProduct();
    }

    @Override
    public void onResume() {
        super.onResume();

        mTvSectionExplore.setText("Explore");
        mTvSectionPopular.setText("Popular");
        mTvSectionNewListing.setText("New Listing");

        fetchExplore();
        fetchPopular();
        fetchNewListing();
    }

    private ArrayList<Product> mModifiedProducts = new ArrayList<>();

    private void updateChildValuesProduct() {
        mDatabaseRef.child(CHILD_PRODUCT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // fetch all product
                for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = productDataSnapshot.getValue(Product.class);
                    mModifiedProducts.add(product);
                }

                // update product values
                for (Product product : mModifiedProducts) {
                    product.setPopularityCount(PRODUCT_DEFAULT_POPULARITY_COUNT);
                }

                // mDatabaseRef.child(CHILD_PRODUCT).add
                for (Product product : mModifiedProducts) {
                    mDatabaseRef.child(CHILD_PRODUCT).child(product.getId())
                            .child("popularityCount").setValue(product.getPopularityCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void fetchExplore() {
        mDatabaseRef.child(CHILD_PRODUCT).limitToFirst(LIMIT_EXPLORE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mSuggestProducts.isEmpty()) {
                    mSuggestProducts.clear();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                        Product product = productDataSnapshot.getValue(Product.class);
                        mSuggestProducts.add(product);
                    }
                    // updateUI
                    mProgressBarSuggest.setVisibility(View.GONE);
                    setupRecyclerView(mRvSuggest);
                } else {
                    mTvSectionExplore.setText("Suggest - Tidak ada data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchPopular() {
        mDatabaseRef.child(CHILD_PRODUCT).limitToLast(LIMIT_POPULAR)
                .orderByChild(CHILD_POPULARITY_COUNT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mPopularProducts.isEmpty()) {
                    mPopularProducts.clear();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                        Product product = productDataSnapshot.getValue(Product.class);
                        Log.d(TAG_POPULAR, "onDataChange: product.getPopularityCount(): " + product.getPopularityCount());
                        mPopularProducts.add(product);
                    }
                    // updateUI
                    mProgressBarPopular.setVisibility(View.GONE);
                    setupRecyclerView(mRvPopular);
                } else {
                    mTvSectionPopular.setText("Popular - Tidak ada data");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchNewListing() {
        mDatabaseRef.child(CHILD_PRODUCT).limitToLast(LIMIT_NEW_LISTING).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mNewListingProducts.isEmpty()) {
                    mNewListingProducts.clear();
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()) {
                        Product product = productDataSnapshot.getValue(Product.class);
                        mNewListingProducts.add(product);
                    }
                    // updateUI
                    mProgressBarNewListing.setVisibility(View.GONE);
                    setupRecyclerView(mRvNewListing);
                } else {
                    mTvSectionNewListing.setText("New Listing - Tidak ada data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more_explore_home:
                navigateToProductListActivity(R.id.btn_more_explore_home);
                break;
            case R.id.btn_more_popular_home:
                navigateToProductListActivity(R.id.btn_more_popular_home);
                break;
            case R.id.btn_more_new_listing_home:
                navigateToProductListActivity(R.id.btn_more_new_listing_home);
                break;
        }
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();
    }

    private void navigateToProductListActivity(int fromButtonId) {

        Intent intent = new Intent(getContext(), ProductListActivity.class);

        if (fromButtonId == R.id.btn_more_explore_home) {
            intent.putExtra(KEY_BUTTON_ID, R.id.btn_more_explore_home);

        } else if (fromButtonId == R.id.btn_more_popular_home) {
            intent.putExtra(KEY_BUTTON_ID, R.id.btn_more_popular_home);

        } else if (fromButtonId == R.id.btn_more_new_listing_home) {
            intent.putExtra(KEY_BUTTON_ID, R.id.btn_more_new_listing_home);
        }
        startActivity(intent);
    }

    @Override
    public void onKategoriClick(String kategori) {
        navigateToDetailCategoryListActivity(kategori);
    }

    private void navigateToDetailCategoryListActivity(String kategori) {
        Intent intent = new Intent(getContext(), ProductListActivity.class);
        intent.putExtra(KEY_KATEGORI, kategori);
        getContext().startActivity(intent);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        LinearLayoutManager defaultHorizontalLayout = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        LinearLayoutManager reverseHorizontalLayout = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                true
        );
        reverseHorizontalLayout.setReverseLayout(true);
        reverseHorizontalLayout.setStackFromEnd(true);

        if (recyclerView.getId() == R.id.rv_popular_home) {
            recyclerView.setLayoutManager(reverseHorizontalLayout);
        } else {
            recyclerView.setLayoutManager(defaultHorizontalLayout);
        }

        mHomeSuggestAdapter = new HomeSuggestAdapter(mSuggestProducts, getContext());
        mHomePopularAdapter = new HomePopularAdapter(mPopularProducts, getContext());
        mHomeNewListingAdapter = new HomeNewListingAdapter(mNewListingProducts, getContext());

        switch (recyclerView.getId()) {
            case R.id.rv_kategori_home:
                recyclerView.setAdapter(mHomeKategoriAdapter);
                break;
            case R.id.rv_suggest_home:
                recyclerView.setAdapter(mHomeSuggestAdapter);
                break;
            case R.id.rv_popular_home:
                recyclerView.setAdapter(mHomePopularAdapter);
                break;
            case R.id.rv_new_listing_home:
                recyclerView.setAdapter(mHomeNewListingAdapter);
                break;
        }


    }
}
