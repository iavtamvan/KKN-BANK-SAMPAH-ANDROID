package com.iavariav.kkntambakajibanksampah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.model.RiwayatModel;

import java.util.ArrayList;

public class RiwayatSampahAdapter extends RecyclerView.Adapter<RiwayatSampahAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RiwayatModel> menuModels;

    public RiwayatSampahAdapter(Context context, ArrayList<RiwayatModel> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_sampah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvToken.setText(menuModels.get(position).getTokenReg());
        holder.tvJenisBerat.setText(menuModels.get(position).getJenisSampah() + ", "  + menuModels.get(position).getBeratSampah());
        holder.tvNamaPetugas.setText("Sudah ambil oleh " +menuModels.get(position).getNamaPetugas());
        holder.tvTgl.setText(menuModels.get(position).getTglInput());

    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tvLvRiwayat;
        private TextView tvToken;
        private TextView tvJenisBerat;
        private TextView tvNamaPetugas;
        private TextView tvTgl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLvRiwayat = itemView.findViewById(R.id.tv_lv_riwayat);
            tvToken = itemView.findViewById(R.id.tv_token);
            tvJenisBerat = itemView.findViewById(R.id.tv_jenis_berat);
            tvNamaPetugas = itemView.findViewById(R.id.tv_nama_petugas);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
        }
    }
}
