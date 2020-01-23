package com.iavariav.kkntambakajibanksampah.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.ui.petugas.PetugasActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AproveSampahAdapter extends RecyclerView.Adapter<AproveSampahAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StatusSampahModel> menuModels;

    public AproveSampahAdapter(Context context, ArrayList<StatusSampahModel> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_aprove_tukar_barang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvJenisBerat.setText(menuModels.get(position).getNamaPenyetor() + ", " + menuModels.get(position).getJenisSampah() + ", " + menuModels.get(position).getBeratSampah() + "Kg");
        holder.tvDeskripsi.setText(menuModels.get(position).getAlamatPenyetor());
        holder.tvTgl.setText(menuModels.get(position).getTglInput());
        holder.tvPoint.setText(menuModels.get(position).getStatusSampah());

        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getStatusBarang("getStatusSampah", sharedPreferences.getString(Config.SHARED_PREF_ID, ""))
                .enqueue(new Callback<ArrayList<StatusSampahModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StatusSampahModel>> call, Response<ArrayList<StatusSampahModel>> response) {
                        if (response.isSuccessful()){
                            menuModels = new ArrayList<>();
                            menuModels = response.body();
                            if (menuModels.get(position).getStatusSampah().equalsIgnoreCase("Ordered")){
                                holder.btnAmbil.setVisibility(View.VISIBLE);
                                holder.btnAmbilSelesai.setVisibility(View.GONE);
                            } else {
                                holder.btnAmbil.setVisibility(View.GONE);
                                holder.btnAmbilSelesai.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StatusSampahModel>> call, Throwable t) {
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        holder.btnAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder xBuilder = new AlertDialog.Builder(context);
                xBuilder.setTitle("Peringatan");
                xBuilder.setMessage("Apakah anda yakin ingin mengambil sampah dari " + menuModels.get(position).getNamaPenyetor());
                xBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ApiService apiService = Client.getInstanceRetrofit();
                        apiService.postAproveSampah(menuModels.get(position).getTokenReg(), "Ambil", sharedPreferences.getString(Config.SHARED_PREF_NAMA_LENGKAP, ""))
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().string());
                                                String error_msg = jsonObject.optString("error_msg");
                                                Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                Uri gmmIntentUri = Uri.parse("geo:"+ menuModels.get(position).getLat() + ", " + menuModels.get(position).getLongi());
                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                                    context.startActivity(mapIntent);
                                                }
                                                ((PetugasActivity)context).refresh();
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
        });

        holder.btnAmbilSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder xBuilder = new AlertDialog.Builder(context);
                xBuilder.setTitle("Peringatan");
                xBuilder.setMessage("Selesai mengambil dari " + menuModels.get(position).getNamaPenyetor() + " ?");
                xBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ApiService apiService = Client.getInstanceRetrofit();
                        apiService.postAproveSampah(menuModels.get(position).getTokenReg(), "Selesai", sharedPreferences.getString(Config.SHARED_PREF_NAMA_LENGKAP, ""))
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().string());
                                                String error_msg = jsonObject.optString("error_msg");
                                                Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                ((PetugasActivity)context).refresh();
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
        });
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tvLvRiwayat;
        private TextView tvJenisBerat;
        private TextView tvTgl;
        private TextView tvPoint;
        private TextView tvDeskripsi;
        private CardView cvKlik;
        private Button btnAmbil;
        private Button btnAmbilSelesai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLvRiwayat = itemView.findViewById(R.id.tv_lv_riwayat);
            tvJenisBerat = itemView.findViewById(R.id.tv_jenis_berat);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
            tvPoint = itemView.findViewById(R.id.tv_point);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_barang);
            cvKlik = itemView.findViewById(R.id.cv_klik);
            btnAmbil = itemView.findViewById(R.id.btn_tukarkan);
            btnAmbilSelesai = itemView.findViewById(R.id.btn_tukarkan_selesai);
        }
    }
}
