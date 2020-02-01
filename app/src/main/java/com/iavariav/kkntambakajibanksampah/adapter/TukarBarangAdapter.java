package com.iavariav.kkntambakajibanksampah.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.StokBarangModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TukarBarangAdapter extends RecyclerView.Adapter<TukarBarangAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StokBarangModel> menuModels;
    private String regId;
    private String idUser;
    public TukarBarangAdapter(Context context, ArrayList<StokBarangModel> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_tukar_barang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        regId = sharedPreferences.getString(Config.SHARED_PREF_REG_ID, "");
        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        holder.tvJenisBerat.setText(menuModels.get(position).getNamaBarang() + " - " + menuModels.get(position).getTipeBarang());
        Glide.with(context).load(menuModels.get(position).getFotoUrlBarang()).into(holder.tvLvRiwayat);
        holder.tvDeskripsi.setText(menuModels.get(position).getDeskripsiBarang());
        holder.tvTgl.setText(menuModels.get(position).getKadaluarsaBarang());
        holder.tvPoint.setText(menuModels.get(position).getPointBarang());

        holder.btnTukarkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHome(idUser, holder.tvPoint.getText().toString().trim(), menuModels.get(position).getNamaBarang(), menuModels.get(position).getNamaPemasok()
                        , menuModels.get(position).getTipeBarang(), menuModels.get(position).getDeskripsiBarang(), menuModels.get(position).getPointBarang(),menuModels.get(position).getRegBarang(),
                        menuModels.get(position).getFotoUrlBarang(), menuModels.get(position).getStatusBarang(), menuModels.get(position).getKadaluarsaBarang());
            }
        });
    }

    private void getHome(final String idUser, final String pointKurangBarang, final String namabarang, final String namaPemasok, final String tipeBarang, final String deskripsiBarang, final String pointBarang, final String regBarang, final String fotoUrlBarang,
                         final String statusBarang, final String kadaluarsaBarang) {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getHome("getHome", regId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status_selesai = jsonObject.optString("status_selesai");
                                String status_ordered = jsonObject.optString("status_ordered");
                                String point_user = jsonObject.optString("point_user");

                                if (point_user.equalsIgnoreCase("0")){
                                    Toast.makeText(context, "Poin anda kurang", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder xBuilder = new AlertDialog.Builder(context);
                                    xBuilder.setTitle("Peringatan");
                                    xBuilder.setMessage("Apakah anda yakin ingin menurkan ke barang " + namabarang);
                                    xBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ApiService apiService = Client.getInstanceRetrofit();
                                            apiService.postTukarSampah(idUser, pointKurangBarang, namabarang, namaPemasok
                                                    , tipeBarang, deskripsiBarang, pointBarang,regBarang,
                                                    fotoUrlBarang, statusBarang, kadaluarsaBarang,
                                                    "tukarPoint")
                                                    .enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            if (response.isSuccessful()){
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                                                    String error_msg = jsonObject.optString("error_msg");
                                                                    Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                    xBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Home : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount(){
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tvLvRiwayat;
        private TextView tvJenisBerat;
        private TextView tvTgl;
        private TextView tvPoint;
        private TextView tvDeskripsi;
        private CardView cvKlik;
        private Button btnTukarkan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLvRiwayat = itemView.findViewById(R.id.tv_lv_riwayat);
            tvJenisBerat = itemView.findViewById(R.id.tv_jenis_berat);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
            tvPoint = itemView.findViewById(R.id.tv_point);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_barang);
            cvKlik = itemView.findViewById(R.id.cv_klik);
            btnTukarkan = itemView.findViewById(R.id.btn_tukarkan);
        }
    }
}
