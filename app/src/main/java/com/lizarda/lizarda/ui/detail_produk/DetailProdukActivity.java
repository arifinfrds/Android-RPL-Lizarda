package com.lizarda.lizarda.ui.detail_produk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailProdukActivity extends AppCompatActivity implements View.OnClickListener {

//    @BindView(R.id.tv_description_detail)
//    TextView mTvDescription;

    @BindView(R.id.rv_comment_detail_produk)
    RecyclerView mRvComment;

    @BindView(R.id.et_comment_detail_produk)
    EditText mEtComment;

    @BindView(R.id.btn_send_comment_detail_produk)
    Button mBtnSendComment;

    private CommentAdapter mCommentAdapter;

    private ArrayList<Model> mModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);

        mBtnSendComment.setOnClickListener(this);


        mModels = Model.generateModels();

        mCommentAdapter = new CommentAdapter(mModels, this);

        setupRecyclerView(mRvComment);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mCommentAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_send_comment_detail_produk) {
            if (mEtComment.getText().toString().matches("")) {
                Toast.makeText(
                        this,
                        "Input komentar tidak boleh kosong.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                uploadComment(mEtComment.getText().toString());
            }
        }
    }

    // MARK: - kodingan upload ke fireabse
    private void uploadComment(String comment) {
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
