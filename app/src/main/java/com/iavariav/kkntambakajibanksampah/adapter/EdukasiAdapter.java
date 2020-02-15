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
import com.iavariav.kkntambakajibanksampah.model.EdukasiModel;
import com.iavariav.kkntambakajibanksampah.ui.user.DetailEdukasiActivity;

import java.util.ArrayList;

public class EdukasiAdapter extends RecyclerView.Adapter<EdukasiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<EdukasiModel> menuModels;

    public EdukasiAdapter(Context context, ArrayList<EdukasiModel> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_edukasi_sampah, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(menuModels.get(position).getGambarBackdrop()).into(holder.ivEdukasi);
        holder.tvNamaEdukasi.setText(menuModels.get(position).getJudulBerita());
        holder.tvTanggalEdukasi.setText(menuModels.get(position).getTanggalRilisBerita() + "\n" + menuModels.get(position).getDiterbitkanOleh());
        holder.cvKlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailEdukasiActivity.class);
                intent.putExtra("BUNDLE_JUDUL", holder.tvNamaEdukasi.getText().toString());
                intent.putExtra("BUNDLE_TANGGAL", holder.tvTanggalEdukasi.getText().toString());
                intent.putExtra("BUNDLE_DESKRIPSI", menuModels.get(position).getIsiBerita());
                intent.putExtra("BUNDLE_KET_LAIN", menuModels.get(position).getKeteranganLainnya());
                intent.putExtra("BUNDLE_BACKDROP_1", menuModels.get(position).getGambar1Berita());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivEdukasi;
        private TextView tvToken;
        private TextView tvNamaEdukasi;
        private TextView tvTanggalEdukasi;
        private CardView cvKlik;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEdukasi = itemView.findViewById(R.id.iv_edukasi);
            tvToken = itemView.findViewById(R.id.tv_token);
            tvNamaEdukasi = itemView.findViewById(R.id.tv_nama_edukasi);
            tvTanggalEdukasi = itemView.findViewById(R.id.tv_tanggal_edukasi);
            cvKlik = itemView.findViewById(R.id.cv_klik);
        }
    }
}
