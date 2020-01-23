package com.iavariav.kkntambakajibanksampah.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.ui.petugas.PetugasActivity;
import com.iavariav.kkntambakajibanksampah.ui.user.UserActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                String username = sp.getString(Config.SHARED_PREF_USERNAME, "");
                String rule = sp.getString(Config.SHARED_PREF_RULE, "");

                // TODO jika belum masuk ke LoginActivity
                if (username.equalsIgnoreCase("") || TextUtils.isEmpty(username)) {
                    finishAffinity();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                // TODO jika sudah nantinya akan masuk ke Home
                else {
                    if (rule.contains("user")) {
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                    } else if (rule.contains("petugas")) {
//                        Toast.makeText(SplashActivity.this, "rule : " + rule, Toast.LENGTH_SHORT).show();
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), PetugasActivity.class));
                    }

                }
            }
        }, 2000);
    }
}
