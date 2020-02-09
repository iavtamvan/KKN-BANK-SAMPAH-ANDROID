package com.iavariav.kkntambakajibanksampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.model.RiwayatModel;

import java.util.ArrayList;

public class RiwayatTukarBarangAdapter extends RecyclerView.Adapter<RiwayatTukarBarangAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RiwayatModel> menuModels;

    public RiwayatTukarBarangAdapter(Context context, ArrayList<RiwayatModel> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_tukar_sampah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvNamaBarangTipe.setText(menuModels.get(position).getTipeBarang());
        holder.tvTgl.setText(menuModels.get(position).getKadaluarsaBarang());
        holder.tvNamaPemasok.setText("Nama pemasok barang : " + menuModels.get(position).getNamaPemasok());
        holder.tvNamaPetugas.setText(menuModels.get(position).getDeskripsiBarang());
        Glide.with(context).load(menuModels.get(position).getFotoUrlBarang()).into(holder.tvLvRiwayat);
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tvLvRiwayat;
        private TextView tvToken;
        private TextView tvNamaBarangTipe;
        private TextView tvNamaPemasok;
        private TextView tvNamaPetugas;
        private TextView tvTgl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLvRiwayat = itemView.findViewById(R.id.tv_lv_riwayat);
            tvToken = itemView.findViewById(R.id.tv_token);
            tvNamaBarangTipe = itemView.findViewById(R.id.tv_nama_barang_tipe);
            tvNamaPemasok = itemView.findViewById(R.id.tv_nama_pemasok);
            tvNamaPetugas = itemView.findViewById(R.id.tv_nama_petugas);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
        }
    }
}
