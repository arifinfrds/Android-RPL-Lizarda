package com.lizarda.lizarda.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lizarda.lizarda.model.Model;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.ui.detail_produk.DetailProdukActivity;
import com.lizarda.lizarda.ui.list_produk.DetailCategoryListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements HomeKategoriCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // MARK: - Properties
    private ArrayList<Model> mModels;

    @BindView(R.id.rv_kategori_home)
    RecyclerView mRvKategori;

    @BindView(R.id.rv_suggest_home)
    RecyclerView mRvSuggest;

    @BindView(R.id.rv_popular_home)
    RecyclerView mRvPopular;

    @BindView(R.id.rv_new_listing_home)
    RecyclerView mRvNewListing;

    @BindView(R.id.btn_more_suggest_home)
    Button mBtnMoreSuggest;

    private HomeKategoriAdapter mHomeKategoriAdapter;
    private HomeSuggestAdapter mHomeSuggestAdapter;
    private HomePopularAdapter mHomePopularAdapter;
    private HomeNewListingAdapter mHomeNewListingAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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

        mHomeKategoriAdapter = new HomeKategoriAdapter(mModels, this);
        mHomeSuggestAdapter = new HomeSuggestAdapter(mModels, getContext());

        setupRecyclerView(mRvKategori);
        setupRecyclerView(mRvSuggest);
        setupRecyclerView(mRvPopular);
        setupRecyclerView(mRvNewListing);

    }

    @Override
    public void onKategoriClick() {
        navigateToDetailCategoryListActivity();
    }

    private void navigateToDetailCategoryListActivity() {
        Intent intent = new Intent(getContext(), DetailCategoryListActivity.class);
        getContext().startActivity(intent);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {


        switch (recyclerView.getId()) {
            case R.id.rv_kategori_home:
                recyclerView.setAdapter(mHomeKategoriAdapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(
                                getContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        )
                );
                break;
            case R.id.rv_suggest_home:
                recyclerView.setAdapter(mHomeSuggestAdapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(
                                getContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        )
                );
                break;
            case R.id.rv_popular_home:
                recyclerView.setAdapter(mHomeSuggestAdapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(
                                getContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        )
                );
                break;
            case R.id.rv_new_listing_home:
                recyclerView.setAdapter(mHomeSuggestAdapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(
                                getContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        )
                );
                break;
        }


    }
}
