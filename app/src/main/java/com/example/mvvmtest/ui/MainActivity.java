package com.example.mvvmtest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mvvmtest.R;
import com.example.mvvmtest.viewmodel.WordViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class MainActivity extends AppCompatActivity {
    private WordViewModel wordViewModel;
    private MyAdapter myAdapter;

    @BindView(R.id.main_recycler)
    RecyclerView mainRecycler;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));


        wordViewModel.getAllGankLive().observe(this, gankEnity -> {
            myAdapter = new MyAdapter(wordViewModel,gankEnity);
            mainRecycler.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        });

    }



    @OnClick(R.id.button_insert)
    public void insert() {
        wordViewModel.insertGanks();
    }

    @OnClick(R.id.button_clear)
    public void clear() {
        wordViewModel.deleteAllGanks();
    }
}
