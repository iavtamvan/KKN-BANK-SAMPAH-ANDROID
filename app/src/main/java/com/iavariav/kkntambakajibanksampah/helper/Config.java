package com.iavariav.kkntambakajibanksampah.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.iavariav.kkntambakajibanksampah.ui.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public final class Config {
    public static final String SHARED_PREF_NAME = "KKN_TAMBAK_AJI2020";
    public static final String SHARED_PREF_ID = "KKN_ID_USER";
    public static final String SHARED_PREF_NAMA_LENGKAP = "KKN_USERNAME";
    public static final String SHARED_PREF_RULE = "KKN_RULE";
    public static final String SHARED_PREF_ALAMAT_USER = "ALAMAT_USER";
    public static final String SHARED_PREF_LAT_USER = "LAT_USER";
    public static final String SHARED_PREF_LONG_USER = "LONG_USER";
    public static final String SHARED_PREF_FIREBASE_ID = "FIREBASE_ID";


    public static final String SHARED_PREF_REGID_FIREBASE = "KKN_REGID_FIREBASE";
    public static final String SHARED_PREF_ERROR_MSG = "KKN_ERROR_MSG";
    public static final String SHARED_PREF_KEY_ENCRYPT = "KKN_KEY_ENCRYPT";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final String BUNDLE_JENIS_KEPERLUAN = "BUNDLE_JENIS_KEPERLUAN";
    public static final String BUNDLE_JENIS_PEMESANAN = "BUNDLE_JENIS_PEMESANAN";
    public static final String BUNDLE_JENIS_KENDARAAN = "BUNDLE_JENIS_KENDARAAN";
    public static final String BUNDLE_KEBERANGKATAN_KAWASAN = "BUNDLE_KEBERANGKATAN_KAWASAN";
    public static final String BUNDLE_KEBERANGKATAN_WITEL = "BUNDLE_KEBERANGKATAN_WITEL";
    public static final String BUNDLE_KEBERANGKATAN_AREA_POOL = "BUNDLE_KEBERANGKATAN_AREA_POOL";
    public static final String BUNDLE_TUJUAN_ALAMAT_JEMPUT = "BUNDLE_TUJUAN_ALAMAT_JEMPUT";
    public static final String BUNDLE_TUJUAN_AREA = "BUNDLE_TUJUAN_AREA";
    public static final String BUNDLE_TUJUAN_ALAMAT_DETAIL_MAPS = "BUNDLE_TUJUAN_ALAMAT_DETAIL_MAPS";
    public static final String BUNDLE_LAT_AWAL = "BUNDLE_LAT_AWAL";
    public static final String BUNDLE_LONG_AWAL = "BUNDLE_LONG_AWAL";
    public static final String BUNDLE_LAT_TUJUAN = "BUNDLE_LAT_TUJUAN";
    public static final String BUNDLE_LONG_TUJUAN = "BUNDLE_LONG_TUJUAN";
    public static final String BUNDLE_WAKTU_KEBERANGKATAN = "BUNDLE_WAKTU_KEBERANGKATAN";
    public static final String BUNDLE_WAKTU_KEPULANGAN = "BUNDLE_WAKTU_KEPULANGAN";
    public static final String BUNDLE_NO_TELEPON_KANTOR = "BUNDLE_NO_TELEPON_KANTOR";
    public static final String BUNDLE_NO_HP = "BUNDLE_NO_HP";
    public static final String BUNDLE_JUMLAH_PENUMPANG = "BUNDLE_JUMLAH_PENUMPANG";
    public static final String BUNDLE_ISI_PENUMPANG = "BUNDLE_ISI_PENUMPANG";
    public static final String BUNDLE_KETERANGAN = "BUNDLE_KETERANGAN";
    public static final String BUNDLE_JARAK_PER_KM = "BUNDLE_JARAK_PER_KM";
    public static final String BUNDLE_BENSIN_PER_LITER = "BUNDLE_BENSIN_PER_LITER";
    public static final String BUNDLE_REG_TOKEN_PEMESANAN = "BUNDLE_REG_TOKEN_PEMESANAN";


    public static void sharedPref(Context context, String idUser, String username, String rule) {
        SharedPreferences sharedPreferences  = context.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.SHARED_PREF_ID, idUser);
        editor.putString(Config.SHARED_PREF_NAMA_LENGKAP, username);
        editor.putString(Config.SHARED_PREF_RULE, rule);


        editor.apply();
    }

//    public static void pushNotif(final Context context, String tittle, String message, String pushtype, String regid){
//        ApiService apiService = ApiConfig.getApiService();
//        apiService.postDataNotif(tittle, message, pushtype, regid)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()){
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String tittle = jsonObject.optString("tittle");
////                                Toast.makeText(context, "" + tittle, Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    public static void logout(Context context){
        SharedPreferences sharedPreferences  = context.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.SHARED_PREF_ID, "");
        editor.putString(Config.SHARED_PREF_NAMA_LENGKAP, "");
        editor.putString(Config.SHARED_PREF_RULE, "");
        editor.putString(Config.SHARED_PREF_ALAMAT_USER, "");
        editor.putString(Config.SHARED_PREF_LAT_USER, "");
        editor.putString(Config.SHARED_PREF_LONG_USER, "");
        editor.putString(Config.SHARED_PREF_FIREBASE_ID, "");
//        editor.putString("regId", "");
        editor.apply();

        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
