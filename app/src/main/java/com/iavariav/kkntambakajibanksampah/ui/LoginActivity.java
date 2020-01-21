package com.iavariav.kkntambakajibanksampah.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.ui.user.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvCallme;
    private Button btnMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUsername.getText().toString().isEmpty() && edtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Harus di isi dahulu username/passwordnya", Toast.LENGTH_SHORT).show();
                } else {
                    ApiService xApiService = Client.getInstanceRetrofit();
                    xApiService.getLogin(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.body().string());
                                            String error_msg = jsonObject.optString("error_msg");
                                            String username = jsonObject.optString("username");
                                            String rule = jsonObject.optString("rule");
                                            String id = jsonObject.optString("id");
                                            String alamat_user = jsonObject.optString("alamat_user");
                                            String lat_user = jsonObject.optString("lat_user");
                                            String long_user = jsonObject.optString("long_user");
                                            String firebase_id = jsonObject.optString("firebase_id");

                                            Toast.makeText(LoginActivity.this, "" + error_msg, Toast.LENGTH_SHORT).show();

                                            if (error_msg.equalsIgnoreCase("Berhasil Login")) {
                                                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(Config.SHARED_PREF_NAMA_LENGKAP, username);
                                                editor.putString(Config.SHARED_PREF_RULE, rule);
                                                editor.putString(Config.SHARED_PREF_ID, id);
                                                editor.putString(Config.SHARED_PREF_ALAMAT_USER, alamat_user);
                                                editor.putString(Config.SHARED_PREF_LAT_USER, lat_user);
                                                editor.putString(Config.SHARED_PREF_LONG_USER, long_user);
                                                editor.putString(Config.SHARED_PREF_FIREBASE_ID, firebase_id);
                                                editor.apply();
                                                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                            } else {
                                                Toast.makeText(LoginActivity.this, "" + error_msg, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        tvCallme = findViewById(R.id.tv_callme);
        btnMasuk = findViewById(R.id.btn_masuk);
    }
}