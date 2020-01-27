package com.iavariav.kkntambakajibanksampah.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;

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

    private SimpleLocation location;
    private String TAG;
    private String token;

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

            }
        });


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                final int i1 = (r.nextInt(80) + 65);
                ApiService apiService = Client.getInstanceRetrofit();
                apiService.postDaftarUser(edtNamaLengkap.getText().toString().trim(), edtNikUser.getText().toString().trim(),
                        tvLokasi.getText().toString().trim(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), edtUsername.getText().toString().trim(),
                        edtPassword.getText().toString().trim(), "KKN-2020-USER" + i1 + location.getLongitude() + i1, token)
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
