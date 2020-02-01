package com.iavariav.kkntambakajibanksampah.ui.user.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.adapter.BeritaAdapter;
import com.iavariav.kkntambakajibanksampah.helper.Config;
import com.iavariav.kkntambakajibanksampah.model.berita.ArticlesItem;
import com.iavariav.kkntambakajibanksampah.model.berita.RootBeritaModel;
import com.iavariav.kkntambakajibanksampah.rest.ApiService;
import com.iavariav.kkntambakajibanksampah.rest.Client;
import com.iavariav.kkntambakajibanksampah.rest.ClientNews;

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
public class HomeFragment extends Fragment {


    private TextView tvNamaSelamatDatang;
    private TextView tvPoinHome;
    private TextView tvOrder;
    private TextView tvOrderSelesai;
    private RecyclerView rvBeritaSampah;

    private ArrayList<ArticlesItem> articlesItems;
    private String regId;
    private String namaLengkap;
    private LinearLayout divCount;
    private LinearLayout divBerita;
    private ProgressBar pbBerita;
    private ProgressBar pbCount;
    private LinearLayout divCountOrdered;
    private LinearLayout divCountSelesai;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        articlesItems = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        regId = sharedPreferences.getString(Config.SHARED_PREF_REG_ID, "");
        Toast.makeText(getActivity(), "" + regId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "" + regId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "" + regId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "" + regId, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "" + regId, Toast.LENGTH_SHORT).show();
        namaLengkap = sharedPreferences.getString(Config.SHARED_PREF_NAMA_LENGKAP, "");
        tvNamaSelamatDatang.setText(namaLengkap);
        getHome();
        return view;
    }

    private void getHome() {
        ApiService apiService = Client.getInstanceRetrofit();
        apiService.getHome("getHome", regId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        getBerita();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status_selesai = jsonObject.optString("status_selesai");
                                String status_ordered = jsonObject.optString("status_ordered");
                                String point_user = jsonObject.optString("point_user");

                                if (status_selesai.isEmpty() && status_ordered.isEmpty() && point_user.isEmpty()){
                                    tvOrderSelesai.setText("Belum ada :(");
                                    tvOrder.setText("Belum ada :(");
                                    tvPoinHome.setText("Kosong :(");
                                } else {
                                    tvOrder.setText(status_ordered + " Menunggu");
                                    tvOrderSelesai.setText(status_selesai + " Selesai");
                                    tvPoinHome.setText(point_user + " Poin");
                                }

//                                Toast.makeText(getActivity(), "selesai " + status_selesai + "statur ordered " + status_ordered + "poin " + point_user, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "selesai " + status_selesai + "statur ordered " + status_ordered + "poin " + point_user, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "selesai " + status_selesai + "statur ordered " + status_ordered + "poin " + point_user, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "selesai " + status_selesai + "statur ordered " + status_ordered + "poin " + point_user, Toast.LENGTH_SHORT).show();
//                                pbCount.setVisibility(View.GONE);
//                                divCountOrdered.setVisibility(View.VISIBLE);
//                                divCountSelesai.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), "Home : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBerita() {
        ApiService apiService = ClientNews.getInstanceRetrofit();
        apiService.getBeritaSampah().enqueue(new Callback<RootBeritaModel>() {
            @Override
            public void onResponse(Call<RootBeritaModel> call, Response<RootBeritaModel> response) {
                if (response.isSuccessful()) {
                    articlesItems = response.body().getArticles();
                    pbBerita.setVisibility(View.GONE);
                    divBerita.setVisibility(View.VISIBLE);
                    BeritaAdapter beritaAdapter = new BeritaAdapter(getActivity(), articlesItems);
                    rvBeritaSampah.setAdapter(beritaAdapter);
                    rvBeritaSampah.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    beritaAdapter.notifyDataSetChanged();
                } else {
                    getBerita();
                }
            }

            @Override
            public void onFailure(Call<RootBeritaModel> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                getBerita();
            }
        });

    }

    private void initView(View view) {
        tvNamaSelamatDatang = view.findViewById(R.id.tv_nama_selamat_datang);
        tvPoinHome = view.findViewById(R.id.tv_poin_home);
        tvOrder = view.findViewById(R.id.tv_order);
        tvOrderSelesai = view.findViewById(R.id.tv_order_selesai);
        rvBeritaSampah = view.findViewById(R.id.rv_berita_sampah);
        divBerita = view.findViewById(R.id.div_berita);
        pbBerita = view.findViewById(R.id.pb_berita);
        pbCount = view.findViewById(R.id.pb_count);
        divCountOrdered = view.findViewById(R.id.div_count_ordered);
        divCountSelesai = view.findViewById(R.id.div_count_selesai);
    }
}
