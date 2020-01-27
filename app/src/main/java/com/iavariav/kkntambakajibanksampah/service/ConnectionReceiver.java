package com.iavariav.kkntambakajibanksampah.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.ui.SplashScreenActivity;

import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ConnectionReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private ArrayList<StatusSampahModel> statusSampahModels;
    @Override
    public void onReceive(final Context context, Intent intent) {
        getNotif(context);
    }

    public void getNotif(final Context context){
        statusSampahModels = new ArrayList<>();
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getSampahNotif("getSampahNotif")
                .enqueue(new Callback<ArrayList<StatusSampahModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StatusSampahModel>> call, Response<ArrayList<StatusSampahModel>> response) {
                        if (response.isSuccessful()){
                            statusSampahModels = response.body();
                            if (statusSampahModels.isEmpty()) {
                                Log.d(TAG, "onResponse: kosong" );
                            } else {
                                for (int i = 0; i < statusSampahModels.size(); i++) {
                                    showNotif(context, statusSampahModels.get(i).getNamaPenyetor(), statusSampahModels.get(i).getAlamatPenyetor());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StatusSampahModel>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });


    }
    public void getTime(final Context context){
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                Toast.makeText(context, "" + millisUntilFinished/1000, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "" + millisUntilFinished/1000, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "" + millisUntilFinished/1000, Toast.LENGTH_SHORT).show();
//                getNotif(context, millisUntilFinished/1000);
            }

            public void onFinish() {
                getTime(context);
            }

        }.start();
    }

    public void showNotif(Context context, String title, String alamat){
        /**Creates an explicit intent for an Activity in your app**/


        Intent resultIntent = new Intent(context, SplashScreenActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Random r = new Random();
        final int i1 = (r.nextInt(80) + 65);

        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Ayo ambil sampah " + title)
                .setContentText(alamat)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}
