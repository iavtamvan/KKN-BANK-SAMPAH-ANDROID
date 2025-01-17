package com.iavariav.kkntambakajibanksampah.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iavariav.kkntambakajibanksampah.BuildConfig;
import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.ui.petugas.PetugasActivity;
import com.iavariav.kkntambakajibanksampah.ui.user.DaftarUserActivity;
import com.iavariav.kkntambakajibanksampah.ui.user.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvCallme;
    private Button btnMasuk;

    private static final int RC_CAMERA_AND_LOCATION = 1;
    private String TAG;
    private TextView tvVersi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        getSupportActionBar().hide();
        methodRequiresTwoPermission();
        tvVersi.setText(BuildConfig.BUILD_TYPE + "." + BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
//                        Toast.makeText(LoginActivity.this, "" + token, Toast.LENGTH_SHORT).show();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("regId", token);
                        editor.apply();
//                        edtUsername.setText(token);

                    }
                });

        tvCallme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DaftarUserActivity.class));
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUsername.getText().toString().isEmpty() && edtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Harus di isi dahulu username/passwordnya", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Loading", "Validasi data...", false, false);
                    ApiService xApiService = Client.getInstanceRetrofit();
                    xApiService.getLogin(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            final JSONObject jsonObject = new JSONObject(response.body().string());
                                            String error_msg = jsonObject.optString("error_msg");
                                            String username = jsonObject.optString("username");
                                            String namaUser = jsonObject.optString("nama_user");
                                            String rule = jsonObject.optString("rule");
                                            String id = jsonObject.optString("id");
                                            String alamat_user = jsonObject.optString("alamat_user");
                                            String lat_user = jsonObject.optString("lat_user");
                                            String long_user = jsonObject.optString("long_user");
                                            String firebase_id = jsonObject.optString("firebase_id");
                                            String reg_id = jsonObject.optString("regID");

                                            if (rule.equalsIgnoreCase("user")) {
                                                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(Config.SHARED_PREF_USERNAME, username);
                                                editor.putString(Config.SHARED_PREF_RULE, rule);
                                                editor.putString(Config.SHARED_PREF_ID, id);
                                                editor.putString(Config.SHARED_PREF_ALAMAT_USER, alamat_user);
                                                editor.putString(Config.SHARED_PREF_LAT_USER, lat_user);
                                                editor.putString(Config.SHARED_PREF_LONG_USER, long_user);
                                                editor.putString(Config.SHARED_PREF_FIREBASE_ID, firebase_id);
                                                editor.putString(Config.SHARED_PREF_NAMA_LENGKAP, namaUser);
                                                editor.putString(Config.SHARED_PREF_REG_ID, reg_id);
                                                editor.apply();
                                                String firebaseId = sharedPreferences.getString("regId", "");

                                                ApiService apiService = Client.getInstanceRetrofit();
                                                apiService.postUpdateRegID(firebaseId, id)
                                                        .enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if (response.isSuccessful()) {
                                                                    try {
                                                                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                        String error_msg = jsonObject1.optString("error_msg");
                                                                        Toast.makeText(LoginActivity.this, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                                        loading.dismiss();
                                                                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                                                        finishAffinity();
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
                                                                Toast.makeText(LoginActivity.this, "U-Fire " + t.getMessage() , Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else if (rule.equalsIgnoreCase("petugas")) {
                                                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(Config.SHARED_PREF_USERNAME, username);
                                                editor.putString(Config.SHARED_PREF_RULE, rule);
                                                editor.putString(Config.SHARED_PREF_ID, id);
                                                editor.putString(Config.SHARED_PREF_ALAMAT_USER, alamat_user);
                                                editor.putString(Config.SHARED_PREF_LAT_USER, lat_user);
                                                editor.putString(Config.SHARED_PREF_LONG_USER, long_user);
                                                editor.putString(Config.SHARED_PREF_FIREBASE_ID, firebase_id);
                                                editor.putString(Config.SHARED_PREF_NAMA_LENGKAP, namaUser);
                                                editor.putString(Config.SHARED_PREF_REG_ID, reg_id);
                                                editor.apply();
                                                String firebaseId = sharedPreferences.getString("regId", "");
                                                ApiService apiService = Client.getInstanceRetrofit();
                                                apiService.postUpdateRegID(firebaseId, id)
                                                        .enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if (response.isSuccessful()) {
                                                                    try {
                                                                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                        String error_msg = jsonObject1.optString("error_msg");
                                                                        Toast.makeText(LoginActivity.this, "" + error_msg, Toast.LENGTH_SHORT).show();
                                                                        loading.dismiss();
                                                                        startActivity(new Intent(getApplicationContext(), PetugasActivity.class));
                                                                        finishAffinity();
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
                                                                Toast.makeText(LoginActivity.this, "U-Fire " + t.getMessage() , Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                loading.dismiss();
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
                                    loading.dismiss();
                                    Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }


    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.app_name),
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    private void initView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        tvCallme = findViewById(R.id.tv_callme);
        btnMasuk = findViewById(R.id.btn_masuk);
        tvVersi = findViewById(R.id.tv_versi);
    }
}
