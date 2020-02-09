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
import android.widget.EditText;
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
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AproveSampahAdapter extends RecyclerView.Adapter<AproveSampahAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StatusSampahModel> menuModels;
    String firebaseId;
    String regId;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    private EditText txt_nama;

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
        firebaseId = menuModels.get(position).getFirebaseId();
        regId = menuModels.get(position).getRegId();
//        Toast.makeText(context, "" + firebaseId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "" + firebaseId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "" + firebaseId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "" + firebaseId, Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "" + firebaseId, Toast.LENGTH_SHORT).show();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getStatusBarang("getStatusSampah", sharedPreferences.getString(Config.SHARED_PREF_ID, ""))
                .enqueue(new Callback<ArrayList<StatusSampahModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StatusSampahModel>> call, Response<ArrayList<StatusSampahModel>> response) {
                        if (response.isSuccessful()) {
                            menuModels = new ArrayList<>();
                            menuModels = response.body();
                            if (menuModels.get(position).getStatusSampah().equalsIgnoreCase("Ordered")) {
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
//                https://www.google.com/maps/search/-6.9987135,110.3055433
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
                                        if (response.isSuccessful()) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().string());
                                                String error_msg = jsonObject.optString("error_msg");
                                                Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                Uri navigationIntentUri = Uri.parse("google.navigation:q=" + menuModels.get(position).getLat() + "," + menuModels.get(position).getLongi());//creating intent with latlng
                                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                                                mapIntent.setPackage("com.google.android.apps.maps");
                                                context.startActivity(mapIntent);
                                                pushNotif(context, "Notifikasi", "Sampah di ambil oleh Petugas " + menuModels.get(position).getNamaPetugas(), firebaseId);
                                                ((PetugasActivity) context).refresh();
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
                dialog = new AlertDialog.Builder(context);
                dialogView = LayoutInflater.from(context).inflate(R.layout.form_data_popup, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Berat Sampah");

                txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama);

//        kosong();

                dialog.setPositiveButton("KIRIM", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (txt_nama.toString().isEmpty()) {
                            Toast.makeText(context, "Isi berat sampah", Toast.LENGTH_SHORT).show();
                        } else {
                            ApiService apiService = Client.getInstanceRetrofit();
                            apiService.postAproveSampah(menuModels.get(position).getTokenReg(), "Selesai", sharedPreferences.getString(Config.SHARED_PREF_NAMA_LENGKAP, ""))
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                                    String error_msg = jsonObject.optString("error_msg");
//                                                Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                    ApiService apiService1 = Client.getInstanceRetrofit();
                                                    apiService1.postSelesaiSampah(menuModels.get(position).getTokenReg(), txt_nama.getText().toString().trim())
                                                            .enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                    if (response.isSuccessful()){
                                                                        JSONObject jsonObject = null;
                                                                        try {
                                                                            jsonObject = new JSONObject(response.body().string());
                                                                            String error_msg = jsonObject.optString("error_msg");
                                                                            Toast.makeText(context, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                                            updatePoin(regId, menuModels.get(position).getTokenReg());
                                                                            pushNotif(context, "Notifikasi", "Sampah telah selesai di proses Oleh " + menuModels.get(position).getNamaPetugas(), firebaseId);
                                                                            ((PetugasActivity) context).refresh();
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
                    }
                });

                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


//                AlertDialog.Builder xBuilder = new AlertDialog.Builder(context);
//                xBuilder.setTitle("Peringatan");
//                xBuilder.setMessage("Selesai mengambil dari " + menuModels.get(position).getNamaPenyetor() + " ?");
//                xBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                xBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();

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

    public void pushNotif(final Context context, String title, String message, final String firebaseId) {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.pushNotif(title, message, "individual", firebaseId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
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

    public void updatePoin(String regId, String tokenReg) {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.postUpdatePoin(regId, tokenReg)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
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

    private void dialogForm() {
        dialog = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.form_data_popup, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Berat Sampah");

        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama);

//        kosong();

        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                nama    = txt_nama.getText().toString();
//                usia    = txt_usia.getText().toString();
//                alamat  = txt_alamat.getText().toString();
//                status = txt_status.getText().toString();
//
//                tvKlikToast.setText("Nama : " + nama + "\n" + "Usia : " + usia + "\n" + "Alamat : " + alamat + "\n" + "Status : " + status);
//                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
