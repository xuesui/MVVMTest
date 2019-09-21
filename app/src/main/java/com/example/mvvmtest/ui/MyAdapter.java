package com.example.mvvmtest.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtest.R;
import com.example.mvvmtest.model.enity.GankEnity;
import com.example.mvvmtest.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private GankEnity gankEnity;
    private WordViewModel wordViewModel;
    private List<String> chinese=new ArrayList<>();
    private List<String> english=new ArrayList<>();

    MyAdapter(WordViewModel wordViewModel,GankEnity ganks) {
        this.wordViewModel = wordViewModel;
        this.gankEnity=ganks;
        if (gankEnity!=null){
            for (int i=0;i<gankEnity.getResults().getAndroid().size();i++){
                chinese.add(gankEnity.getResults().getAndroid().get(i).getDesc());
                english.add(gankEnity.getResults().getAndroid().get(i).getWho());
            }
        }

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item_normal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position + 1));
        holder.chinese.setText(gankEnity.getResults().getAndroid().get(position).getDesc());
        holder.english.setText(gankEnity.getResults().getAndroid().get(position).getWho());
        holder.switchIsChinese.setOnCheckedChangeListener(null);
        if (gankEnity.isVisible()){
            holder.switchIsChinese.setChecked(true);
            holder.chinese.setVisibility(View.VISIBLE);
        }else {
            holder.switchIsChinese.setChecked(false);
            holder.chinese.setVisibility(View.GONE);
        }

        holder.switchIsChinese.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                gankEnity.setVisible(true);
                holder.chinese.setVisibility(View.VISIBLE);
                wordViewModel.updateGanks(gankEnity);
            }else {
                gankEnity.setVisible(false);
                holder.chinese.setVisibility(View.GONE);
                wordViewModel.updateGanks(gankEnity);
            }
        });


        holder.itemView.setOnClickListener(view -> {
            Uri uri = Uri.parse(gankEnity.getResults().getAndroid().get(position).getUrl());
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chinese.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.switchIsChinese)
        Switch switchIsChinese;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.english)
        TextView english;
        @BindView(R.id.chinese)
        TextView chinese;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
