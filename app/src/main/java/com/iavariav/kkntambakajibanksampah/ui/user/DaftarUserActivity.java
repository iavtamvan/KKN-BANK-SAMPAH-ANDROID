package com.iavariav.kkntambakajibanksampah.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.ui.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarUserActivity extends AppCompatActivity {

    private TextInputEditText edtNamaLengkap;
    private TextInputEditText edtNikUser;
    private TextView tvLokasi;
    private Button btnCekLokasi;
    private TextInputEditText edtUsername;
    private TextInputEditText edtPassword;
    private Button btnDaftar;

    private double latitude;
    private double longitude;

    private SimpleLocation location;
    private String TAG;
    private String token;
    private final static int PLACE_PICKER_REQUEST = 999;
    private String placeNameAdress;
    private String placeName;
    private TextInputEditText edtRt;
    private TextInputEditText edtRw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_user);
        initView();
        location = new SimpleLocation(DaftarUserActivity.this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("regId", token);
                        editor.apply();

                    }
                });

        btnCekLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DaftarUserActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                final int i1 = (r.nextInt(80) + 65);
                final ProgressDialog loading = ProgressDialog.show(DaftarUserActivity.this, "Loading", "Mengirim data...", false, false);
                ApiService apiService = Client.getInstanceRetrofit();
                apiService.postDaftarUser(edtNamaLengkap.getText().toString().trim(), edtNikUser.getText().toString().trim(),
                        tvLokasi.getText().toString().trim(), latitude, longitude, edtUsername.getText().toString().trim(),
                        edtPassword.getText().toString().trim(), "KKN-2020-USER" + i1 + location.getLongitude() + i1, token, edtRt.getText().toString().trim(), edtRw.getText().toString().trim())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        String errro_msg = jsonObject.optString("error_msg");
                                        if (errro_msg.equalsIgnoreCase("Berhasil")) {
                                            loading.dismiss();
                                            Toast.makeText(DaftarUserActivity.this, "" + errro_msg, Toast.LENGTH_SHORT).show();
                                            finishAffinity();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        } else {
                                            loading.dismiss();
                                            edtNamaLengkap.setText("");
                                            edtNikUser.setText("");
                                            edtUsername.setText("");
                                            edtPassword.setText("");
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
                                loading.dismiss();
                                Toast.makeText(DaftarUserActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(DaftarUserActivity.this, data);
                    Toast.makeText(this, "" + place.getAddress(), Toast.LENGTH_SHORT).show();
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    placeNameAdress = String.format("%s", place.getAddress());
                    placeName = String.format("%s", place.getName());
                    tvLokasi.setText(placeName + ", " + placeNameAdress);

                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.BUNDLE_LAT_AWAL, String.valueOf(latitude));
                    editor.putString(Config.BUNDLE_LONG_AWAL, String.valueOf(longitude));

                    editor.apply();
//                    placeNameAdress = String.format("%s", place.getAddress());
//                    placeName = String.format("%s", place.getName());
//                    encryptPlaceName = vigenere.encryptAlgorithm(placeName + ", " + placeNameAdress, keyEncrypt);
//                    latitudeTujuan = place.getLatLng().latitude;
//                    encryptLatitudeTujuan = vigenere.encryptAlgorithm(String.valueOf(latitudeTujuan), keyEncrypt);
//                    longitudeTUjuan = place.getLatLng().longitude;
//                    encryptLongitudeTUjuan = vigenere.encryptAlgorithm(String.valueOf(longitudeTUjuan), keyEncrypt);
//
////                    tvAlamatDetail.setText(placeName + ", " + placeNameAdress);
////                    getDistance(latitudeBerangkat, longitudeBerangkat, latitudeTujuan, longitudeTUjuan);
//                    hitungJarak = Haversine.hitungJarak(latitudeBerangkat, longitudeBerangkat, latitudeTujuan, longitudeTUjuan);
//                    stringJarak = Double.parseDouble(String.format("%.2f", hitungJarak));
//                    encryptStringJarak = vigenere.encryptAlgorithm(String.valueOf(stringJarak), keyEncrypt);
//                    hitungHargaBBM = (stringJarak / 11.5) * 7650;
//                    encryptHitungHargaBBM = vigenere.encryptAlgorithm(String.valueOf(hitungHargaBBM), keyEncrypt);
////                    tvAlamatDetail.setText(stringJarak + ">>> " + "Rp." + hitungHargaBBM);
//                    tvAlamatDetail.setText(placeName + ", " + placeNameAdress);
            }
        }
    }

    private void initView() {
        edtNamaLengkap = findViewById(R.id.edt_nama_lengkap);
        edtNikUser = findViewById(R.id.edt_nik_user);
        tvLokasi = findViewById(R.id.tv_lokasi);
        btnCekLokasi = findViewById(R.id.btn_cek_lokasi);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnDaftar = findViewById(R.id.btn_daftar);
        edtRt = findViewById(R.id.edt_rt);
        edtRw = findViewById(R.id.edt_rw);
    }
}
