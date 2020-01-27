package com.iavariav.kkntambakajibanksampah.ui.petugas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.adapter.AproveSampahAdapter;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.StatusSampahModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.service.ConnectionReceiver;
import com.iavariav.kkntambakajibanksampah.ui.SplashScreenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AproveFragment extends Fragment {


    private RecyclerView rv;
    private LinearLayout div;
    private ArrayList<StatusSampahModel> statusSampahModels;
    private String idUser;

    private TextView tvCountDownTime;
    private LinearLayout divContainer;
    private TextView tvCountdownTime;
    private Button btnShare;

    private ConnectionReceiver connectionReceiver;

    public AproveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aprove, container, false);
        initView(view);
        statusSampahModels = new ArrayList<>();
//        connectionReceiver = new ConnectionReceiver();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ayo ikuti bank sampah agar bumi kita semakin sehat :) \nDownload aplikasinya di ... \nKKN UPGRIS TAMBAKAJI 2020" );
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

//                pushNotif(getActivity(), "Notif", "COba", "dtj_1Q8NMCk:APA91bEgJQevs0ZqlRzB6Zg7Zu3qbt9SW63zAA_OjcAC2Jy4NKW_y5jfPdFBX07gtgQmXWNZ9r7N86WzkybQq0tRd7bCBmblB49AINu4q-SZGppkUBM75nFTWEznwiOMB3xyc5PN0z3n");
            }
        });

        getAprove();
        return view;
    }


    public void pushNotif (final Context context, String title, String message, final String regID){
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.pushNotif(title, message, "individual", regID)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String error_msg = jsonObject.optString("tittle");
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

    private void getTime() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountDownTime.setText("Otomatis Refresh: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                statusSampahModels.clear();
                getAprove();
            }

        }.start();
    }

    public void getAprove() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getStatusBarang("getStatusSampah", idUser)
                .enqueue(new Callback<ArrayList<StatusSampahModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StatusSampahModel>> call, Response<ArrayList<StatusSampahModel>> response) {
                        if (response.isSuccessful()) {
                            statusSampahModels = response.body();
                            if (statusSampahModels.isEmpty()) {
                                divContainer.setVisibility(View.GONE);
                                div.setVisibility(View.VISIBLE);
                                getTime();
                            } else {
                                divContainer.setVisibility(View.VISIBLE);
                                div.setVisibility(View.GONE);
                                AproveSampahAdapter aproveSampahAdapter = new AproveSampahAdapter(getActivity(), statusSampahModels);
                                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rv.setAdapter(aproveSampahAdapter);
                                aproveSampahAdapter.notifyDataSetChanged();
//                                Intent intent = new Intent("com.iavariav.kkntambakajibanksampah.SOME_ACTION");
//                                connectionReceiver.onReceive(getActivity(), intent);
//                                getActivity().sendBroadcast(intent);
                                getTime();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StatusSampahModel>> call, Throwable t) {
                        Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
        tvCountDownTime = view.findViewById(R.id.tv_countdown_time);
        div = view.findViewById(R.id.div);
        divContainer = view.findViewById(R.id.divContainer);
        btnShare = view.findViewById(R.id.btn_share);
    }
}
