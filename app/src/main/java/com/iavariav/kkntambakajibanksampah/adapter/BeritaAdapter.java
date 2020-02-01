package com.iavariav.kkntambakajibanksampah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.berita.ArticlesItem;
import com.iavariav.kkntambakajibanksampah.ui.user.WebViewActivity;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ArticlesItem> menuModels;
    public BeritaAdapter(Context context, ArrayList<ArticlesItem> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_berita_sampah, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(menuModels.get(position).getUrlToImage()).into(holder.ivBerita);
        holder.tvJudulBerita.setText(menuModels.get(position).getTitle());
        holder.tvTanggal.setText(menuModels.get(position).getPublishedAt());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(Config.BUNDLE_URL_NEWS, menuModels.get(position).getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBerita;
        private TextView tvJudulBerita;
        private TextView tvTanggal;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBerita = itemView.findViewById(R.id.iv_berita);
            tvJudulBerita = itemView.findViewById(R.id.tv_judul_berita);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            cardView = itemView.findViewById(R.id.cv_klik);

        }
    }
}
