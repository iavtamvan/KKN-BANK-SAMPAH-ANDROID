package com.iavariav.kkntambakajibanksampah.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_user);
        initView();


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService apiService = Client.getInstanceRetrofit();
                apiService.postDaftarUser(edtNamaLengkap.getText().toString().trim(), edtNikUser.getText().toString().trim(),
                        tvLokasi.getText().toString().trim(), "-43u5", "4389u", edtUsername.getText().toString().trim(),
                        edtPassword.getText().toString().trim(), "RegID", "ForebaseID")
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        String errro_msg = jsonObject.optString("error_msg");
                                        Toast.makeText(DaftarUserActivity.this, "" + errro_msg, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(DaftarUserActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    private void initView() {
        edtNamaLengkap = findViewById(R.id.edt_nama_lengkap);
        edtNikUser = findViewById(R.id.edt_nik_user);
        tvLokasi = findViewById(R.id.tv_lokasi);
        btnCekLokasi = findViewById(R.id.btn_cek_lokasi);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnDaftar = findViewById(R.id.btn_daftar);
    }
}
